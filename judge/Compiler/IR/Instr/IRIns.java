package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IRVisitor.IRVisitor;

public abstract class IRIns {

	private BasicBlock belongBB;
	private IRIns prevIRIns, nextIRIns;

	public void setBelongBB(BasicBlock belongBB) {
		this.belongBB = belongBB;
	}

	public abstract void accept(IRVisitor irVisitor);
}
