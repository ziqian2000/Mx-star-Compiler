package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class ForStmtNode extends StmtNode {

	private ExprNode init, cond, step;
	private StmtNode bodyStmt;

	public ForStmtNode(Position position, ExprNode init, ExprNode cond, ExprNode step, StmtNode bodyStmt){
		super(position);
		this.init = init;
		this.cond = cond;
		this.step = step;
		this.bodyStmt = bodyStmt;
	}

	public ExprNode getInit() {
		return init;
	}

	public ExprNode getCond() {
		return cond;
	}

	public ExprNode getStep() {
		return step;
	}

	public StmtNode getBodyStmt() {
		return bodyStmt;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
