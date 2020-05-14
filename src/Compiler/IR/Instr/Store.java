package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
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
	public List<VirtualRegister> getUseRegister() {
		List<VirtualRegister> registerList = new ArrayList<>();
		if(src instanceof VirtualRegister) registerList.add((VirtualRegister)src);
		if(ptr instanceof VirtualRegister) registerList.add((VirtualRegister)ptr);
		return registerList;
	}

	@Override
	public VirtualRegister getDefRegister() {
		return null;
	}

	@Override
	public void replaceUseOpr(Operand oldOpr, Operand newOpr) {
		if(src == oldOpr) src = newOpr;
		if(ptr == oldOpr) ptr = newOpr;
	}

	@Override
	public void replaceDefRegister(VirtualRegister newDefRegister) {
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
