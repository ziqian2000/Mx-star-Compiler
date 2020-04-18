package Compiler.Assembly.Instr;

import Compiler.IR.Operand.Register;
import Compiler.IR.StackLocation;

public class AsmStore extends AsmIns {

	// [ rs1 + offset (loc) ] ← rs2
	private int size;
	private StackLocation loc;
	private Register rs2;

	public AsmStore(StackLocation loc, Register rs2, int size){
		this.rs2 = rs2;
		this.loc = loc;
		this.size = size;
	}
}
