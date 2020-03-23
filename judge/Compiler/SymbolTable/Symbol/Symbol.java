package Compiler.SymbolTable.Symbol;

import Compiler.SymbolTable.Scope;

public abstract class Symbol {

	private String identifier;
	private Scope scope;

	public Symbol(String identifier){
		this.identifier = identifier;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Scope getScope() {
		return scope;
	}

	public String getIdentifier() {
		return identifier;
	}
}
