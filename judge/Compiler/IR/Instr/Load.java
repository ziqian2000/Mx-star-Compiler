package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.IRVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	@Override
	public List<VirtualRegister> getUseRegister() {
		return ptr instanceof VirtualRegister ? Collections.singletonList((VirtualRegister) ptr) : Collections.emptyList();
	}

	@Override
	public VirtualRegister getDefRegister() {
		return dst instanceof VirtualRegister ? (VirtualRegister) dst : null;
	}

	@Override
	public void setDefRegister(VirtualRegister newDefRegister) {
		dst = newDefRegister;
	}

	@Override
	public List<Operand> getOperands() {
		return Arrays.asList(dst, ptr);
	}

	@Override
	public List<BasicBlock> getBBs() {
		return Collections.emptyList();
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		return new Load(opr.get(0), opr.get(1));
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
