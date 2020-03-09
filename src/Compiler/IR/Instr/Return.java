package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

public class Return extends IRIns {

	// to return a value

	private Operand retValue;

	public Return(Operand retValue){
		this.retValue = retValue;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

	public Operand getRetValue() {
		return retValue;
	}
}
