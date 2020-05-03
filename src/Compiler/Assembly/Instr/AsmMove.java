package Compiler.Assembly.Instr;

import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;

import java.util.Collections;
import java.util.List;

public class AsmMove extends AsmIns {

	private Register rs1;
	private Register rd;

	public AsmMove(Register rs1, Register rd){
		this.rs1 = rs1;
		this.rd = rd;
	}

	public Register getRs1() {
		return rs1;
	}

	public Register getRd() {
		return rd;
	}

	@Override
	public Register getDefRegister() {
		return rd;
	}

	@Override
	public List<Register> getUseRegister() {
		return Collections.singletonList(rs1);
	}

	@Override
	public void replaceUseRegister(Register oldReg, Register newReg){
		if(rs1 == oldReg) rs1 = newReg;
	}

	@Override
	public void replaceDefRegister(Register newReg){
		rd = newReg;
	}

}
