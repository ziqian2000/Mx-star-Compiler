package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.Utils.Position;

public class FuncDeclNode extends DeclNode {

	private TypeNode type;
	private String identifier;
	private VarDeclListNode paraDeclList;
	private StmtNode bodyStmt;
	private FuncSymbol funcSymbol;

	public FuncDeclNode(Position position, TypeNode type, String identifier, VarDeclListNode paraDeclList, StmtNode bodyStmt){
		super(position);
		this.type = type;
		this.identifier = identifier;
		this.paraDeclList = paraDeclList;
		this.bodyStmt = bodyStmt;
	}

	public String getIdentifier(){
		return identifier;
	}

	public TypeNode getType() {
		return type;
	}

	public VarDeclListNode getParaDeclList() {
		return paraDeclList;
	}

	public StmtNode getBodyStmt() {
		return bodyStmt;
	}

	public FuncSymbol getFuncSymbol() {
		return funcSymbol;
	}

	public void setFuncSymbol(FuncSymbol funcSymbol) {
		this.funcSymbol = funcSymbol;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
