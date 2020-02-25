package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

import java.util.List;

public class CompStmtNode extends StmtNode {

	private List<StmtNode> stmtList;

	public CompStmtNode(Position position, List<StmtNode> stmtList){
		super(position);
		this.stmtList = stmtList;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}

}