package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.IR.BasicBlock;
import Compiler.IR.Instr.IRIns;

public abstract class AsmIns {

	private AsmBasicBlock belongBB;
	private AsmIns prevIns, nextIns;

	public void setBelongBB(AsmBasicBlock belongBB) {
		this.belongBB = belongBB;
	}

	public AsmBasicBlock getBelongBB() {
		return belongBB;
	}

	public void setPrevIns(AsmIns prevIns) {
		this.prevIns = prevIns;
	}

	public void setNextIns(AsmIns nextIns) {
		this.nextIns = nextIns;
	}

	public AsmIns getPrevIns() {
		return prevIns;
	}

	public AsmIns getNextIns() {
		return nextIns;
	}

}
