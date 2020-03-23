package Compiler.IR.Instr;

import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;
import Compiler.SemanticAnalysis.ASTVisitor;

public class Move extends IRIns {

	// to copy value [src] into [dst], both of which are in register but not in memory

	private Operand src;
	private Operand dst;

	public Move(Operand src, Operand dst){
		this.src = src;
		this.dst = dst;
	}

	public Operand getSrc() {
		return src;
	}

	public Operand getDst() {
		return dst;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
