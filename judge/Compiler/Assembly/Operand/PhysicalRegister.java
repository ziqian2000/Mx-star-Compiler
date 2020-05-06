package Compiler.Assembly.Operand;

import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.Storage;

import java.util.HashMap;
import java.util.Map;

public class PhysicalRegister extends Register {

	public PhysicalRegister(){

	}

	public PhysicalRegister(String identifier){
		super(identifier);
	}

}
