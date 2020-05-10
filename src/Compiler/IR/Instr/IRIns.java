package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.IRVisitor;

import java.util.List;

public abstract class IRIns {

	private BasicBlock belongBB;
	private IRIns prevIns, nextIns;

	// DCE
	public boolean mark;



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

	public void prependIns(IRIns ins){
		if(prevIns != null) prevIns.setNextIns(ins);
		ins.setPrevIns(prevIns);
		prevIns = ins;
		ins.setNextIns(this);
		ins.setBelongBB(getBelongBB());
		if(getBelongBB().getHeadIns() == this) getBelongBB().setHeadIns(ins);
	}

	public void appendIns(IRIns ins){
		if(nextIns != null) nextIns.setPrevIns(ins);
		ins.setNextIns(nextIns);
		nextIns = ins;
		ins.setPrevIns(this);
		ins.setBelongBB(getBelongBB());
		if(getBelongBB().getTailIns() == this) getBelongBB().setTailIns(ins);
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

	public IRIns replaceSelfWithCopy(List<Operand> opr, List<BasicBlock> BB){
		IRIns newIns = copySelf(opr, BB);
		replaceSelfWithAnotherIns(newIns);
		return newIns;
	}

	public void replaceSelfWithAnotherIns(IRIns newIns){
		appendIns(newIns);
		removeFromList();
	}

	public abstract List<VirtualRegister> getUseRegister();

	public abstract VirtualRegister getDefRegister();

	public abstract void setDefRegister(VirtualRegister newDefRegister);

	public abstract List<Operand> getOperands();

	public abstract List<BasicBlock> getBBs();

	public abstract IRIns copySelf(List<Operand> opr, List<BasicBlock> BB);

	public abstract void accept(IRVisitor irVisitor);
}
