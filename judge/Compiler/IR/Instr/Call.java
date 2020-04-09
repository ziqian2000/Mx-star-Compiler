package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.Operand.I32Pointer;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Call extends IRIns {

	// to call function [function] with parameter [paraList], and store the result in [dst]
	// if [function] is a class member function, then [objPtr] will be the object itself

	private Function function;
	private Operand obj;
	private Operand dst;
	private List<Operand> paraList;

	public Call(Function function, Operand obj, List<Operand> paraList, Operand dst){
		this.function = function;
		this.obj = obj;
		this.paraList = paraList;
		this.dst = dst;
	}

	public Function getFunction() {
		return function;
	}

	public Operand getObj() {
		return obj;
	}

	public List<Operand> getParaList() {
		return paraList;
	}

	public Operand getDst() {
		return dst;
	}

	@Override
	public List<Register> getUseRegister() {
		List<Register> registerList = new ArrayList<>();
		if(obj instanceof Register) registerList.add((Register)obj);
		paraList.forEach(opr -> {
			if(opr instanceof Register) registerList.add((Register) opr);
		});
		return registerList;
	}

	@Override
	public Register getDefRegister() {
		return dst instanceof Register ? (Register) dst : null;
	}@Override

	public void setDefRegister(Register newDefRegister) {
		dst = newDefRegister;
	}

	@Override
	public List<Operand> getOperands() {
		List<Operand> oprList = new ArrayList<>(Arrays.asList(obj, dst));
		oprList.addAll(paraList);
		return oprList;
	}

	@Override
	public List<BasicBlock> getBBs() {
		return Collections.emptyList();
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Call(function, opr.get(0), paraList.isEmpty() ? Collections.emptyList() : opr.subList(2, opr.size()), opr.get(1));
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
