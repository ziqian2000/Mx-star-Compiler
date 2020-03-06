package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;

public class Assign extends IR{

	// to copy value [src] into [dst], both of which are in register but not in memory

	private Operand src;
	private Operand dst;

	public Assign(Operand src, Operand dst){
		this.src = src;
		this.dst = dst;
	}

}
