package Compiler.AST;

import Compiler.utils.Position;

public class UnaryExprNode extends ExprNode {

	public enum Op{
		POS_INC, POS_SUB,
		PRE_INC, PRE_SUB,
		PLU, NEG,
		NOT, COM
	}

	private ExprNode opr;
	Op op;

	public UnaryExprNode(Position position, Op op, ExprNode opr){
		super(position);
		this.op = op;
		this.opr = opr;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
