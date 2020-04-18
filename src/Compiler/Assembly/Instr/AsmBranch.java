package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.IR.Operand.Register;

public class AsmBranch extends AsmIns {

	// todo : merge cmp into branch

	Register rs1, rs2;
	AsmBasicBlock BB;
	Op op;

	public enum Op{
		BEQ, BNE, BLE, BGE, BLT, BGT
	}

	public AsmBranch(Register rs1, Register rs2, Op op, AsmBasicBlock BB){
		this.rs1 = rs1;
		this.rs2 = rs2;
		this.BB = BB;
		this.op = op;
	}

}
