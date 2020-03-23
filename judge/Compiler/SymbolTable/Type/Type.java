package Compiler.SymbolTable.Type;

public abstract class Type {

	private String identifier;

	public Type(String identifier){
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String toArrayString(int dim){
		return identifier + "[" + dim + "]";
	}

	public abstract int getSize();
}
