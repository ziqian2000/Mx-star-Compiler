package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.utils.Position;

public class VarDeclStmtNode extends StmtNode {

	private VarDeclListNode varDeclList;

	public VarDeclStmtNode(Position position, VarDeclListNode varDeclList){
		super(position);
		this.varDeclList = varDeclList;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
