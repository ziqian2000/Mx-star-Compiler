package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

import java.util.List;

public class CompStmtNode extends StmtNode {

	private List<StmtNode> stmtList;

	private boolean functionBody;

	public CompStmtNode(Position position, List<StmtNode> stmtList){
		super(position);
		this.stmtList = stmtList;
		this.functionBody = false;
	}

	public void setFunctionBody(boolean functionBody) {
		this.functionBody = functionBody;
	}

	public boolean getFunctionBody(){
		return functionBody;
	}

	public List<StmtNode> getStmtList() {
		return stmtList;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}

}
