package Compiler.IR.Operand;

import java.util.HashMap;
import java.util.Map;

public abstract class Register extends Storage {

	private boolean global = false;
	private Register assocGlobal;
	private Register originVar = this; // the origin register before SSA renaming

	// SSA
	private Map<Integer, Register> SSARenamedRegisters;

	public Register(){}

	public Register(String identifier){
		super(identifier);
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setAssocGlobal(Register assocGlobal) {
		this.assocGlobal = assocGlobal;
	}

	public Register getAssocGlobal() {
		return assocGlobal;
	}

	// SSA
	public abstract Register getSSACopyWithID(int id);
	public Register getSSARenamedRegister(int id){
		if(SSARenamedRegisters == null) SSARenamedRegisters = new HashMap<>();
		if(SSARenamedRegisters.containsKey(id)) return SSARenamedRegisters.get(id);
		Register newRegister = getSSACopyWithID(id);
		SSARenamedRegisters.put(id, newRegister);
		newRegister.setOriginVar(this);
		newRegister.setIdentifier(newRegister.getIdentifier() + "." + id);
		return newRegister;
	}

	public void setOriginVar(Register originVar) {
		this.originVar = originVar;
	}

	public Register getOriginVar() {
		return originVar;
	}
}
