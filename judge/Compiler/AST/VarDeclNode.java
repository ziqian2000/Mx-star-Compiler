package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.Utils.Position;

public class VarDeclNode extends DeclNode {

	private TypeNode type;
	private String identifier;
	private ExprNode expr;
	private Symbol symbol;

	public VarDeclNode(Position position, TypeNode type, String identifier, ExprNode expr){
		super(position);
		this.type = type;
		this.identifier = identifier;
		this.expr = expr;
	}

	public String getIdentifier(){
		return identifier;
	}

	public TypeNode getType() {
		return type;
	}

	public void setType(TypeNode type){
		this.type = type;
	}

	public ExprNode getExpr() {
		return expr;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
