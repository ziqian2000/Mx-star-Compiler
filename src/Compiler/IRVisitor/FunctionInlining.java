package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.Call;
import Compiler.IR.Instr.IRIns;
import Compiler.IR.Operand.Operand;

import java.util.*;

public class FunctionInlining {

	IR ir;

	private Map<Function, Integer> funcCalledCnt = new HashMap<>();
	private Map<Function, List<Call>> funcCallInstList = new HashMap<>();

	public FunctionInlining(IR ir){
		this.ir = ir;
	}

	public void run(){
		init();
		NonRecursiveFuncInlining();
		RecursiveFuncInlining();
	}

	private void init(){
		for(Function function : ir.getFunctionList()) funcCalledCnt.put(function, 1);
		for(Function function : ir.getFunctionList()){
			function.makeBBList();
			funcCallInstList.put(function, new ArrayList<>());
			for(BasicBlock BB : function.getBBList()){
				for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
					if(ins instanceof Call){
						Function callee = ((Call) ins).getFunction();
						funcCallInstList.get(function).add((Call)ins);
						funcCalledCnt.put(callee, funcCalledCnt.get(callee) + 1);
					}
				}
			}

		}
	}

	private void NonRecursiveFuncInlining(){
		boolean inlined = true;
		while(inlined){
			inlined = false;
			for(Function function : ir.getFunctionList()) {
				for(Call callInst : funcCallInstList.get(function)){
					Function callee = callInst.getFunction();
					if(isNonRecursive(callee)) inline(callInst);
				}
			}
		}
	}

	private	void RecursiveFuncInlining(){
		// todo, consider to incline each function 5 times
	}

	private void inline(Call ins){
		Map<Operand, Operand> tempRegisterMap = new HashMap<>();
		Map<BasicBlock, BasicBlock> tempBBMap = new HashMap<>();
		// todo
	}

	// utility method

	boolean isNonRecursive(Function function){
		for (Call x : funcCallInstList.get(function)) {
			if (x.getFunction() == function) return false;
		}
		return true;
	}

	boolean isRecursive(Function function){ return !isNonRecursive(function); }

}
