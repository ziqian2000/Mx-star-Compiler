package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

public class Binary extends IRIns {

	public enum Op{
		ADD, SUB, MUL, DIV, MOD,
		SHL, SHR,
		LT, LE, GT, GE, EQ, NEQ,
		AND, OR, XOR
	}

	// to perform [op] operation on [lhs] and [rhs], and store the result in [dst]

	private Op op;
	private Operand lhs, rhs, dst;

	public Binary(Op op, Operand lhs, Operand rhs, Operand dst){
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
		this.dst = dst;
	}

	public Op getOp() {
		return op;
	}

	public Operand getLhs() {
		return lhs;
	}

	public Operand getRhs() {
		return rhs;
	}

	public Operand getDst() {
		return dst;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
