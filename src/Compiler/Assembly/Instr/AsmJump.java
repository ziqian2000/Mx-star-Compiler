package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.IR.Operand.Register;

import java.util.Collections;
import java.util.List;

public class AsmJump extends AsmIns {

	private AsmBasicBlock BB;

	public AsmJump(AsmBasicBlock BB){
		this.BB = BB;
	}

	public AsmBasicBlock getBB() {
		return BB;
	}

	@Override
	public Register getDefRegister() {
		return null;
	}

	@Override
	public List<Register> getUseRegister() {
		return Collections.emptyList();
	}

	@Override
	public void replaceUseRegister(Register oldReg, Register newReg){
	}

	@Override
	public void replaceDefRegister(Register newReg){
		assert false;
	}

}
