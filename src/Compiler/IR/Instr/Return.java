package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.FuckingException;

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
	public List<VirtualRegister> getUseRegister() {
		return retValue instanceof VirtualRegister ? Collections.singletonList((VirtualRegister)retValue) : Collections.emptyList();
	}

	@Override
	public VirtualRegister getDefRegister() {
		return null;
	}

	@Override
	public void replaceUseOpr(Operand oldOpr, Operand newOpr) {
		if(retValue == oldOpr) retValue = newOpr;
	}

	@Override
	public void replaceDefRegister(VirtualRegister newDefRegister) {
		throw new FuckingException("no def register in this instruction");
	}

	@Override
	public List<Operand> getOperands() {
		return Collections.singletonList(retValue);
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
