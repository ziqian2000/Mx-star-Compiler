package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

public class Unary extends IRIns {

	public enum Op{
		POS_INC, POS_SUB,
		PRE_INC, PRE_SUB,
		PLU, NEG,
		NOT, COM
	}

	// to perform unary operation [op] on operand [opr], and store the result in [dst]

	private Op op;
	private Operand opr, dst;

	public Unary(Op op, Operand opr, Operand dst){
		this.op = op;
		this.opr = opr;
		this.dst = dst;
	}

	public Op getOp() {
		return op;
	}

	public Operand getOpr() {
		return opr;
	}

	public Operand getDst() {
		return dst;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
