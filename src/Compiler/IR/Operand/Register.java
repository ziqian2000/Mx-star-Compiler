package Compiler.IR.Operand;

public class Register extends Storage {

	private boolean global = false;

	public Register(){}

	public Register(String identifier){
		super(identifier);
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean getGlobal() {
		return global;
	}

}
