package Compiler.IRVisitor.Optimizations;

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

public class SSAConstructor extends Pass {

	IR ir;

	public SSAConstructor(IR ir){
		this.ir = ir;
	}

	public void run(){
		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			constructDominatorTree(func, false);
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
	Map<VirtualRegister, Stack<Integer>> stack = new LinkedHashMap<>();
	Map<VirtualRegister, Integer> count = new LinkedHashMap<>();

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
				S.replaceDefRegister(defRegister.getSSARenamedRegister(i));
			}
		}
		for(BasicBlock y : n.getSucBBList()){
			for(IRIns ins = y.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(ins instanceof Phi){
					VirtualRegister origin = ((Phi) ins).getDst().getOriginVar();
					VirtualRegister renamedRegister = stack.containsKey(origin) && !stack.get(origin).isEmpty()
											? origin.getSSARenamedRegister(stack.get(origin).peek())
											: null; // means it can take an arbitrary value
					((Phi) ins).getPath().put(n, renamedRegister);
				}
			}
		}

		n.iDomChildren.forEach(this::recursiveRenaming);

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
		Set<BasicBlock> inserted = new LinkedHashSet<>();
		for(VirtualRegister var : func.getGlobals()){
			inserted.clear();
			queue.addAll(defBBs.getOrDefault(var, Collections.emptyList()));
			while(!queue.isEmpty()){
				BasicBlock BB = queue.remove();
				for(BasicBlock BB2 : BB.domFront){
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

	Map<VirtualRegister, List<BasicBlock>> defBBs = new LinkedHashMap<>();
	public void traverseGlobals(Function func){
		Set<VirtualRegister> defined = new LinkedHashSet<>();
		for(BasicBlock BB : func.getPreOrderBBList()){
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



}
