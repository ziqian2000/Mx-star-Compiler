package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IRVisitor.IRVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Return extends IRIns {

	// to return a value

	private Operand retValue;

	public Return(Operand retValue){
		this.retValue = retValue;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

	@Override
	public List<Operand> fetchOpr() {
		return Arrays.asList(retValue);
	}

	@Override
	public List<BasicBlock> fetchBB() {
		return Collections.emptyList();
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Return(opr.get(0));
	}

	public Operand getRetValue() {
		return retValue;
	}
}
