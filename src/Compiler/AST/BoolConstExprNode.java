package Compiler.AST;

import Compiler.utils.Position;

public class BoolConstExprNode extends ConstNode {

	private boolean value;

	public BoolConstExprNode(Position position, boolean value){
		super(position);
		this.value = value;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
