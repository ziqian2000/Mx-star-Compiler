package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class FuncDeclNode extends DeclNode {

	private TypeNode type;
	private String identifier;
	private VarDeclListNode paraDeclList;
	private StmtNode bodyStmt;

	public FuncDeclNode(Position position, TypeNode type, String identifier, VarDeclListNode paraDeclList, StmtNode bodyStmt){
		super(position);
		this.type = type;
		this.identifier = identifier;
		this.paraDeclList = paraDeclList;
		this.bodyStmt = bodyStmt;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
