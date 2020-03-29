package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	@Override
	public List<Operand> fetchOpr() {
		return Arrays.asList(lhs, rhs, dst);
	}

	@Override
	public List<BasicBlock> fetchBB() {
		return Collections.emptyList();
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Binary(op, opr.get(0), opr.get(1), opr.get(2));
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
