package Compiler.Optimization;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.I32Pointer;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.Utils.FuckingException;

import java.util.*;

public class FunctionInlining {

	final private int CODE_LEN_LIMIT = 10000;

	private IR ir;

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

		removeUselessFunc();
		NonRecursiveFuncInlining();
		RecursiveFuncInlining();
	}

	private void removeUselessFunc(){
		for(Function func : irFuncExceptBuiltin)
			if(noCall(func))
				ir.getFunctionList().remove(func);
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
					&& funcLength.get(function) + funcLength.get(callee) < CODE_LEN_LIMIT) {

						funcLength.put(function, funcLength.get(function) + funcLength.get(callee));

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
		// todo, consider to inline each function at most 5 times
	}

	private void inline(Call ins, Function caller){
		Function callee = ins.getFunction();

		// split current BB in the middle, remove CALL ins
		BasicBlock firstHalfBB = ins.getBelongBB(), secondHalfBB = new BasicBlock("second_half");
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
		makeTmpOprCopy(tempStorageMap, callee.getObj());
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

		// insert those basic blocks, both first/second half basic block are terminated
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
				if(((VirtualRegister) opr).isGlobal() || ((VirtualRegister) opr).getAssocGlobal() != null)
					tempStorageMap.put(opr, opr); // global shouldn't be copied
				else if(opr instanceof I32Value) tempStorageMap.put(opr, new I32Value(opr.getIdentifier()));
				else if(opr instanceof I32Pointer) tempStorageMap.put(opr, new I32Pointer(opr.getIdentifier()));
				else throw new FuckingException("impossible type of opr! fuck you!");
			}
			else tempStorageMap.put(opr, opr); // immediate, static string
		}
	}

}