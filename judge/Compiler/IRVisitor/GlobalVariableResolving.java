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
import Compiler.IR.Operand.Register;

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
					List<Operand> oprList = ins.fetchOpr();
					for(Operand opr : oprList)
						if(opr instanceof I32Value) if(((I32Value) opr).getAssocGlobal() != null){
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
				targetPosIns.prependIns(new Load(globalVar, globalVar.getAssocGlobal()));
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
						}
					}
				}
			}
		}
	}

}
