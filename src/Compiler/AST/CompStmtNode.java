package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Scope;
import Compiler.Utils.Position;

import java.util.List;

public class CompStmtNode extends StmtNode {

	private List<StmtNode> stmtList;

	private boolean isFunctionBody;

	public CompStmtNode(Position position, List<StmtNode> stmtList){
		super(position);
		this.stmtList = stmtList;
		this.isFunctionBody = false;
	}

	public void setIsFunctionBody(boolean functionBody) {
		this.isFunctionBody = functionBody;
	}

	public boolean getIsFunctionBody(){
		return isFunctionBody;
	}

	public List<StmtNode> getStmtList() {
		return stmtList;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}

}
