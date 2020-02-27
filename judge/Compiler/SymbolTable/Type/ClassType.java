package Compiler.SymbolTable.Type;

import Compiler.SymbolTable.Symbol.ClassSymbol;

public class ClassType extends Type {

	private ClassSymbol selfSymbol;

	public ClassType(String identifier){
		super(identifier);
	}

	public void setSelfSymbol(ClassSymbol classSymbol) {
		this.selfSymbol = classSymbol;
	}

	public ClassSymbol getSelfSymbol() {
		return selfSymbol;
	}
}
