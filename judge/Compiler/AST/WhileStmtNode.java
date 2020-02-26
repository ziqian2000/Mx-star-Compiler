package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class WhileStmtNode extends StmtNode {

	private ExprNode cond;
	private StmtNode bodyStmt;

	public WhileStmtNode(Position position, ExprNode cond, StmtNode bodyStmt){
		super(position);
		this.cond = cond;
		this.bodyStmt = bodyStmt;
	}

	public ExprNode getCond() {
		return cond;
	}

	public StmtNode getBodyStmt() {
		return bodyStmt;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
