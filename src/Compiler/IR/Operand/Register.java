package Compiler.IR.Operand;

public class Register extends Storage {

	private boolean global = false;
	private Register assocGlobal;

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

	public void setAssocGlobal(Register assocGlobal) {
		this.assocGlobal = assocGlobal;
	}

	public Register getAssocGlobal() {
		return assocGlobal;
	}
}
