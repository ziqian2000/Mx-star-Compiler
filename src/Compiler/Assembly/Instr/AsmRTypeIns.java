package Compiler.Assembly.Instr;

import Compiler.IR.Operand.Register;

public class AsmRTypeIns extends AsmIns {

	public enum Op{
		ADD, SUB, MUL, DIV, REM,
		SLL, SRA,
		SLT, SLTU,
		AND, OR, XOR
	}
	private Register rs1, rs2, rd;
	private AsmRTypeIns.Op op;

	public AsmRTypeIns(Register rs1, Register rs2, Register rd, AsmRTypeIns.Op op){
		this.rs1 = rs1;
		this.rs2 = rs2;
		this.rd = rd;
		this.op = op;
	}

}
