package Compiler.AST;

import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Position;

public abstract class TypeNode extends BaseNode{

	private String identifier;
	private Type typeInfo;

	public TypeNode(Position position, String identifier) {
		super(position);
		this.identifier = identifier;
	}

	public Type getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(Type type) {
		this.typeInfo = type;
	}

	public String getIdentifier() {
		return identifier;
	}
}
