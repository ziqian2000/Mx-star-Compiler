package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class ExprStmtNode extends StmtNode{

	private ExprNode expr;

	public ExprStmtNode(Position position, ExprNode expr){
		super(position);
		this.expr = expr;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
