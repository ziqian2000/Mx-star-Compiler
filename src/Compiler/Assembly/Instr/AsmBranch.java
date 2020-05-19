package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;

import java.util.Arrays;
import java.util.List;

public class AsmBranch extends AsmIns {

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

	public Register getRs1() {
		return rs1;
	}

	public Register getRs2() {
		return rs2;
	}

	public Op getOp() {
		return op;
	}

	public String getOpStr(){
		return op.toString().toLowerCase();
	}

	public AsmBasicBlock getBB() {
		return BB;
	}

	public void setBB(AsmBasicBlock BB) {
		this.BB = BB;
	}

	@Override
	public Register getDefRegister() {
		return null;
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
		assert false;
	}

	@Override
	public void accept(AsmVisitor asmVisitor) {
		asmVisitor.visit(this);
	}
}
