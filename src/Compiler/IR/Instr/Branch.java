package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.FuckingException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Branch extends IRIns {

	private Operand cond;
	private BasicBlock thenBB;
	private BasicBlock elseBB;

	public Branch(Operand cond, BasicBlock thenBB, BasicBlock elseBB){
		this.cond = cond;
		this.thenBB = thenBB;
		this.elseBB = elseBB;
	}

	public Operand getCond() {
		return cond;
	}

	public BasicBlock getThenBB() {
		return thenBB;
	}

	public BasicBlock getElseBB() {
		return elseBB;
	}

	@Override
	public List<Register> getUseRegister() {
		return cond instanceof Register ? Collections.singletonList((Register)cond) : Collections.emptyList();
	}

	@Override
	public Register getDefRegister() {
		return null;
	}

	@Override
	public void setDefRegister(Register newDefRegister) {
		throw new FuckingException("no def register in this instruction");
	}

	@Override
	public List<Operand> getOperands() {
		return Collections.singletonList(cond);
	}

	@Override
	public List<BasicBlock> getBBs() {
		return Arrays.asList(thenBB, elseBB);
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Branch(opr.get(0), BB.get(0), BB.get(1));
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
