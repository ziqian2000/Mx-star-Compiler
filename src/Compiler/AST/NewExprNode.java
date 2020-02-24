package Compiler.AST;

import Compiler.utils.Position;

import java.util.List;

public class NewExprNode extends ExprNode {

	private TypeNode type;
	private int dim;
	private List<ExprNode> exprList;

	public NewExprNode(Position position, TypeNode type, int dim, List<ExprNode> exprList){
		super(position);
		this.type = type;
		this.dim = dim;
		this.exprList = exprList;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
