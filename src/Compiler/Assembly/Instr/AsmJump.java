package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmBasicBlock;

public class AsmJump extends AsmIns {

	private AsmBasicBlock BB;

	public AsmJump(AsmBasicBlock BB){
		this.BB = BB;
	}

}
