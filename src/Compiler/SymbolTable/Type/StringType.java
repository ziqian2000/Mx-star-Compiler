package Compiler.SymbolTable.Type;

import Compiler.Utils.Config;

public class StringType extends BasicType {

	public StringType(){
		super("string");
	}

	@Override
	public int getSize() {
		return Config.POINTER_SIZE;
	}
}
