package Compiler.IR.Operand;

import Compiler.IR.StackLocation;

import java.util.HashMap;
import java.util.Map;

public abstract class Register extends Storage implements StackLocation {

	public Register(){

	}

	public Register(String identifier){
		super(identifier);
	}

}
