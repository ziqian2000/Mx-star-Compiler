package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;

public class CJump extends IR{

	private Operand cond;
	private BasicBlock thenBB;
	private BasicBlock elseBB;

	public CJump(Operand cond, BasicBlock thenBB, BasicBlock elseBB){
		this.cond = cond;
		this.thenBB = thenBB;
		this.elseBB = elseBB;
	}

}
