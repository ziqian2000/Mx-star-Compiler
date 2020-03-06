package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;

public class Load extends IR{

	// to load from [ptr] and store in register [dst]

	private Operand dst;
	private Operand ptr;

	public Load(Operand dst, Operand ptr){
		this.dst = dst;
		this.ptr = ptr;
	}

}
