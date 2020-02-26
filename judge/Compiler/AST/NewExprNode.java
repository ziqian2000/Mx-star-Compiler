package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.Utils.Position;

import java.util.List;

public class NewExprNode extends ExprNode {

	private TypeNode baseType;
	private int dim;
	private List<ExprNode> exprList;

	private Symbol symbol;

	public NewExprNode(Position position, TypeNode baseType, int dim, List<ExprNode> exprList){
		super(position);
		this.baseType = baseType;
		this.dim = dim;
		this.exprList = exprList;
	}

	public TypeNode getBaseType() {
		return baseType;
	}

	public int getDim() {
		return dim;
	}

	public List<ExprNode> getExprList() {
		return exprList;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
