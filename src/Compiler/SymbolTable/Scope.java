package Compiler.SymbolTable;

import Compiler.SymbolTable.Symbol.ClassSymbol;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.StringType;
import Compiler.SymbolTable.Type.Type;

import java.util.HashMap;
import java.util.Map;

public class Scope {

	private Scope upperScope;
	private Map<String, Symbol> symbolTable;
	private Map<String, Type> typeTable;

	public Scope(Scope upperScope){
		this.upperScope = upperScope;
		this.symbolTable = new HashMap<>();
		this.typeTable = upperScope == null ? new HashMap<>() : upperScope.getTypeTable();
	}

	public Scope getUpperScope() {
		return upperScope;
	}

	public Map<String, Type> getTypeTable() {
		return typeTable;
	}

	public boolean isTopScope(){
		return upperScope == null;
	}

	public Symbol findSymbol(String identifier){
		if(symbolTable.containsKey(identifier)) return symbolTable.get(identifier);
		else if(upperScope != null) return upperScope.findSymbol(identifier);
		else return null;
	}
	public Symbol findLocalSymbol(String identifier){
		return symbolTable.getOrDefault(identifier, null);
	}

	public Type findType(String identifier){
		return typeTable.getOrDefault(identifier, null);
	}

	public void addSymbol(String identifier, Symbol symbol){
		symbolTable.put(identifier, symbol);
	}

	public void addType(String identifier, Type type) {
		typeTable.put(identifier, type);
	}

}
