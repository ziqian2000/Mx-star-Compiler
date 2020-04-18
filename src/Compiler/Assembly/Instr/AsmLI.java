package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.IR.Operand.Immediate;
import Compiler.IR.Operand.Register;

public class AsmLI extends AsmIns {

	private Immediate imm;
	private Register rd;

	public AsmLI(Register rd, Immediate imm){
		this.imm = imm;
		this.rd = rd;
	}

}
