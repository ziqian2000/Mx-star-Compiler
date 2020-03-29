package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

import java.util.Collections;
import java.util.List;

public class Jump extends IRIns {

	private BasicBlock BB;

	public Jump(BasicBlock BB){
		this.BB = BB;
	}

	public BasicBlock getBB() {
		return BB;
	}

	@Override
	public List<Operand> fetchOpr() {
		return Collections.emptyList();
	}

	@Override
	public List<BasicBlock> fetchBB() {
		return Collections.singletonList(BB);
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Jump(BB.get(0));
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
