package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.utils.Position;

public class StringConstExprNode extends ConstNode {

	private String value;

	public StringConstExprNode(Position position, String value) {
		super(position);
		this.value = value;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
