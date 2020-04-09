package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IRVisitor.IRVisitor;
import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
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
	public List<Register> getUseRegister() {
		return src instanceof Register ? Collections.singletonList((Register) src) : Collections.emptyList();
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
		return Arrays.asList(src, dst);
	}

	@Override
	public List<BasicBlock> getBBs() {
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
