package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.utils.Position;

public class WhileStmtNode extends StmtNode {

	private ExprNode cond;
	private StmtNode bodyStmt;

	public WhileStmtNode(Position position, ExprNode cond, StmtNode bodyStmt){
		super(position);
		this.cond = cond;
		this.bodyStmt = bodyStmt;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
