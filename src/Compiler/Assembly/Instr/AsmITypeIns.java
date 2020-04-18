package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmFunction;
import Compiler.IR.Operand.Immediate;
import Compiler.IR.Operand.Register;

public class AsmITypeIns extends AsmIns {

	public enum Op{
		ADDI, XORI, ORI, ANDI,
		SLTI, SLTIU,
		SLLI, SRAI
	}

	private Register rs1, rd;
	private Immediate imm;
	private Op op;

	public AsmITypeIns(Register rs1, Immediate imm, Register rd, Op op){
		this.rs1 = rs1;
		this.rd = rd;
		this.imm = imm;
		this.op = op;
	}

}
