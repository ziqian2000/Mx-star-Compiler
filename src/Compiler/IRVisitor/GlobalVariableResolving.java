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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalVariableResolving {

	IR ir;
	private List<Function> irFuncExceptBuiltin = new ArrayList<>();
	private Map<Function, List<I32Value>> globalVarInFunc = new HashMap<>();

	public GlobalVariableResolving(IR ir){
		this.ir = ir;
	}

	public void run(){
		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) irFuncExceptBuiltin.add(func);
		copyGlobalTmpVar();
		scanningFunc();

		loadBeforeEntering();
		storeAfterExiting();
		loadAndStoreWhenCalling();
	}

	public void scanningFunc(){
		for(Function function : irFuncExceptBuiltin){
			function.makeBBList();
			List<I32Value> globalVarList = new ArrayList<>();
			globalVarInFunc.put(function, globalVarList);
			for(BasicBlock BB : function.getBBList()){
				for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
					List<Operand> oprList = ins.getOperands();
					for(Operand opr : oprList)
						if(opr instanceof I32Value && ((I32Value) opr).getAssocGlobal() != null){
							if(!globalVarList.contains(opr)) globalVarList.add((I32Value)opr);
						}
				}
			}
		}
	}

	public void loadBeforeEntering(){
		for(Function function : irFuncExceptBuiltin){
			IRIns targetPosIns = function.getEntryBB().getHeadIns();
			List<I32Value> globalVarList = globalVarInFunc.get(function);
			for(I32Value globalVar : globalVarList){
				if(function.getIdentifier().equals("__init")){
					var tmp = targetPosIns;
					while(!(tmp instanceof Store) || ((Store) tmp).getPtr() != globalVar.getAssocGlobal()) tmp = tmp.getNextIns();
					tmp.appendIns(new Load(globalVar, globalVar.getAssocGlobal()));
				}
				else targetPosIns.prependIns(new Load(globalVar, globalVar.getAssocGlobal()));
			}
		}
	}

	public void storeAfterExiting(){
		for(Function function : irFuncExceptBuiltin){
			IRIns targetPosIns = function.getExitBB().getTailIns();
			List<I32Value> globalVarList = globalVarInFunc.get(function);
			for(I32Value globalVar : globalVarList){
				targetPosIns.prependIns(new Store(globalVar, globalVar.getAssocGlobal()));
			}
		}
	}

	public void loadAndStoreWhenCalling(){
		for(Function function : irFuncExceptBuiltin){
//			function.makeBBList();
			List<I32Value> globalVarList = globalVarInFunc.get(function);
			for(BasicBlock BB : function.getBBList()){
				for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
					if(ins instanceof Call){
						for(I32Value globalVar : globalVarList){
							ins.prependIns(new Store(globalVar, globalVar.getAssocGlobal()));
							ins.appendIns(new Load(globalVar, globalVar.getAssocGlobal()));
							// todo: maybe just store/load needed global variable? but how to deal with recursive reference?
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
			Map<Operand, Operand> tempGlobalVarMap = new HashMap<>();
			function.makeBBList();
			for(BasicBlock BB : function.getBBList()){
				for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
					List<Operand> oprList = ins.getOperands();
					List<Operand> newOprList = new ArrayList<>();
					for(Operand opr : oprList){
						if(opr instanceof VirtualRegister && ((VirtualRegister) opr).getAssocGlobal() != null) {
							if(tempGlobalVarMap.containsKey(opr)) newOprList.add(tempGlobalVarMap.get(opr));
							else{
								I32Value newOpr = new I32Value(opr.getIdentifier());
								newOpr.setAssocGlobal(((VirtualRegister) opr).getAssocGlobal());
								newOprList.add(newOpr);
								tempGlobalVarMap.put(opr,newOpr);
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
