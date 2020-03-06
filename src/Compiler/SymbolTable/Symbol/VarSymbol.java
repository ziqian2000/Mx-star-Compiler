package Compiler.SymbolTable.Symbol;

import Compiler.IR.Operand.Operand;
import Compiler.SymbolTable.Type.Type;

public class VarSymbol extends Symbol {

	private Type varType;

	// IR
	private int offset;
	private Operand storage;

	public VarSymbol(String identifier, Type varType){
		super(identifier);
		this.varType = varType;
	}

	public Type getVarType() {
		return varType;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public void setStorage(Operand storage) {
		this.storage = storage;
	}

	public Operand getStorage() {
		return storage;
	}
}
