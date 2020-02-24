package Compiler.AST;

import Compiler.utils.Position;

public class StringTypeNode extends BasicTypeNode {

	public StringTypeNode(Position position){
		super(position, "string");
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
