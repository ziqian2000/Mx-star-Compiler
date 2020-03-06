package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;

public class New extends IR{

	// to allocate [size] memory to pointer [ptr]

	private Operand size;
	private Operand ptr;

	public New(Operand size, Operand ptr){
		this.size = size;
		this.ptr = ptr;
	}

}
