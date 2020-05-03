package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.IR.BasicBlock;
import Compiler.IR.Instr.IRIns;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;

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

	public abstract List<Register> getUseRegister();

	public abstract void replaceUseRegister(Register oldReg, Register newReg);

	public abstract void replaceDefRegister(Register newReg);

	public abstract Register getDefRegister();

}
