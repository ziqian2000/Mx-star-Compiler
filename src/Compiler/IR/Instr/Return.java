package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Return extends IRIns {

	// to return a value

	private Operand retValue;

	public Return(Operand retValue){
		this.retValue = retValue;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

	@Override
	public List<Register> getUseRegister() {
		return retValue instanceof Register ? Collections.singletonList((Register)retValue) : Collections.emptyList();
	}

	@Override
	public Register getDefRegister() {
		return null;
	}

	@Override
	public void setDefRegister(Register newDefRegister) {
		throw new FuckingException("no def register in this instruction");
	}

	@Override
	public List<Operand> getOperands() {
		return Arrays.asList(retValue);
	}

	@Override
	public List<BasicBlock> getBBs() {
		return Collections.emptyList();
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Return(opr.get(0));
	}

	public Operand getRetValue() {
		return retValue;
	}
}
