package Compiler.SymbolTable.Type;

import Compiler.SymbolTable.Symbol.ClassSymbol;
import Compiler.Utils.FuckingException;

public class ClassType extends Type {

	private ClassSymbol selfSymbol;
	private int size;

	public ClassType(String identifier){
		super(identifier);
	}

	public void setSelfSymbol(ClassSymbol classSymbol) {
		this.selfSymbol = classSymbol;
	}

	public ClassSymbol getSelfSymbol() {
		return selfSymbol;
	}

	public void setSize(int siz) {
		this.size = siz;
	}

	@Override
	public int getSize() {
		return size;
	}
}
