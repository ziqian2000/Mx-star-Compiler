package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

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
	public List<Operand> fetchOpr() {
		return Arrays.asList(src, ptr);
	}

	@Override
	public List<BasicBlock> fetchBB() {
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
