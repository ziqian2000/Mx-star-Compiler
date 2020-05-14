package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.FuckingException;

import java.util.Collections;
import java.util.List;

public class Jump extends IRIns {

	private BasicBlock BB;

	public Jump(BasicBlock BB){
		this.BB = BB;
	}

	public BasicBlock getBB() {
		return BB;
	}

	@Override
	public List<VirtualRegister> getUseRegister() {
		return Collections.emptyList();
	}

	@Override
	public VirtualRegister getDefRegister() {
		return null;
	}

	@Override
	public void replaceUseOpr(Operand oldOpr, Operand newOpr) {

	}

	@Override
	public void replaceDefRegister(VirtualRegister newDefRegister) {
		throw new FuckingException("no def register in this instruction");
	}

	@Override
	public List<Operand> getOperands() {
		return Collections.emptyList();
	}

	@Override
	public List<BasicBlock> getBBs() {
		return Collections.singletonList(BB);
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Jump(BB.get(0));
	}

	public void replaceTargetBB(BasicBlock newBB){
		BB = newBB;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
