package Compiler.Assembly.Instr;

import Compiler.IR.Operand.Immediate;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.StaticStrConst;

import java.util.Collections;
import java.util.List;

public class AsmLA extends AsmIns {

	private Register rd;
	private StaticStrConst symbol; // for string only

	public AsmLA(Register rd, StaticStrConst symbol){
		this.symbol = symbol;
		this.rd = rd;
	}


	@Override
	public Register getDefRegister() {
		return rd;
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
		rd = newReg;
	}

}
