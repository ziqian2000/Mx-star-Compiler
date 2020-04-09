package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.Register;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	@Override
	public List<Register> getUseRegister() {
		return size instanceof Register ? Collections.singletonList((Register)size) : Collections.emptyList();
	}

	@Override
	public Register getDefRegister() {
		return (Register) ptr;
	}

	@Override
	public void setDefRegister(Register newDefRegister) {
		ptr = newDefRegister;
	}

	@Override
	public List<Operand> getOperands() {
		return Arrays.asList(size, ptr);
	}

	@Override
	public List<BasicBlock> getBBs() {
		return Collections.emptyList();
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Alloc(opr.get(0), opr.get(1));
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}

}
