package Compiler.SymbolTable.Symbol;

import Compiler.IR.Function;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Type.ClassType;

public class ClassSymbol extends Symbol {

	private ClassType selfType;
	private Scope bodyScope;
	private Function constructor;

	public ClassSymbol(String identifier, ClassType selfType){
		super(identifier);
		this.selfType = selfType;
	}

	public Scope getBodyScope() {
		return bodyScope;
	}

	public void setBodyScope(Scope bodyScope) {
		this.bodyScope = bodyScope;
	}

	public void setSelfType(ClassType selfType) {
		this.selfType = selfType;
	}

	public ClassType getSelfType(){
		return selfType;
	}

	public void setConstructor(Function constructor) {
		this.constructor = constructor;
	}

	public Function getConstructor() {
		return constructor;
	}
}
