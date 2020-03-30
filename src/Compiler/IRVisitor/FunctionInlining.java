package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.I32Pointer;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.Utils.FuckingException;

import java.util.*;

public class FunctionInlining {

	IR ir;

	private List<Function> irFuncExceptBuiltin = new ArrayList<>();
	private Set<Function> changedFunc = new LinkedHashSet<>();
	private Map<Function, List<Call>> funcCallInstList = new HashMap<>();

	public FunctionInlining(IR ir){
		this.ir = ir;
	}

	public void run(){
		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) irFuncExceptBuiltin.add(func);
		changedFunc.addAll(irFuncExceptBuiltin);

		removeUselessFunc();
		NonRecursiveFuncInlining(); // todo : consider to add a limitation on the length of function to avoid huge code
		RecursiveFuncInlining();


	}

	private void removeUselessFunc(){
		for(Function func : irFuncExceptBuiltin)
			if(noAccess(func))
				ir.getFunctionList().remove(func);
	}

	private void findCallInsInsideChangedFunc(){
		for(Function function : changedFunc){
			function.makeBBList();
			funcCallInstList.put(function, new ArrayList<>());
			for(BasicBlock BB : function.getBBList()){
				for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
					if(ins instanceof Call){
						funcCallInstList.get(function).add((Call)ins);
					}
				}
			}
		}
		changedFunc.clear();
	}

	private void NonRecursiveFuncInlining(){

		boolean inlined = true;
		while(inlined){
			inlined = false;

			for(Function function : irFuncExceptBuiltin) {
				if(!ir.getFunctionList().contains(function)) continue; // has been removed

				findCallInsInsideChangedFunc();

				for(Call callInst : funcCallInstList.get(function)){
					Function callee = callInst.getFunction();
					if(callee.getIsBuiltin()) continue;

					if(isNonRecursive(callee) && !function.getIdentifier().equals("__init")) {
						inline(callInst, function);
						inlined = true;
						changedFunc.add(function);

						if(noAccess(callee)) {
							ir.getFunctionList().remove(callee);
							changedFunc.remove(callee);
						}

					}
				}
			}

		}
	}

	private	void RecursiveFuncInlining(){
		// todo, consider to inline each function 5 times
	}

	private void inline(Call ins, Function caller){
		Function callee = ins.getFunction();

		// split current BB in the middle, remove CALL ins
		BasicBlock firstHalfBB = ins.getBelongBB(), secondHalfBB = new BasicBlock("second_half");
		if(caller.getExitBB() == firstHalfBB) caller.setExitBB(secondHalfBB);
		for(IRIns tmpIns = ins.getNextIns(), nextIns; tmpIns != null; tmpIns = nextIns){
			nextIns = tmpIns.getNextIns(); // as it will be changed
			tmpIns.removeFromList();
			secondHalfBB.addInst(tmpIns);
		}
		secondHalfBB.setTerminated(true);
		ins.setNextIns(null);
		ins.removeFromList();

		// make temporary opr & BB
		Map<Operand, Operand> tempStorageMap = new HashMap<>();
		Map<BasicBlock, BasicBlock> tempBBMap = new HashMap<>();
		// opr as parameter/obj
		callee.getParaList().forEach(opr -> makeTmpOprCopy(tempStorageMap, opr));
		makeTmpOprCopy(tempStorageMap, callee.getObj());
		// opr inside instruction
		for(BasicBlock BB : callee.getBBList()){
			// copy basic block
			tempBBMap.put(BB, new BasicBlock(BB.getIdentifier()));
			for(IRIns originIns = BB.getHeadIns(); originIns != null; originIns = originIns.getNextIns()){
				List<Operand> originOpr = originIns.fetchOpr();
				// copy register
				originOpr.forEach(opr -> makeTmpOprCopy(tempStorageMap, opr));
			}
		}

		// pass parameters
		for(int i = 0; i < ins.getParaList().size(); i++){
			Operand from = ins.getParaList().get(i);
			Operand to = tempStorageMap.get(callee.getParaList().get(i));
			firstHalfBB.sudoAddInst(new Move(from, to));
		}
		if(ins.getObj() != null) firstHalfBB.sudoAddInst(new Move(ins.getObj(), tempStorageMap.get(callee.getObj())));

		// copy the whole function
		for(BasicBlock BB : callee.getBBList()){
			// copy the whole basic block
			BasicBlock tmpBB = tempBBMap.get(BB);
			for(IRIns originIns = BB.getHeadIns(); originIns != null; originIns = originIns.getNextIns()){

				// replacement
				List<Operand> tmpOprList = new ArrayList<>(), originOprList = originIns.fetchOpr();
				for (Operand operand : originOprList) {
					tmpOprList.add(tempStorageMap.get(operand));
				}
				List<BasicBlock> tmpBBList = new ArrayList<>(), originBBList = originIns.fetchBB();
				for (BasicBlock basicBlock : originBBList) {
					tmpBBList.add(tempBBMap.get(basicBlock));
				}

				IRIns tmpIns = originIns.copySelf(tmpOprList, tmpBBList);
				tmpBB.addInst(tmpIns);
			}
			tmpBB.setTerminated(true);
		}

		// replace the final RETURN with MOVE
		BasicBlock entryBB = tempBBMap.get(callee.getEntryBB()), exitBB = tempBBMap.get(callee.getExitBB());
		Return retIns = (Return)exitBB.getTailIns();
		retIns.removeFromList();
		if(retIns.getRetValue() != null && ins.getDst() != null){
			exitBB.sudoAddInst(new Move(retIns.getRetValue(), ins.getDst()));
		}

		// insert those basic blocks, both first/second half basic block are terminated
		firstHalfBB.sudoAddInst(new Jump(entryBB));
		exitBB.sudoAddInst(new Jump(secondHalfBB));

	}

	// utility method

	boolean noAccess(Function function){
		if(function.getIdentifier().equals("__init")) return false;
		for(Function func : irFuncExceptBuiltin) {
			if (!ir.getFunctionList().contains(func)) continue; // has been removed

			if (func != function) {

				if (changedFunc.contains(func)) {
					findCallInsInsideChangedFunc();
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
			if(opr instanceof Register){
				if(((Register) opr).getGlobal() || ((Register) opr).getAssocGlobal() != null)
					tempStorageMap.put(opr, opr); // global shouldn't be copied
				else if(opr instanceof I32Value) tempStorageMap.put(opr, new I32Value(opr.getIdentifier()));
				else if(opr instanceof I32Pointer) tempStorageMap.put(opr, new I32Pointer(opr.getIdentifier()));
				else throw new FuckingException("impossible type of opr! fuck you!");
			}
			else tempStorageMap.put(opr, opr); // immediate, static string
		}
	}

}
