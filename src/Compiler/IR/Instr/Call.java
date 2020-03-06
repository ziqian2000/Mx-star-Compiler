package Compiler.IR.Instr;

import Compiler.IR.Function;
import Compiler.IR.Operand.I32Pointer;
import Compiler.IR.Operand.Operand;

import java.util.ArrayList;
import java.util.List;

public class Call extends IR{

	// to call function [function] with parameter [paraList], and store the result in [dst]
	// if [function] is a class member function, then [objPtr] will be the object itself

	private Function function;
	private I32Pointer objPtr;
	private List<Operand> paraList;
	private Operand dst;

	public Call(Function function, I32Pointer objPtr, List<Operand> paraList, Operand dst){
		this.function = function;
		this.objPtr = objPtr;
		this.paraList = paraList;
		this.dst = dst;
	}
}
