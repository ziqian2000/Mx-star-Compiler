package Compiler.AST;

import Compiler.utils.Position;

public class NullConstExprNode extends ConstNode {

	public NullConstExprNode(Position position){
		super(position);
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
