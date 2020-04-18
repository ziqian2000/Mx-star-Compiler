package Compiler.Assembly.Instr;

import Compiler.IR.Operand.Register;
import Compiler.IR.StackLocation;
import Compiler.IR.StackPointerOffset;

public class AsmLoad extends AsmIns {

	private int size;
	private StackLocation loc;
	private Register rd;

	public AsmLoad(StackLocation loc, Register rd, int size){
		this.rd = rd;
		this.loc = loc;
		this.size = size;
	}

}
