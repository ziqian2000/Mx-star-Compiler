package Compiler.AST;

import Compiler.utils.Position;

public class IntConstExprNode extends ConstNode {

	private int value;

	public IntConstExprNode(Position position, int value){
		super(position);
		this.value = value;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}

}
