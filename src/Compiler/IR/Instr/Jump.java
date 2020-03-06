package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;

public class Jump extends IR{

	private BasicBlock BB;

	public Jump(BasicBlock BB){
		this.BB = BB;
	}

}
