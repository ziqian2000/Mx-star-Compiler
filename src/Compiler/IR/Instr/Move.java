package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;
import Compiler.SemanticAnalysis.ASTVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	@Override
	public List<Operand> fetchOpr() {
		return Arrays.asList(src, dst);
	}

	@Override
	public List<BasicBlock> fetchBB() {
		return Collections.emptyList();
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Move(opr.get(0), opr.get(1));
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
