package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.FuckingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Phi extends IRIns {

	private Register dst;
	private Map<BasicBlock, Register> path;

	public Phi(Register dst){
		this.dst = dst;
		path = new HashMap<>();
	}

	public Register getDst() {
		return dst;
	}

	public Map<BasicBlock, Register> getPath() {
		return path;
	}

	@Override
	public List<Register> getUseRegister() {
		throw new FuckingException("Don't call me");
	}

	@Override
	public Register getDefRegister() {
		return dst;
	}

	@Override
	public List<Operand> getOperands() {
		throw new FuckingException("Don't call me");
	}

	@Override
	public List<BasicBlock> getBBs() {
		throw new FuckingException("Don't call me");
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		throw new FuckingException("Don't call me");
	}

	@Override
	public void setDefRegister(Register newDefRegister) {
		dst = newDefRegister;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
