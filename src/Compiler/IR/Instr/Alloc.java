package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

public class Alloc extends IRIns {

	// to allocate [size] memory to pointer [ptr]

	private Operand size;
	private Operand ptr;

	public Alloc(Operand size, Operand ptr){
		this.size = size;
		this.ptr = ptr;
	}

	public Operand getSize() {
		return size;
	}

	public Operand getPtr() {
		return ptr;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
