package Compiler.Assembly.Instr;

import Compiler.IR.Operand.Immediate;
import Compiler.IR.Operand.Register;

public class AsmLUI extends AsmIns {

	private Immediate imm;
	private Register rd;

	public AsmLUI(Register rd, Immediate imm){
		this.imm = imm;
		this.rd = rd;
	}

}
