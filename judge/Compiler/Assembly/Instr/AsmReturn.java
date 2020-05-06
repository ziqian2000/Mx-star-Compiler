package Compiler.Assembly.Instr;

import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;

import java.util.Collections;
import java.util.List;

public class AsmReturn extends AsmIns {

	public AsmReturn(){

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

	@Override
	public void accept(AsmVisitor asmVisitor) {
		asmVisitor.visit(this);
	}

}
