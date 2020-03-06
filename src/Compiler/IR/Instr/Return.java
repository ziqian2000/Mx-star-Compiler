package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;

public class Return extends IR{

	// to return a value

	private Operand retValue;

	public Return(Operand retValue){
		this.retValue = retValue;
	}

}
