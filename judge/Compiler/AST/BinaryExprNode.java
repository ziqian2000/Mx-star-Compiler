package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

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

	public ExprNode getLhs() {
		return lhs;
	}

	public ExprNode getRhs() {
		return rhs;
	}

	public Op getOp() {
		return op;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
