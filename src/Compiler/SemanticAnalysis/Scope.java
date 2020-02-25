package Compiler.SemanticAnalysis;

import Compiler.Symbol.Symbol;

import java.util.HashMap;
import java.util.Map;

public class Scope {

	private Scope upperScope;
	private Map<String, Symbol> varSymbolTable, funcSymbolTable, classSymbolTable;

	public Scope(Scope upperScope){
		this.upperScope = upperScope;
		this.varSymbolTable = new HashMap<>();
		this.funcSymbolTable = new HashMap<>();
		this.classSymbolTable = new HashMap<>();
	}

	public Symbol findVar(String identifier){
		if(varSymbolTable.containsKey(identifier)) return varSymbolTable.get(identifier);
		else if(upperScope != null) return upperScope.findVar(identifier);
		else return null;
	}
	public Symbol findFunc(String identifier){
		if(funcSymbolTable.containsKey(identifier)) return funcSymbolTable.get(identifier);
		else if(upperScope != null) return upperScope.findFunc(identifier);
		else return null;
	}
	public Symbol findClass(String identifier){
		if(classSymbolTable.containsKey(identifier)) return classSymbolTable.get(identifier);
		else if(upperScope != null) return upperScope.findClass(identifier);
		else return null;
	}

	public void addVar(String identifier, Symbol symbol){
		varSymbolTable.put(identifier, symbol);
	}

	public void addFunc(String identifier, Symbol symbol){
		funcSymbolTable.put(identifier, symbol);
	}

	public void addClass(String identifier, Symbol symbol){
		classSymbolTable.put(identifier, symbol);
	}

}
