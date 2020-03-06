package Compiler.SymbolTable.Type;

import Compiler.Utils.Config;

public class ArrayType extends Type {

	private Type type;
	private int dim;

	public ArrayType(Type type, int dim){
		super(type.getIdentifier() + "[" + dim + "]");
		this.type = type;
		this.dim = dim;
	}

	public int getDim() {
		return dim;
	}

	public Type getType() {
		return type;
	}

	@Override
	public int getSize() {
		return Config.POINTER_SIZE;
	}
}
