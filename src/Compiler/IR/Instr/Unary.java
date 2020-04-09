package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	@Override
	public List<Register> getUseRegister() {
		return opr instanceof Register ? Collections.singletonList((Register) opr) : Collections.emptyList();
	}

	@Override
	public Register getDefRegister() {
		return dst instanceof Register ? (Register) dst : null;
	}

	@Override
	public void setDefRegister(Register newDefRegister) {
		dst = newDefRegister;
	}

	@Override
	public List<Operand> getOperands() {
		return Arrays.asList(opr, dst);
	}

	@Override
	public List<BasicBlock> getBBs() {
		return Collections.emptyList();
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Unary(op, opr.get(0), opr.get(1));
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
