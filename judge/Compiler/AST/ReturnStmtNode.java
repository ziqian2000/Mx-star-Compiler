package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class ReturnStmtNode extends StmtNode {

	private ExprNode expr;

	public ReturnStmtNode(Position position, ExprNode expr){
		super(position);
		this.expr = expr;
	}

	public ExprNode getExpr() {
		return expr;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}

}
