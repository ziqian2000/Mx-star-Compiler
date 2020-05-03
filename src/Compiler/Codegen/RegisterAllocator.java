package Compiler.Codegen;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.Assembly.AsmFunction;
import Compiler.Assembly.Assembly;
import Compiler.Assembly.Instr.*;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Register;
import Compiler.IR.StackPointerOffset;
import Compiler.Utils.Config;

import java.util.*;

public class RegisterAllocator {

	private Assembly asm;
	private int K;

	private Set<Register> preColored = new HashSet<>();
	private Set<Register> initial = new HashSet<>();
	private Set<Register> simplifyWorklist = new HashSet<>();
	private Set<Register> freezeWorklist = new HashSet<>();
	private Set<Register> spillWorklist = new HashSet<>();
	private Set<Register> spilledNodes = new HashSet<>();
	private Set<Register> coalescedNodes = new HashSet<>();
	private Set<Register> coloredNodes = new HashSet<>();
	private Stack<Register> selectStack = new Stack<>();

	private Set<AsmMove> coalescedMoves = new HashSet<>();
	private Set<AsmMove> constrainedMoves = new HashSet<>();
	private Set<AsmMove> frozenMoves = new HashSet<>();
	private Set<AsmMove> worklistMoves = new HashSet<>();
	private Set<AsmMove> activeMoves = new HashSet<>();

	private Set<Edge> adjSet = new HashSet<>();
	private Map<Register, Set<Register>> adjList = new HashMap<>();
	private Map<Register, Integer> degree = new HashMap<>();
	private Map<Register, Set<AsmMove>> moveList = new HashMap<>();
	private Map<Register, Register> alias = new HashMap<>();
	private Map<Register, String> color = new HashMap<>();

	private Map<Register, StackPointerOffset> spillAddr = new HashMap<>();

	public RegisterAllocator(Assembly asm){
		this.asm = asm;
		K = asm.allocatableRegName.length;
	}

	public void run(){
		for(AsmFunction func : asm.getFunctionList()) if(!func.getIsBuiltin()){
			while(true){
				System.out.println("A");
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
				assignColor();
				if(!spillWorklist.isEmpty())
					rewriteProgram(func);
				else break;
			}
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
		adjList.clear();
		degree.clear();
		moveList.clear();
		alias.clear();
		color.clear();

		spillAddr.clear();
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

				Set<Register> newIn = new HashSet<>(BB.getLiveOut()), newOut = new HashSet<>();
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

		// init data structure
		for(var name : asm.allocatableRegName){
			initial.add(asm.getPhyReg(name));
		}
		for(var BB : func.getBBList()){
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				initial.addAll(ins.getUseRegister());
				if(ins.getDefRegister() != null) initial.add(ins.getDefRegister());
			}
		}
		for(var reg : initial){
			degree.put(reg, 0);
		}

		// pre-colored
		for(var name : asm.allocatableRegName){
			var reg = asm.getPhyReg(name);

			preColored.add(reg);
			degree.put(reg, Integer.MAX_VALUE);
		}

		// new set
		for(var reg : initial){
			adjList.put(reg, new HashSet<>());
			moveList.put(reg, new HashSet<>());
		}
	}

	private void build(AsmFunction func){

		for(AsmBasicBlock BB : func.getBBList()){
			Set<Register> live = new HashSet<>(BB.getLiveOut());
			for(AsmIns ins = BB.getTailIns(); ins != null; ins = ins.getPrevIns()){

				if(ins instanceof AsmMove){
					live.removeAll(ins.getUseRegister());

					moveList.get(ins.getDefRegister()).add((AsmMove)ins);
					for(var reg : ins.getUseRegister()) moveList.get(reg).add((AsmMove)ins);

					worklistMoves.add((AsmMove)ins);
				}

				List<Register> defs = new ArrayList<>();
				if(ins.getDefRegister() != null) defs.add(ins.getDefRegister());
				if(ins instanceof AsmCall) {
					for(var name : asm.callerSaveRegisterName){
						defs.add(asm.getPhyReg(name));
					}
				}

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
			if(degree.get(reg) >= K) spillWorklist.add(reg);
			else if(moveRelated(reg)) freezeWorklist.add(reg);
			else simplifyWorklist.add(reg);
		}
		initial.clear();
	}

	private Set<Register> adjacent(Register reg){
		Set<Register> ret = new HashSet<>(adjList.get(reg));
		ret.removeAll(selectStack);
		ret.removeAll(coalescedNodes);
		return ret;
	}

	private Set<AsmMove> nodeMoves(Register reg){
		Set<AsmMove> ret = new HashSet<>(moveList.get(reg));
		Set<AsmMove> tmp = new HashSet<>(activeMoves);
		tmp.addAll(worklistMoves);
		ret.retainAll(tmp);
		return ret;
	}

	private boolean moveRelated(Register reg){
		return !nodeMoves(reg).isEmpty();
	}

	private void simplify(){
		Register reg = simplifyWorklist.iterator().next();
		simplifyWorklist.remove(reg);
		selectStack.push(reg);
		for(var nei : adjacent(reg)) decrementDegree(nei);
	}

	private void decrementDegree(Register reg){
		int d = degree.get(reg);
		degree.put(reg, d - 1);
		if(d == K){
			var regs = adjacent(reg);
			regs.add(reg);
			enableMoves(regs);

			spillWorklist.remove(reg);
			if(moveRelated(reg)) freezeWorklist.add(reg);
			else simplifyWorklist.add(reg);
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
		if(!preColored.contains(reg) && !moveRelated(reg) && degree.get(reg) < K){
			freezeWorklist.remove(reg);
			simplifyWorklist.add(reg);
		}
	}

	private boolean OK(Register t, Register r){
		return degree.get(t) < K || preColored.contains(t) || adjSet.contains(new Edge(t, r));
	}

	private boolean conservative(Set<Register> neighbors){
		int cnt = 0;
		for(var reg : neighbors){
			if(degree.get(reg) >= K) cnt += 1;
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
		alias.put(v, u);
		moveList.get(u).addAll(moveList.get(v));
		enableMoves(Collections.singleton(v));
		for(var nei : adjacent(v)){
			addEdge(nei, u);
			decrementDegree(nei);
		}
		if(degree.get(u) >= K && freezeWorklist.contains(u)){
			freezeWorklist.remove(u);
			spillWorklist.add(u);
		}
	}

	private Register getAlias(Register reg){
		if(coalescedNodes.contains(reg)) return getAlias(alias.get(reg));
		else return reg;
	}

	private void freeze(){
		var reg = freezeWorklist.iterator().next();
		freezeWorklist.remove(reg);
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
				freezeWorklist.remove(v);
				simplifyWorklist.add(v);
			}
		}
	}

	private void selectSpill(){
		// todo : better selecting policy
		var reg = spillWorklist.iterator().next();
		spillWorklist.remove(reg);
		simplifyWorklist.add(reg);
		freezeMoves(reg);
	}

	private void assignColor(){
		while(!selectStack.isEmpty()){
			var reg = selectStack.pop();

			Set<String> okColors = new HashSet<>(Arrays.asList(asm.getAllocatableRegName()));

			for(var nei : adjList.get(reg)){
				var trueNei = getAlias(nei);
				if(coloredNodes.contains(trueNei) || preColored.contains(trueNei))
					okColors.remove(color.get(trueNei));
			}

			if(okColors.isEmpty()){
				spilledNodes.add(reg);
			}
			else{
				coloredNodes.add(reg);
				// todo : assign caller-save first !!!
				String c = okColors.iterator().next();
				color.put(reg, c);
			}
		}
		coalescedNodes.forEach(reg -> color.put(reg, color.get(getAlias(reg))));
	}

	void rewriteProgram(AsmFunction func){

		for(var spilledReg : spilledNodes){
			spillAddr.put(spilledReg, new StackPointerOffset(func.allocStack(Config.SIZE), false, func, asm.getPhyReg("sp")));
		}

		for(var BB : func.getBBList()){
			AsmIns nextIns;
			for(AsmIns ins = BB.getHeadIns(); ins != null; ins = nextIns){
				nextIns = ins.getNextIns();

				for(var useReg : ins.getUseRegister()){
					if(spillAddr.containsKey(useReg)){
						// todo : peephole optimization if def == useReg
						// todo : change MOVE to LOAD in some case
						var tmp = new I32Value("spillUse_" + useReg.getIdentifier());
						ins.prependIns(new AsmLoad(spillAddr.get(useReg), tmp, Config.SIZE));
						ins.replaceUseRegister(useReg, tmp);
					}
				}
				if(spillAddr.containsKey(ins.getDefRegister())){
					var defReg = ins.getDefRegister();
					var tmp = new I32Value("spillDef_" + defReg.getIdentifier());
					ins.replaceDefRegister(tmp);
					ins.appendIns(new AsmStore(spillAddr.get(defReg), tmp, new I32Value("store_tmp"), Config.SIZE));
				}
			}
		}
	}

	private void addEdge(Register u, Register v){
		if(u == v || adjSet.contains(new Edge(u, v))) return;
		if(!preColored.contains(u)){
			adjSet.add(new Edge(u, v));
			adjList.get(u).add(v);
			degree.put(u, degree.get(u) + 1);
		}
		if(!preColored.contains(v)){
			adjSet.add(new Edge(v, u));
			adjList.get(v).add(u);
			degree.put(v, degree.get(v) + 1);
		}
	}

}

class Edge{
	Register u, v;
	Edge(Register u, Register v){
		this.u = u;
		this.v = v;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Edge edge = (Edge) o;
		return u == edge.u && v == edge.v;
	}

	@Override
	public int hashCode() {
		return Objects.hash(u, v);
	}
}
