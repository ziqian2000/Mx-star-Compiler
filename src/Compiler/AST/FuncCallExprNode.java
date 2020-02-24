package Compiler.AST;

import Compiler.utils.Position;

import java.util.List;

public class FuncCallExprNode extends ExprNode {

	private ExprNode function;
	private List<ExprNode> paraList;

	public FuncCallExprNode(Position position, ExprNode function, List<ExprNode> paraList){
		super(position);
		this.function = function;
		this.paraList = paraList;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
