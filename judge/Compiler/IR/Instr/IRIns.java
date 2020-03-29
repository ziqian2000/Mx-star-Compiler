package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IRVisitor.IRVisitor;

public abstract class IRIns {

	private BasicBlock belongBB;
	private IRIns prevIns, nextIns;

	public void setBelongBB(BasicBlock belongBB) {
		this.belongBB = belongBB;
	}

	public BasicBlock getBelongBB() {
		return belongBB;
	}

	public void setPrevIns(IRIns prevIns) {
		this.prevIns = prevIns;
	}

	public void setNextIns(IRIns nextIns) {
		this.nextIns = nextIns;
	}

	public IRIns getPrevIns() {
		return prevIns;
	}

	public IRIns getNextIns() {
		return nextIns;
	}



	public void removeFromList(){
		if(getPrevIns() == null) {
			if(getNextIns() == null){
				// the only one
				getBelongBB().setHeadIns(null);
				getBelongBB().setTailIns(null);
			}
			else {
				// the head
				getBelongBB().setHeadIns(getNextIns());
				getNextIns().setPrevIns(null);
			}
		}
		else if(getNextIns() == null){
			// the tail
			getBelongBB().setTailIns(getPrevIns());
			getPrevIns().setNextIns(null);
		}
		else{
			// general case
			getNextIns().setPrevIns(getPrevIns());
			getPrevIns().setNextIns(getNextIns());
		}
	}

	public abstract void accept(IRVisitor irVisitor);
}
