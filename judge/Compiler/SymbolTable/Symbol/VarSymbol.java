package Compiler.SymbolTable.Symbol;

import Compiler.SymbolTable.Type.Type;

public class VarSymbol extends Symbol {

	Type varType;

	public VarSymbol(String identifier, Type varType){
		super(identifier);
		this.varType = varType;
	}

}
