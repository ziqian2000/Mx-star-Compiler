package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.IRVisitor;

import java.util.ArrayList;
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

	public void setOp(Op op) {
		this.op = op;
	}

	@Override
	public List<VirtualRegister> getUseRegister() {
		List<VirtualRegister> registerList = new ArrayList<>();
		if(lhs instanceof VirtualRegister) registerList.add((VirtualRegister)lhs);
		if(rhs instanceof VirtualRegister) registerList.add((VirtualRegister)rhs);
		return registerList;
	}

	@Override
	public VirtualRegister getDefRegister() {
		return dst instanceof VirtualRegister ? (VirtualRegister) dst : null;
	}

	@Override
	public void setDefRegister(VirtualRegister newDefRegister) {
		dst = newDefRegister;
	}

	@Override
	public List<Operand> getOperands() {
		return Arrays.asList(lhs, rhs, dst);
	}

	@Override
	public List<BasicBlock> getBBs() {
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
