package Compiler.AST;

import Compiler.utils.Position;

public class IfStmtNode extends StmtNode {

	private ExprNode cond;
	private StmtNode thenStmt, elseStmt;

	public IfStmtNode(Position position, ExprNode cond, StmtNode thenStmt, StmtNode elseStmt){
		super(position);
		this.cond = cond;
		this.thenStmt = thenStmt;
		this.elseStmt = elseStmt;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
