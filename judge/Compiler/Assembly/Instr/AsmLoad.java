package Compiler.Assembly.Instr;

import Compiler.Assembly.Operand.PhysicalRegister;
import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IR.StackLocation;
import Compiler.IR.StackPointerOffset;
import Compiler.Utils.FuckingException;

import java.util.Collections;
import java.util.List;

public class AsmLoad extends AsmIns {

	private int size;
	private StackLocation loc;
	private Register rd;

	public AsmLoad(StackLocation loc, Register rd, int size){
		this.rd = rd;
		this.loc = loc;
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public StackLocation getLoc() {
		return loc;
	}

	public Register getRd() {
		return rd;
	}

	@Override
	public Register getDefRegister() {
		return rd;
	}

	@Override
	public List<Register> getUseRegister() {
		if(loc instanceof VirtualRegister)
			return ((VirtualRegister) loc).isGlobal() ? Collections.emptyList() : Collections.singletonList((Register) loc);
		else if(loc instanceof PhysicalRegister)
			return Collections.singletonList((Register) loc);
		else if(loc instanceof StackPointerOffset)
			return Collections.singletonList(((StackPointerOffset) loc).getSp());
		else throw new FuckingException("Unhandled type implemented from StackLocation.");
	}

	@Override
	public void replaceUseRegister(Register oldReg, Register newReg){
		if(loc == oldReg) loc = newReg;
	}

	@Override
	public void replaceDefRegister(Register newReg){
		rd = newReg;
	}

	@Override
	public void accept(AsmVisitor asmVisitor) {
		asmVisitor.visit(this);
	}

}
