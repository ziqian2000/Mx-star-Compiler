package Compiler.AST;

import Compiler.utils.Position;

public class ClassTypeNode extends TypeNode {

	public ClassTypeNode(Position position, String identifier){
		super(position, identifier);
	}


	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
