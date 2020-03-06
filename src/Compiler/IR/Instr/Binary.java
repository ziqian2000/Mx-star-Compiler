package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;

public class Binary extends IR{

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

}
