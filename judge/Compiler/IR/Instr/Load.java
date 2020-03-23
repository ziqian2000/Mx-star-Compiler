package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

public class Load extends IRIns {

	// to load from [ptr] and store in register [dst]

	private Operand dst;
	private Operand ptr;

	public Load(Operand dst, Operand ptr){
		this.dst = dst;
		this.ptr = ptr;
	}

	public Operand getDst() {
		return dst;
	}

	public Operand getPtr() {
		return ptr;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
