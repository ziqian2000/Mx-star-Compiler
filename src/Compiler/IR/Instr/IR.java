package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;

public abstract class IR {

	private BasicBlock belongBB;
	private IR prevIR, nextIR;

	public void setBelongBB(BasicBlock belongBB) {
		this.belongBB = belongBB;
	}
}
