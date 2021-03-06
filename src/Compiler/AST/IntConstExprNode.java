package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class IntConstExprNode extends ConstNode {

	private int value;

	public IntConstExprNode(Position position, int value){
		super(position);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}

}
