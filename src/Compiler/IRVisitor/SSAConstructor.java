package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.IRIns;
import Compiler.IR.Instr.Phi;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;

import java.util.*;

/**
 * If encounter TLE:
 * 	Optimize the Map defined here (e.g. dfn, vertex, iDom...) as it takes an extra O(log n) time to calculate.
 * 	Store them in BB class if necessary.
 */

public class SSAConstructor {

	IR ir;

	public SSAConstructor(IR ir){
		this.ir = ir;
	}

	public void run(){
		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			constructDominatorTree(func);
			constructDominanceFrontier(func);
			traverseGlobals(func);
			insertPhi(func);
			renameVariables(func);
		}
		ir.setSSAForm(true);
	}

	/**
	 * rename variables
	 */
	Map<VirtualRegister, Stack<Integer>> stack = new HashMap<>();
	Map<VirtualRegister, Integer> count = new HashMap<>();

	public void recursiveRenaming(BasicBlock n){
		for(IRIns S = n.getHeadIns(), nextIns; S != null; S = nextIns){
			nextIns = S.getNextIns();

			// use
			if(!(S instanceof Phi)){
				List<Operand> oprList = S.getOperands();
				List<Operand> newOprList = new ArrayList<>();
				List<VirtualRegister> useRegisterList = S.getUseRegister();
				for(Operand opr : oprList){
					if(opr instanceof VirtualRegister && !((VirtualRegister) opr).isGlobal() && useRegisterList.contains(opr)){
						// renaming global variable forbidden
						int i = stack.get(opr).peek();
						newOprList.add(((VirtualRegister) opr).getSSARenamedRegister(i));
					}
					else newOprList.add(opr);
				}
				S = S.replaceSelfWithCopy(newOprList, S.getBBs());
			}
			// define
			if(S.getDefRegister() != null){
				VirtualRegister defRegister = S.getDefRegister().getOriginVar();
				if(defRegister.isGlobal()) continue; // renaming global variable forbidden
				if(stack.get(defRegister) == null) {
					count.put(defRegister, -1);
					stack.put(defRegister, new Stack<>());
				}
				int i = count.get(defRegister) + 1;
				count.put(defRegister, i);
				stack.get(defRegister).push(i);
				S.setDefRegister(defRegister.getSSARenamedRegister(i));
			}
		}
		for(BasicBlock y : n.getSucBBList()){
			for(IRIns ins = y.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(ins instanceof Phi){
					VirtualRegister origin = ((Phi) ins).getDst().getOriginVar();
					VirtualRegister renamedRegister = stack.containsKey(origin) && !stack.get(origin).isEmpty()
											? origin.getSSARenamedRegister(stack.get(origin).peek())
											: null;
					((Phi) ins).getPath().put(n, renamedRegister);
				}
			}
		}

		iDomChildren.get(n).forEach(this::recursiveRenaming);

		for(IRIns S = n.getHeadIns(); S != null; S = S.getNextIns()){
			// define
			if(S.getDefRegister() != null){
				VirtualRegister defRegister = S.getDefRegister().getOriginVar();
				if(defRegister.isGlobal()) continue; // renaming global variable forbidden
				stack.get(defRegister).pop();
			}
		}

	}
	public void renameVariables(Function function){
		// todo : eliminate useless arguments

		// obj
		if(function.getObj() != null){
			VirtualRegister obj = function.getObj();
			count.put(obj, 0);
			stack.put(obj, new Stack<>());
			stack.get(obj).push(count.get(obj));
			function.setObj(obj.getSSARenamedRegister(count.get(obj)));
		}

		// parameter
		for(int i = 0, _i = function.getParaList().size(); i < _i; i++){
			VirtualRegister para = function.getParaList().get(i);
			count.put(para, 0);
			stack.put(para, new Stack<>());
			stack.get(para).push(count.get(para));
			function.getParaList().set(i, para.getSSARenamedRegister(count.get(para)));
		}

		recursiveRenaming(function.getEntryBB());
	}
	/**
	 * insert phi
	 */
	public void insertPhi(Function func){
		Queue<BasicBlock> queue = new LinkedList<>();
		Set<BasicBlock> inserted = new HashSet<>();
		for(VirtualRegister var : func.getGlobals()){
			inserted.clear();
			queue.addAll(defBBs.getOrDefault(var, Collections.emptyList()));
			while(!queue.isEmpty()){
				BasicBlock BB = queue.remove();
				for(BasicBlock BB2 : domFront.get(BB)){
					if(!inserted.contains(BB2)){
						inserted.add(BB2);
						BB2.sudoPrependInst(new Phi(var));
						queue.add(BB2);
					}
				}
			}
		}
	}
	/**
	 * traverse all globals
	 */
	Map<VirtualRegister, List<BasicBlock>> defBBs = new HashMap<>();
	public void traverseGlobals(Function func){
		Set<VirtualRegister> defined = new HashSet<>();
		for(BasicBlock BB : func.getBBList()){
			defined.clear();
			for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){

				List<VirtualRegister> useRegisters = ins.getUseRegister();
				VirtualRegister defRegister = ins.getDefRegister();

				for(VirtualRegister useRegister : useRegisters){
					if(!useRegister.isGlobal() && !defined.contains(useRegister))
						func.addGlobals(useRegister);
				}
				if(defRegister != null) {
					defined.add(defRegister);
					if(!defBBs.containsKey(defRegister)) defBBs.put(defRegister, new ArrayList<>());
					defBBs.get(defRegister).add(BB);
				}
			}
		}
	}
	/**
	 * dominance frontier computation
	 */
	Map<BasicBlock, Set<BasicBlock>> domFront = new HashMap<>();
	public void constructDominanceFrontier(Function func){
		List<BasicBlock> BBList = func.getBBList();
		BBList.forEach(BB -> domFront.put(BB, new HashSet<>()));
		for(BasicBlock BB : BBList){
			// todo : why to check predecessor >= 2?
			for(BasicBlock predecessor : BB.getPreBBList()){
				BasicBlock x = predecessor;
				while(x != iDom.get(BB)){
					domFront.get(x).add(BB);
					x = iDom.get(x);
				}
			}
		}
	}
	/**
	 * Dominator tree
	 */
	Map<BasicBlock, Integer> 	dfn = new HashMap<>();
	Map<Integer, BasicBlock>	vertex = new HashMap<>();
	Map<BasicBlock, List<BasicBlock>> bucket = new HashMap<>();

	Map<BasicBlock, BasicBlock> ancestor = new HashMap<>();
	Map<BasicBlock, BasicBlock> best = new HashMap<>();

	Map<BasicBlock, BasicBlock>	semiDom = new HashMap<>();
	Map<BasicBlock, BasicBlock> sameDom = new HashMap<>();
	Map<BasicBlock, BasicBlock> iDom = new HashMap<>(); // i.e. parent in dominator tree
	Map<BasicBlock, List<BasicBlock>> iDomChildren = new HashMap<>();

	private BasicBlock ancestorWithLowestSemi(BasicBlock v){
		BasicBlock a = ancestor.get(v);
		if(ancestor.get(a) != null){
			BasicBlock b = ancestorWithLowestSemi(a);
			ancestor.put(v, ancestor.get(a));
			if(dfn.get(semiDom.get(b)) < dfn.get(semiDom.get(best.get(v))))
				best.put(v, b);
		}
		return best.get(v);
	}

	private void link(BasicBlock p, BasicBlock n){
		ancestor.put(n, p);
		best.put(n, n);
	}

	private void constructDominatorTree(Function function)
	{
		// fake dfs
		function.makeBBList();
		List<BasicBlock> preOrderBBList = function.getBBList();

		dfn.clear();
		vertex.clear();
		for(int i = 0; i < preOrderBBList.size(); i++) {
			BasicBlock n = preOrderBBList.get(i);
			dfn.put(n, i + 1);
			vertex.put(i + 1, n);
		}

		// init
		preOrderBBList.forEach(BB -> {
			bucket.put(BB, new ArrayList<>());
			semiDom.put(BB, null);
			ancestor.put(BB, null);
			iDom.put(BB, null);
			sameDom.put(BB, null);
		});
		for(int i = preOrderBBList.size() - 1; i >= 1; i--){
			BasicBlock n = preOrderBBList.get(i);
			BasicBlock p = n.getParent();
			BasicBlock s = p;
			for(BasicBlock v : n.getPreBBList()){
				BasicBlock ss = dfn.get(v) <= dfn.get(n)
						? v
						: semiDom.get(ancestorWithLowestSemi(v));
				if(dfn.get(ss) < dfn.get(s))
					s = ss;
			}
			semiDom.put(n, s);
			bucket.get(s).add(n);
			link(p, n);
			for(BasicBlock v : bucket.get(p)){
				BasicBlock y = ancestorWithLowestSemi(v);
				if(semiDom.get(y) == semiDom.get(v)) iDom.put(v, p);
				else sameDom.put(v, y);
			}
			bucket.get(p).clear();
		}
		for(int i = 1; i < preOrderBBList.size(); i++){
			BasicBlock n = preOrderBBList.get(i);
			if(sameDom.get(n) != null)
				iDom.put(n, iDom.get(sameDom.get(n)));
		}

		for(BasicBlock BB : preOrderBBList) iDomChildren.put(BB, new ArrayList<>());
		for(BasicBlock BB : preOrderBBList) if(iDom.get(BB) != null){
			iDomChildren.get(iDom.get(BB)).add(BB);
		}
	}

}
