package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;

public class Store extends IR{

	// to store something in register [src] to position [ptr]

	private Operand src;
	private Operand ptr;

	public Store(Operand src, Operand ptr){
		this.src = src;
		this.ptr = ptr;
	}

}
