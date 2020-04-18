package Compiler.IR.Operand;

import java.util.HashMap;
import java.util.Map;

public abstract class VirtualRegister extends Register {

	private boolean global = false;
	private VirtualRegister assocGlobal;
	private VirtualRegister originVar = this; // the origin register before SSA renaming

	// SSA
	private Map<Integer, VirtualRegister> SSARenamedRegisters;

	public VirtualRegister(){}

	public VirtualRegister(String identifier){
		super(identifier);
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setAssocGlobal(VirtualRegister assocGlobal) {
		this.assocGlobal = assocGlobal;
	}

	public VirtualRegister getAssocGlobal() {
		return assocGlobal;
	}

	// SSA
	public abstract VirtualRegister getSSACopyWithID(int id);
	public VirtualRegister getSSARenamedRegister(int id){
		if(SSARenamedRegisters == null) SSARenamedRegisters = new HashMap<>();
		if(SSARenamedRegisters.containsKey(id)) return SSARenamedRegisters.get(id);
		VirtualRegister newRegister = getSSACopyWithID(id);
		SSARenamedRegisters.put(id, newRegister);
		newRegister.setOriginVar(this);
		newRegister.setIdentifier(newRegister.getIdentifier() + "." + id);
		return newRegister;
	}

	public void setOriginVar(VirtualRegister originVar) {
		this.originVar = originVar;
	}

	public VirtualRegister getOriginVar() {
		return originVar;
	}
}
