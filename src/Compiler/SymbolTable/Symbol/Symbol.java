package Compiler.SymbolTable.Symbol;

public abstract class Symbol {

	private String identifier;

	public Symbol(String identifier){
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}
}
