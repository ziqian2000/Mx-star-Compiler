package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmFunction;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AsmCall extends AsmIns {

	private AsmFunction func;

	public AsmCall(AsmFunction func){
		this.func = func;
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
