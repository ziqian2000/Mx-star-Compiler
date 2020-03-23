package Compiler.SymbolTable.Type;

import Compiler.Utils.Config;

public abstract class BasicType extends Type {
	public BasicType(String identifier){
		super(identifier);
	}

	@Override
	public int getSize() {
		return Config.BASIC_TYPE_SIZE;
	}
}
