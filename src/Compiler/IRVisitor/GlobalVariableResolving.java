package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.Call;
import Compiler.IR.Instr.IRIns;
import Compiler.IR.Instr.Load;
import Compiler.IR.Instr.Store;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;

import java.util.*;

public class GlobalVariableResolving {

	IR ir;
	private List<Function> irFuncExceptBuiltin = new ArrayList<>();
	private Map<Function, Set<Function>> calleeList = new LinkedHashMap<>();

	private Map<Function, Set<VirtualRegister>> varUsedInFunc = new LinkedHashMap<>();
	private Map<Function, Set<VirtualRegister>> varRecurUsedInFunc = new LinkedHashMap<>();
	private Map<Function, Map<Operand, Operand>> tempGlobalVarMap = new LinkedHashMap<>(); // global variables -> local temporary variables

	public GlobalVariableResolving(IR ir){
		this.ir = ir;
	}

	public void run(){
		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) irFuncExceptBuiltin.add(func);
		copyGlobalTmpVar();

		computeGlobalVarUsed();

		loadBeforeEntering();
		storeAfterExiting();
		loadAndStoreWhenCalling();
	}

	public void computeGlobalVarUsed(){
		for(var function : irFuncExceptBuiltin){
			function.makePreOrderBBList();

			Set<VirtualRegister> globalVarUsed = new LinkedHashSet<>();
			Set<Function> calleeFunction = new LinkedHashSet<>();

			for(BasicBlock BB : function.getPreOrderBBList()){
				for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){

					if(ins instanceof Call && !((Call) ins).getFunction().getIsBuiltin())
						calleeFunction.add(((Call) ins).getFunction());

					List<Operand> oprList = ins.getOperands();
					for(Operand opr : oprList)
						if(opr instanceof I32Value && ((I32Value) opr).getAssocGlobal() != null){
							globalVarUsed.add(((I32Value) opr).getAssocGlobal());
						}

				}
			}
			calleeList.put(function, calleeFunction);
			varUsedInFunc.put(function, globalVarUsed);
			varRecurUsedInFunc.put(function, new LinkedHashSet<>(globalVarUsed));
		}

		boolean changed = true;
		while(changed) {
			changed = false;
			for (var function : irFuncExceptBuiltin) {
				for(var callee : calleeList.get(function)){
					if(!varRecurUsedInFunc.get(function).containsAll(varRecurUsedInFunc.get(callee))){
						changed = true;
						varRecurUsedInFunc.get(function).addAll(varRecurUsedInFunc.get(callee));
					}
				}
			}
		}

	}

	public void loadBeforeEntering(){
		for(Function function : irFuncExceptBuiltin){
			IRIns targetPosIns = function.getEntryBB().getHeadIns();

			Set<VirtualRegister> globalVarSet = varUsedInFunc.get(function);
			var tempVarMap = tempGlobalVarMap.get(function);


			for(var globalVar : globalVarSet){
				assert tempVarMap.containsKey(globalVar);

				if(function.getIdentifier().equals("__init")){
					var tmp = targetPosIns;
					while(!(tmp instanceof Store) || ((Store) tmp).getPtr() != globalVar) tmp = tmp.getNextIns();
					tmp.appendIns(new Load(tempVarMap.get(globalVar), globalVar));
				}
				else targetPosIns.prependIns(new Load(tempVarMap.get(globalVar), globalVar));
			}
		}
	}

	public void storeAfterExiting(){
		for(Function function : irFuncExceptBuiltin){
			IRIns targetPosIns = function.getExitBB().getTailIns();

			Set<VirtualRegister> globalVarSet = varUsedInFunc.get(function);
			var tempVarMap = tempGlobalVarMap.get(function);

			for(var globalVar : globalVarSet){
				assert tempVarMap.containsKey(globalVar);

				targetPosIns.prependIns(new Store(tempVarMap.get(globalVar), globalVar));
			}
		}
	}

	public void loadAndStoreWhenCalling(){
		for(Function function : irFuncExceptBuiltin) if(!function.getIdentifier().equals("__init")) {
//			function.makeBBList();
			var globalVarUsed = varUsedInFunc.get(function);
			for(BasicBlock BB : function.getPreOrderBBList()){
				for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
					if(ins instanceof Call && !((Call) ins).getFunction().getIsBuiltin()){

						var globalVarCalleeUsed = varRecurUsedInFunc.get(((Call) ins).getFunction());
						var tempVarMap = tempGlobalVarMap.get(function);

						for(var globalVar : globalVarCalleeUsed)
							if(globalVarUsed.contains(globalVar)) {
								assert tempVarMap.containsKey(globalVar);

								ins.prependIns(new Store(tempVarMap.get(globalVar), globalVar));
								ins.appendIns(new Load(tempVarMap.get(globalVar), globalVar));
						}
					}
				}
			}
		}
	}

	// different functions use the same temporal local variable for some global variable, which may cause potential problems
	// so make a new copy for each temporal local variable
	public void copyGlobalTmpVar(){
		for(Function function : irFuncExceptBuiltin){

			Map<Operand, Operand> oprReplacement = new LinkedHashMap<>();
			Map<Operand, Operand> tempVarMap = new LinkedHashMap<>();
			tempGlobalVarMap.put(function, tempVarMap);

			function.makePreOrderBBList();
			for(BasicBlock BB : function.getPreOrderBBList()){
				for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
					List<Operand> oprList = ins.getOperands();
					List<Operand> newOprList = new ArrayList<>();
					for(Operand opr : oprList){
						if(opr instanceof VirtualRegister && ((VirtualRegister) opr).getAssocGlobal() != null) {
							if(oprReplacement.containsKey(opr)) newOprList.add(oprReplacement.get(opr));
							else{
								I32Value newOpr = new I32Value(opr.getIdentifier());
								newOpr.setAssocGlobal(((VirtualRegister) opr).getAssocGlobal());
								newOprList.add(newOpr);
								oprReplacement.put(opr,newOpr);
								tempVarMap.put(((VirtualRegister) opr).getAssocGlobal(), newOpr);
							}
						}
						else newOprList.add(opr);
					}
					IRIns newIns = ins.copySelf(newOprList, ins.getBBs());
					ins.prependIns(newIns);
					ins.removeFromList();
				}
			}
		}
	}

}
