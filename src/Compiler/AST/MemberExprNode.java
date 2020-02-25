package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.utils.Position;

public class MemberExprNode extends ExprNode {

	private ExprNode expr;
	private String identifier;

	public MemberExprNode(Position position, ExprNode exprNode, String identifier){
		super(position);
		this.expr = exprNode;
		this.identifier = identifier;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
