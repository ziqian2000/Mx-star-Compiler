package Compiler.AST;

import Compiler.utils.Position;

public class BinaryExprNode extends ExprNode {

	public enum Op{
		ADD, SUB, MUL, DIV, MOD,
		SHL, SHR,
		LT, LE, GT, GE, EQ, NEQ,
		ASS,
		AND, OR, XOR, LAND, LOR
	}

	private ExprNode lhs, rhs;
	Op op;

	public BinaryExprNode(Position position, Op op, ExprNode lhs, ExprNode rhs){
		super(position);
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
