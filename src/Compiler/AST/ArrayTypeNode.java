package Compiler.AST;

import Compiler.utils.Position;

public class ArrayTypeNode extends TypeNode {

	private TypeNode type;

	public ArrayTypeNode(Position position, TypeNode typeNode){
		super(position, typeNode.getIdentifier() + "[]");
		this.type = typeNode;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
