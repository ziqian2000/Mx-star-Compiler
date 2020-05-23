package Compiler.Optimization;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.*;
import Compiler.Utils.FuckingException;

import java.util.*;

public class FunctionInlining {

	final private int OVERALL_CODE_LEN_LIMIT = 4000; // huge code leads to TLE in register allocation; note that this is not precise, the actual overall len will be bigger than it
	final private int FUNC_CODE_LEN_LIMIT = 1500; // however, this is precise
	final private int RECURSIVELY_INLINE_DEPTH = 10;

	private IR ir;

	private int approxOverallCodeLen;

	private List<Function> irFuncExceptBuiltin = new ArrayList<>();
	private Set<Function> changedFunc = new LinkedHashSet<>();
	private Map<Function, List<Call>> funcCallInstList = new LinkedHashMap<>();
	private Map<Function, Integer> funcLength = new LinkedHashMap<>();

	public FunctionInlining(IR ir){
		this.ir = ir;
	}

	public void run(){
		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) irFuncExceptBuiltin.add(func);
		changedFunc.addAll(irFuncExceptBuiltin);
		calculateOriginCodeLen();

		removeUselessFunc();
		NonRecursiveFuncInlining();
		RecursiveFuncInlining();
	}

	private void calculateOriginCodeLen(){
		processChangedFunction();
		approxOverallCodeLen = 0;
		for(var func : ir.getFunctionList()) if(!func.getIsBuiltin())
			approxOverallCodeLen += funcLength.get(func);
	}

	private void removeUselessFunc(){
		for(Function func : irFuncExceptBuiltin)
			if(noCall(func)) {
				ir.getFunctionList().remove(func);
				approxOverallCodeLen -= funcLength.get(func);
			}
	}

	private void processChangedFunction(){
		// find CALL ins & compute the length
		for(Function function : changedFunc){
			function.makePreOrderBBList();

			funcCallInstList.put(function, new ArrayList<>());
			int len = 0;

			for(BasicBlock BB : function.getPreOrderBBList()){
				for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){

					len += 1;
					if(ins instanceof Call){
						funcCallInstList.get(function).add((Call)ins);
					}
				}
			}
			funcLength.put(function, len);

		}

		changedFunc.clear();
	}

	private void NonRecursiveFuncInlining(){

		boolean inlined = true;
		while(inlined){
			inlined = false;

			for(Function function : irFuncExceptBuiltin) {
				if(!ir.getFunctionList().contains(function)) continue; // has been removed

				processChangedFunction();

				for(Call callInst : funcCallInstList.get(function)){
					Function callee = callInst.getFunction();
					if(callee.getIsBuiltin()) continue;

					if(isNonRecursive(callee) && !function.getIdentifier().equals("__init")
					&& funcLength.get(function) + funcLength.get(callee) < FUNC_CODE_LEN_LIMIT
					&& funcLength.get(callee) + approxOverallCodeLen < OVERALL_CODE_LEN_LIMIT) {

						funcLength.put(function, funcLength.get(function) + funcLength.get(callee));
						approxOverallCodeLen += funcLength.get(callee);

						inline(callInst, function);
						inlined = true;
						changedFunc.add(function);

						if(noCall(callee)) {
							ir.getFunctionList().remove(callee);
							changedFunc.remove(callee);
						}

					}
				}
			}

		}
	}

	private	void RecursiveFuncInlining(){
		boolean inlined = true;
		int inline_times = 0;
		while(inlined && inline_times < RECURSIVELY_INLINE_DEPTH){
			inlined = false;
			inline_times += 1;

			for(Function function : irFuncExceptBuiltin) {
				if(!ir.getFunctionList().contains(function)) continue; // has been removed

				processChangedFunction();

				for(Call callInst : funcCallInstList.get(function)){
					Function callee = callInst.getFunction();
					if(callee.getIsBuiltin()) continue;

					if(!function.getIdentifier().equals("__init")
					&& funcLength.get(function) + funcLength.get(callee) < FUNC_CODE_LEN_LIMIT
					&& funcLength.get(callee) + approxOverallCodeLen < OVERALL_CODE_LEN_LIMIT) {

						assert isRecursive(callee);

						funcLength.put(function, funcLength.get(function) + funcLength.get(callee));
						approxOverallCodeLen += funcLength.get(callee);

						inline(callInst, function);
						inlined = true;
						changedFunc.add(function);
					}
				}
			}

		}
	}

	private void inline(Call ins, Function caller){
		Function callee = ins.getFunction();

		if(callee == caller){
			callee = makeCompleteFuncCopy(callee);
		}

		// split current BB in the middle, remove CALL ins
		BasicBlock firstHalfBB = ins.getBelongBB(), secondHalfBB = new BasicBlock("second_half_" + ins.getFunction().getIdentifier());
		if(caller.getExitBB() == firstHalfBB) caller.setExitBB(secondHalfBB);
		for(IRIns tmpIns = ins.getNextIns(), nextIns; tmpIns != null; tmpIns = nextIns){
			nextIns = tmpIns.getNextIns(); // as it will be changed
			tmpIns.removeFromList();
			secondHalfBB.appendInst(tmpIns);
		}
		secondHalfBB.setTerminated(true);
		ins.setNextIns(null);
		ins.removeFromList();

		// make temporary opr & BB
		Map<Operand, Operand> tempStorageMap = new LinkedHashMap<>();
		Map<BasicBlock, BasicBlock> tempBBMap = new LinkedHashMap<>();
		// opr as parameter/obj
		callee.getParaList().forEach(opr -> makeTmpOprCopy(tempStorageMap, opr));
		if(callee.getObj() != null) makeTmpOprCopy(tempStorageMap, callee.getObj());
		// opr inside instruction
		for(BasicBlock BB : callee.getPreOrderBBList()){
			// copy basic block
			tempBBMap.put(BB, new BasicBlock(BB.getIdentifier()));
			for(IRIns originIns = BB.getHeadIns(); originIns != null; originIns = originIns.getNextIns()){
				List<Operand> originOpr = originIns.getOperands();
				// copy register
				originOpr.forEach(opr -> makeTmpOprCopy(tempStorageMap, opr));
			}
		}

		// pass parameters
		for(int i = 0; i < ins.getParaList().size(); i++){
			Operand from = ins.getParaList().get(i);
			Operand to = tempStorageMap.get(callee.getParaList().get(i));
			firstHalfBB.sudoAppendInst(new Move(from, to));
		}
		if(ins.getObj() != null) firstHalfBB.sudoAppendInst(new Move(ins.getObj(), tempStorageMap.get(callee.getObj())));

		// copy the whole function
		for(BasicBlock BB : callee.getPreOrderBBList()){
			// copy the whole basic block
			BasicBlock tmpBB = tempBBMap.get(BB);
			for(IRIns originIns = BB.getHeadIns(); originIns != null; originIns = originIns.getNextIns()){

				// replacement
				List<Operand> tmpOprList = new ArrayList<>(), originOprList = originIns.getOperands();
				for (Operand operand : originOprList) {
					tmpOprList.add(tempStorageMap.get(operand));
				}
				List<BasicBlock> tmpBBList = new ArrayList<>(), originBBList = originIns.getBBs();
				for (BasicBlock basicBlock : originBBList) {
					tmpBBList.add(tempBBMap.get(basicBlock));
				}

				IRIns tmpIns = originIns.copySelf(tmpOprList, tmpBBList);
				tmpBB.appendInst(tmpIns);
			}
			tmpBB.setTerminated(true);
		}

		// replace the final RETURN with MOVE
		BasicBlock entryBB = tempBBMap.get(callee.getEntryBB()), exitBB = tempBBMap.get(callee.getExitBB());
		Return retIns = (Return)exitBB.getTailIns();
		retIns.removeFromList();
		if(retIns.getRetValue() != null && ins.getDst() != null){
			exitBB.sudoAppendInst(new Move(retIns.getRetValue(), ins.getDst()));
		}

		// insert those basic blocks, both first/second half basic blocks are terminated
		firstHalfBB.sudoAppendInst(new Jump(entryBB));
		exitBB.sudoAppendInst(new Jump(secondHalfBB));

	}

	// utility method

	boolean noCall(Function function){
		// if there aren't any call to this function
		if(function.getIdentifier().equals("__init")) return false;
		for(Function func : irFuncExceptBuiltin) {
			if (!ir.getFunctionList().contains(func)) continue; // has been removed

			if (func != function) {

				if (changedFunc.contains(func)) {
					processChangedFunction();
				}

				for (Call callIns : funcCallInstList.get(func)) {
					if (callIns.getFunction() == function) return false;
				}
			}
		}
		return true;
	}

	private boolean isNonRecursive(Function function){
		for (Call x : funcCallInstList.get(function)) {
			if (x.getFunction() == function) return false;
		}
		return true;
	}

	private boolean isRecursive(Function function){ return !isNonRecursive(function); }

	private void makeTmpOprCopy(Map<Operand, Operand> tempStorageMap, Operand opr){
		if(!tempStorageMap.containsKey(opr)){
			if(opr instanceof VirtualRegister){
				if(((VirtualRegister) opr).isGlobal() || ((VirtualRegister) opr).getAssocGlobal() != null) {
					tempStorageMap.put(opr, opr); // global shouldn't be copied
				}
				else if(opr instanceof I32Value) tempStorageMap.put(opr, new I32Value(opr.getIdentifier()));
				else if(opr instanceof I32Pointer) tempStorageMap.put(opr, new I32Pointer(opr.getIdentifier()));
				else throw new FuckingException("impossible type of opr! fuck you!");
			}
			else tempStorageMap.put(opr, opr); // immediate, static string
		}
	}


	public Function makeCompleteFuncCopy(Function func){
		Map<BasicBlock, BasicBlock> tempBBMap = new LinkedHashMap<>();
		Map<Operand, Operand> tempStorageMap = new LinkedHashMap<>();

		func.makePreOrderBBList();
		func.getPreOrderBBList().forEach(BB -> tempBBMap.put(BB, new BasicBlock("copy_" + BB.getIdentifier())));

		var tmpFunc = new Function("copy_" + func.getIdentifier());

		if(func.getObj() != null){
			makeTmpOprCopy(tempStorageMap, func.getObj());
			tmpFunc.setObj((VirtualRegister) tempStorageMap.get(func.getObj()));
		}
		for(var para : func.getParaList()){
			makeTmpOprCopy(tempStorageMap, para);
			tmpFunc.getParaList().add((VirtualRegister) tempStorageMap.get(para));
		}

		for(var BB : func.getPreOrderBBList()){
			var newBB = tempBBMap.get(BB);
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){

				var oprList = ins.getOperands();
				var BBList = ins.getBBs();

				var newOprList = new ArrayList<Operand>();
				var newBBList = new ArrayList<BasicBlock>();

				for(var opr : oprList) {
					makeTmpOprCopy(tempStorageMap, opr);
					newOprList.add(tempStorageMap.get(opr));
				}
				for(var block : BBList){
					newBBList.add(tempBBMap.get(block));
				}

				var newIns = ins.copySelf(newOprList, newBBList);
				newBB.appendInst(newIns);
			}
			newBB.setTerminated(true);
			assert newBB.getHeadIns() != null;
			assert newBB.getTailIns() != null;
		}

		tmpFunc.setEntryBB(tempBBMap.get(func.getEntryBB()));
		tmpFunc.setExitBB(tempBBMap.get(func.getExitBB()));
		tmpFunc.makePreOrderBBList();

		return tmpFunc;
	}

}
