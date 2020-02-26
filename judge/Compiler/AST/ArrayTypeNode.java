package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class ArrayTypeNode extends TypeNode {

	private TypeNode type;

	public ArrayTypeNode(Position position, TypeNode typeNode){
		super(position, typeNode.getIdentifier() + "[]");
		this.type = typeNode;
	}

	public TypeNode getType() {
		return type;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
