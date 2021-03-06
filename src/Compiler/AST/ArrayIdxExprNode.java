package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class ArrayIdxExprNode extends ExprNode {

	ExprNode array, idx;

	public ArrayIdxExprNode(Position position, ExprNode array, ExprNode idx){
		super(position);
		this.array = array;
		this.idx = idx;
	}

	public ExprNode getArray() {
		return array;
	}

	public ExprNode getIdx() {
		return idx;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
