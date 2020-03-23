package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

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

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
