package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IRVisitor.IRVisitor;

public class Jump extends IRIns {

	private BasicBlock BB;

	public Jump(BasicBlock BB){
		this.BB = BB;
	}

	public BasicBlock getBB() {
		return BB;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
