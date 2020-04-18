package Compiler.Assembly.Instr;

import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;

public class AsmMove extends AsmIns {

	private Register rs1;
	private Register rd;

	public AsmMove(Register rs1, Register rd){
		this.rs1 = rs1;
		this.rd = rd;
	}

}
