package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Immediate;
import Compiler.IR.Operand.Register;

import java.util.Collections;
import java.util.List;

public class AsmLI extends AsmIns {

	private Immediate imm;
	private Register rd;

	public AsmLI(Register rd, Immediate imm){
		this.imm = imm;
		this.rd = rd;
	}

	public Register getRd() {
		return rd;
	}

	public Immediate getImm() {
		return imm;
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

	@Override
	public void accept(AsmVisitor asmVisitor) {
		asmVisitor.visit(this);
	}

}
