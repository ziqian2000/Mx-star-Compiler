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

public class Store extends IRIns {

	// to store something in register [src] to position [ptr]

	private Operand src;
	private Operand ptr;

	public Store(Operand src, Operand ptr){
		this.src = src;
		this.ptr = ptr;
	}

	public Operand getSrc() {
		return src;
	}

	public Operand getPtr() {
		return ptr;
	}

	@Override
	public List<Register> getUseRegister() {
		List<Register> registerList = new ArrayList<>();
		if(src instanceof Register) registerList.add((Register)src);
		if(ptr instanceof Register) registerList.add((Register)ptr);
		return registerList;
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
		return Arrays.asList(src, ptr);
	}

	@Override
	public List<BasicBlock> getBBs() {
		return Collections.emptyList();
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Store(opr.get(0), opr.get(1));
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
