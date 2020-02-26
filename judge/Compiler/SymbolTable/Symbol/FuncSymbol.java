package Compiler.SymbolTable.Symbol;

import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Type.Type;

public class FuncSymbol extends Symbol {

	private Type retType;
	private Scope bodyScope;

	public FuncSymbol(String identifier, Type retType){
		super(identifier);
		this.retType = retType;
	}

	public void setBodyScope(Scope bodyScope) {
		this.bodyScope = bodyScope;
	}

	public void setRetType(Type retType) {
		this.retType = retType;
	}

	public Type getRetType() {
		return retType;
	}

	public Scope getBodyScope() {
		return bodyScope;
	}
}
