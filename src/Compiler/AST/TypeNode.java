package Compiler.AST;

import Compiler.utils.Position;

public abstract class TypeNode extends BaseNode{

	private String identifier;

	public TypeNode(Position position, String identifier) {
		super(position);
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}
}
