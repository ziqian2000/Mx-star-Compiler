package Compiler.Assembly.Instr;

import Compiler.IR.Operand.Register;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	@Override
	public Register getDefRegister() {
		return rd;
	}

	@Override
	public List<Register> getUseRegister() {
		return Arrays.asList(rs1, rs2);
	}

	@Override
	public void replaceUseRegister(Register oldReg, Register newReg){
		if(rs1 == oldReg) rs1 = newReg;
		if(rs2 == oldReg) rs2 = newReg;
	}

	@Override
	public void replaceDefRegister(Register newReg){
		rd = newReg;
	}

}
