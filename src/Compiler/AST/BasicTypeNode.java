package Compiler.AST;

import Compiler.utils.Position;

public abstract class BasicTypeNode extends TypeNode {

	public BasicTypeNode(Position position, String identifier){
		super(position, identifier);
	}
}
