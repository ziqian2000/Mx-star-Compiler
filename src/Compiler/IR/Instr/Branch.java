package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

public class Branch extends IRIns {

	private Operand cond;
	private BasicBlock thenBB;
	private BasicBlock elseBB;

	public Branch(Operand cond, BasicBlock thenBB, BasicBlock elseBB){
		this.cond = cond;
		this.thenBB = thenBB;
		this.elseBB = elseBB;
	}

	public Operand getCond() {
		return cond;
	}

	public BasicBlock getThenBB() {
		return thenBB;
	}

	public BasicBlock getElseBB() {
		return elseBB;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
