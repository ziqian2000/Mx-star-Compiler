package Compiler.AST;

import Compiler.utils.Position;

public class ThisExprNode extends ExprNode{

	private String identifier;

	public ThisExprNode(Position position){
		super(position);
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
