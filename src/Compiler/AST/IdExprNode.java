package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class IdExprNode extends ExprNode {

	private String identifier;

	public IdExprNode(Position position, String identifier){
		super(position);
		this.identifier = identifier;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}