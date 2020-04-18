package Compiler.Assembly.Instr;

import Compiler.Assembly.AsmFunction;

public class AsmCall extends AsmIns {

	private AsmFunction func;

	public AsmCall(AsmFunction func){
		this.func = func;
	}

}
