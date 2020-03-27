package Compiler.IR.Instr;

import Compiler.IR.Function;
import Compiler.IR.Operand.I32Pointer;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IRVisitor.IRVisitor;

import java.util.List;

public class Call extends IRIns {

	// to call function [function] with parameter [paraList], and store the result in [dst]
	// if [function] is a class member function, then [objPtr] will be the object itself

	private Function function;
	private Operand obj;
	private List<Operand> paraList;
	private Operand dst;

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

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
