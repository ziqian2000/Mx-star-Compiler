package Compiler.SymbolTable.Symbol;

import Compiler.IR.Function;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Type.Type;

import java.util.ArrayList;
import java.util.List;

public class FuncSymbol extends Symbol {

	private Type retType;
	private List<VarSymbol> paraSymbolList;
	private Scope bodyScope;

	// IR
	private Function function;

	public FuncSymbol(String identifier, Type retType){
		super(identifier);
		this.retType = retType;
	}

	public FuncSymbol(String identifier, Type retType, List<VarSymbol> paraSymbolList){
		super(identifier);
		this.retType = retType;
		this.paraSymbolList = paraSymbolList;
	}

	public void setBodyScope(Scope bodyScope) {
		this.bodyScope = bodyScope;
	}

	public void setRetType(Type retType) {
		this.retType = retType;
	}

	public void setParaSymbolList(List<VarSymbol> paraSymbolList) {
		this.paraSymbolList = paraSymbolList;
	}

	public Type getRetType() {
		return retType;
	}

	public Scope getBodyScope() {
		return bodyScope;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public Function getFunction() {
		return function;
	}

	public List<VarSymbol> getParaSymbolList() {
		return paraSymbolList;
	}
}
