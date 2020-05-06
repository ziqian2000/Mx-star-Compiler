package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.Codegen.AsmVisitor;
import Compiler.IR.BasicBlock;
import Compiler.IR.Instr.IRIns;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.IRVisitor;

import java.util.List;

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

	public void prependIns(AsmIns ins){
		if(prevIns != null) prevIns.setNextIns(ins);
		ins.setPrevIns(prevIns);
		prevIns = ins;
		ins.setNextIns(this);
		ins.setBelongBB(getBelongBB());
		if(getBelongBB().getHeadIns() == this) getBelongBB().setHeadIns(ins);
	}

	public void appendIns(AsmIns ins){
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

	public abstract List<Register> getUseRegister();

	public abstract void replaceUseRegister(Register oldReg, Register newReg);

	public abstract void replaceDefRegister(Register newReg);

	public abstract Register getDefRegister();

	public abstract void accept(AsmVisitor asmVisitor);

}
