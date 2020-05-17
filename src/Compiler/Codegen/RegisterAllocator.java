package Compiler.Codegen;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.Assembly.AsmFunction;
import Compiler.Assembly.Assembly;
import Compiler.Assembly.Instr.*;
import Compiler.Assembly.Operand.PhysicalRegister;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Register;
import Compiler.IR.StackPointerOffset;
import Compiler.IRVisitor.Edge;
import Compiler.Utils.Config;

import java.util.*;

public class RegisterAllocator {

	private Assembly asm;
	private int K;

	private Set<Register> preColored = new LinkedHashSet<>();
	private Set<Register> initial = new LinkedHashSet<>();
	private Set<Register> simplifyWorklist = new LinkedHashSet<>();
	private Set<Register> freezeWorklist = new LinkedHashSet<>();
	private Set<Register> spillWorklist = new LinkedHashSet<>();
	private Set<Register> spilledNodes = new LinkedHashSet<>();
	private Set<Register> coalescedNodes = new LinkedHashSet<>();
	private Set<Register> coloredNodes = new LinkedHashSet<>();
	private Stack<Register> selectStack = new Stack<>();

	private Set<AsmMove> coalescedMoves = new LinkedHashSet<>();
	private Set<AsmMove> constrainedMoves = new LinkedHashSet<>();
	private Set<AsmMove> frozenMoves = new LinkedHashSet<>();
	private Set<AsmMove> worklistMoves = new LinkedHashSet<>();
	private Set<AsmMove> activeMoves = new LinkedHashSet<>();

	private Set<Edge> adjSet = new LinkedHashSet<>();

	public RegisterAllocator(Assembly asm){
		this.asm = asm;
		K = asm.allocatableRegName.length;
	}

	public void run(){
		for(AsmFunction func : asm.getFunctionList()) if(!func.getIsBuiltin()){
			while(true){
				clean();
				init(func);
				livenessAnalysis(func);
				build(func);
				makeWorklist();
				while(!simplifyWorklist.isEmpty() || !worklistMoves.isEmpty() || !freezeWorklist.isEmpty() || !spillWorklist.isEmpty()){
					if(!simplifyWorklist.isEmpty()) simplify();
					else if(!worklistMoves.isEmpty()) coalesce();
					else if(!freezeWorklist.isEmpty()) freeze();
					else selectSpill();
				}
				assignColors();
				if(!spilledNodes.isEmpty()) {
					rewriteProgram(func);
				}
				else break;
			}
			applyColoring(func);
		}
	}

	private void clean(){
		preColored.clear();
		initial.clear();
		simplifyWorklist.clear();
		freezeWorklist.clear();
		spillWorklist.clear();
		spilledNodes.clear();
		coalescedNodes.clear();
		coloredNodes.clear();
		selectStack.clear();

		coalescedMoves.clear();
		constrainedMoves.clear();
		frozenMoves.clear();
		worklistMoves.clear();
		activeMoves.clear();

		adjSet.clear();
	}

	private void livenessAnalysis(AsmFunction func){
		// use & def for each basic block
		for(AsmBasicBlock BB : func.getBBList()){
			BB.getDef().clear();
			BB.getUse().clear();
			BB.getLiveIn().clear();
			BB.getLiveOut().clear();
			for(AsmIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				// use
				for(Register use : ins.getUseRegister()){
					if(!BB.getDef().contains(use))
						BB.getUse().add(use);
				}
				// def
				if(ins.getDefRegister() != null)
					BB.getDef().add(ins.getDefRegister());
				if(ins instanceof AsmCall){
					for(var name : asm.getCallerSaveRegisterName()){
						BB.getDef().add(asm.getPhyReg(name));
					}
				}
			}
		}
		// iterative analysis
		for(boolean changed = true; changed; ){
			changed = false;
			for(int i = func.getBBList().size() - 1; i >= 0; i--){ // reversed order makes it faster
				AsmBasicBlock BB = func.getBBList().get(i);

				Set<Register> newIn = new LinkedHashSet<>(BB.getLiveOut()), newOut = new LinkedHashSet<>();
				newIn.removeAll(BB.getDef());
				newIn.addAll(BB.getUse());
				BB.getSucBBList().forEach(sucBB -> newOut.addAll(sucBB.getLiveIn()));
				if(!newIn.equals(BB.getLiveIn()) || !newOut.equals(BB.getLiveOut())){
					changed = true;
					BB.setLiveIn(newIn);
					BB.setLiveOut(newOut);
				}
			}
		}
	}

	private void init(AsmFunction func){

		// prepare
		func.makeBBList();

		// pre-colored
		for(var name : asm.phyRegName){
			var reg = asm.getPhyReg(name);

			reg.color = name;
			preColored.add(reg);
			reg.degree = Integer.MAX_VALUE;
		}

		// fake initial, consist of all registers, including physical (pre-colored) registers
		initial.addAll(preColored);
		for(var BB : func.getBBList()){
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				initial.addAll(ins.getUseRegister());
				if(ins.getDefRegister() != null) initial.add(ins.getDefRegister());
			}
		}

		// new set
		initial.forEach(Register::clean);

		// real initial, remove physical (pre-colored) registers
		initial.removeAll(preColored);

		for(var reg : initial){
			reg.degree = 0;
			reg.color = null;
		}
	}

	private void build(AsmFunction func){

		for(AsmBasicBlock BB : func.getBBList()){
			Set<Register> live = new LinkedHashSet<>(BB.getLiveOut());
			for(AsmIns ins = BB.getTailIns(); ins != null; ins = ins.getPrevIns()){

				if(ins instanceof AsmMove){
					live.removeAll(ins.getUseRegister());

					ins.getDefRegister().moveList.add((AsmMove)ins);
					for(var reg : ins.getUseRegister()) reg.moveList.add((AsmMove)ins);

					worklistMoves.add((AsmMove)ins);
				}

				List<Register> defs = new ArrayList<>();
				if(ins.getDefRegister() != null) defs.add(ins.getDefRegister());
				if(ins instanceof AsmCall) {
					for(var name : asm.callerSaveRegisterName){
						defs.add(asm.getPhyReg(name));
					}
				}


				if(ins instanceof AsmStore && ((AsmStore) ins).getRt() != null)
					addEdge(((AsmStore) ins).getRs(), ((AsmStore) ins).getRt());
				for(var def : defs){
					for (Register reg : live) {
						addEdge(def, reg);
					}
				}

				// update live out
				live.removeAll(defs);
				live.addAll(ins.getUseRegister());

			}
		}
	}

	private void makeWorklist(){
		for(var reg : initial) {
			if(reg.degree >= K) spillWorklist.add(reg);
			else if(moveRelated(reg)) freezeWorklist.add(reg);
			else{
				assert !(reg instanceof PhysicalRegister);
				simplifyWorklist.add(reg);
			}
		}
		initial.clear();
	}

	private Set<Register> adjacent(Register reg){
		Set<Register> ret = new LinkedHashSet<>(reg.adjList);
		ret.removeAll(selectStack);
		ret.removeAll(coalescedNodes);
		return ret;
	}

	private Set<AsmMove> nodeMoves(Register reg){
		Set<AsmMove> ret = new LinkedHashSet<>(reg.moveList);
		Set<AsmMove> tmp = new LinkedHashSet<>(activeMoves);
		tmp.addAll(worklistMoves);
		ret.retainAll(tmp);
		return ret;
	}

	private boolean moveRelated(Register reg){
		return !nodeMoves(reg).isEmpty();
	}

	private void simplify(){
		Register reg = simplifyWorklist.iterator().next();
		assert !(reg instanceof PhysicalRegister);
		simplifyWorklist.remove(reg);
		selectStack.push(reg);
		for(var nei : adjacent(reg)) decrementDegree(nei);
	}

	private void decrementDegree(Register reg){
		int d = reg.degree;
		reg.degree = d-1;
		if(d == K){
			var regs = adjacent(reg);
			regs.add(reg);
			enableMoves(regs);

			spillWorklist.remove(reg);
			if(moveRelated(reg)) freezeWorklist.add(reg);
			else {
				assert !(reg instanceof PhysicalRegister);
				simplifyWorklist.add(reg);
			}
		}
	}

	private void enableMoves(Set<Register> regs){
		for(var reg : regs){
			for(var moveIns : nodeMoves(reg)){
				if(activeMoves.contains(moveIns)){
					activeMoves.remove(moveIns);
					worklistMoves.add(moveIns);
				}
			}
		}
	}

	private void addWorklist(Register reg){
		if(!preColored.contains(reg) && !moveRelated(reg) && reg.degree < K){
			freezeWorklist.remove(reg);
			assert !(reg instanceof PhysicalRegister);
			simplifyWorklist.add(reg);
		}
	}

	private boolean OK(Register t, Register r){
		return t.degree < K || preColored.contains(t) || adjSet.contains(new Edge(t, r));
	}

	private boolean conservative(Set<Register> neighbors){
		int cnt = 0;
		for(var reg : neighbors){
			if(reg.degree >= K) cnt += 1;
		}
		return cnt < K;
	}

	private void coalesce(){
		var moveIns = worklistMoves.iterator().next();
		Register x = getAlias(moveIns.getRs1()), y = getAlias(moveIns.getRd()), u, v;
		if(preColored.contains(y)) {u = y; v = x;}
		else {u = x; v = y;}
		worklistMoves.remove(moveIns);
		if(u == v){
			coalescedMoves.add(moveIns);
			addWorklist(u);
		}
		else if(preColored.contains(v) || adjSet.contains(new Edge(u, v))){
			constrainedMoves.add(moveIns);
			addWorklist(u);
			addWorklist(v);
		}
		else{

			boolean cond = true;
			if(preColored.contains(u)){
				for(var nei : adjacent(v)) cond &= OK(nei, u);
			}
			else{
				var tmp = adjacent(u);
				tmp.addAll(adjacent(v));
				cond = conservative(tmp);
			}

			if(cond){
				coalescedMoves.add(moveIns);
				combine(u, v);
				addWorklist(u);
			}
			else{
				activeMoves.add(moveIns);
			}

		}
	}

	private void combine(Register u, Register v){
		freezeWorklist.remove(v);
		spillWorklist.remove(v);
		coalescedNodes.add(v);
		v.alias = u;
		u.moveList.addAll(v.moveList);
		enableMoves(Collections.singleton(v));
		for(var nei : adjacent(v)){
			addEdge(nei, u);
			decrementDegree(nei);
		}
		if(u.degree >= K && freezeWorklist.contains(u)){
			freezeWorklist.remove(u);
			spillWorklist.add(u);
		}
	}

	private Register getAlias(Register reg){
		if(coalescedNodes.contains(reg)) return getAlias(reg.alias);
		else return reg;
	}

	private void freeze(){
		var reg = freezeWorklist.iterator().next();
		freezeWorklist.remove(reg);
		assert !(reg instanceof PhysicalRegister);
		simplifyWorklist.add(reg);
		freezeMoves(reg);
	}

	private void freezeMoves(Register u){
		Register v;
		for(var moveIns : nodeMoves(u)){
			if(getAlias(moveIns.getRs1()) == getAlias(u)){
				v = getAlias(moveIns.getRd());
			}
			else v = getAlias(moveIns.getRs1());
			activeMoves.remove(moveIns);
			frozenMoves.add(moveIns);
			if(freezeWorklist.contains(v) && nodeMoves(v).isEmpty()){
				assert !(v instanceof PhysicalRegister);
				freezeWorklist.remove(v);
				simplifyWorklist.add(v);
			}
		}
	}

	private void selectSpill(){
		// todo : better selecting policy
		var reg = spillWorklist.iterator().next();
		assert !(reg instanceof PhysicalRegister);
		spillWorklist.remove(reg);
		simplifyWorklist.add(reg);
		freezeMoves(reg);
	}

	private void assignColors(){
		while(!selectStack.isEmpty()){
			var reg = selectStack.pop();
			assert !(reg instanceof PhysicalRegister);

			Set<String> okColors = new LinkedHashSet<>(Arrays.asList(asm.getAllocatableRegName()));

			for(var nei : reg.adjList){
				var trueNei = getAlias(nei);
				if(coloredNodes.contains(trueNei) || preColored.contains(trueNei)) {
					okColors.remove(trueNei.color);
				}
			}

			if(okColors.isEmpty()){
				spilledNodes.add(reg);
			}
			else{
				coloredNodes.add(reg);

				// try to assign caller-save first
				String c = null;
				for(var r : asm.getCallerSaveRegisterName())
					if(okColors.contains(r)){
						c = r;
						break;
					}
				if(c == null){
					for(var r : asm.getCalleeSaveRegisterName())
						if(okColors.contains(r)){
							c = r;
							break;
						}
				}

				assert c != null;
				reg.color = c;
			}
		}
		for (Register reg : coalescedNodes) {
			assert !(reg instanceof PhysicalRegister);
			assert getAlias(reg).color != null;
			reg.color = getAlias(reg).color;
		}
	}

	void rewriteProgram(AsmFunction func){

		for(var spilledReg : spilledNodes){
			spilledReg.spillAddr = new StackPointerOffset(func.stackAllocFromBot(Config.SIZE), true, func, asm.getPhyReg("sp"));
		}

		for(var BB : func.getBBList()){
			AsmIns nextIns;
			for(AsmIns ins = BB.getHeadIns(); ins != null; ins = nextIns){
				nextIns = ins.getNextIns();

				// spill use
				for(var useReg : ins.getUseRegister()){
					if(useReg.spillAddr != null){
						// todo : peephole optimization if def == useReg
						// todo : change MOVE to LOAD in some case
						assert !(useReg instanceof PhysicalRegister);
						var tmp = new I32Value("spillUse_" + useReg.getIdentifier());
						ins.prependIns(new AsmLoad(useReg.spillAddr, tmp, Config.SIZE));
						ins.replaceUseRegister(useReg, tmp);
					}
				}
				// spill def
				if(ins.getDefRegister() != null && ins.getDefRegister().spillAddr != null){
					var defReg = ins.getDefRegister();
					assert !(defReg instanceof PhysicalRegister);
					var tmp = new I32Value("spillDef_" + defReg.getIdentifier());
					ins.replaceDefRegister(tmp);
					ins.appendIns(new AsmStore(defReg.spillAddr, tmp, null, Config.SIZE));
				}
			}
		}
	}

	private void addEdge(Register u, Register v){
		if(u == v || adjSet.contains(new Edge(u, v))) return;
//		debug(u.getIdentifier() + " " + v.getIdentifier());
		adjSet.add(new Edge(u, v));
		adjSet.add(new Edge(v, u));
		if(!preColored.contains(u)){
			u.adjList.add(v);
			u.degree += 1;
		}
		if(!preColored.contains(v)){
			v.adjList.add(u);
			v.degree += 1;
		}
	}

	private void applyColoring(AsmFunction func){

		for(var BB : func.getBBList()){
			for(AsmIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){

				for(var useReg : ins.getUseRegister()){
					if(!(useReg instanceof PhysicalRegister) && useReg.color != null){
						assert !useReg.getIdentifier().equals("ra");
						var tmp = asm.getPhyReg(useReg.color);
						ins.replaceUseRegister(useReg, tmp);
					}
				}

				var defReg = ins.getDefRegister();
				if(defReg != null && !(defReg instanceof PhysicalRegister) && defReg.color != null){
					assert !defReg.getIdentifier().equals("ra");
					var tmp = asm.getPhyReg(defReg.color);
					ins.replaceDefRegister(tmp);
				}
			}
		}

	}

	void debug(String s){
		System.err.println(s);
	}

}