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
	private Register rd;

	private StackLocation loc; // if loc is of StackPointerOffset, then offset is useless
	private int offset; // available only when loc is a register

	public AsmLoad(StackPointerOffset loc, Register rd, int size){
		// loc is of StackPointerOffset type
		this.rd = rd;
		this.loc = loc;
		this.size = size;
	}

	public AsmLoad(Register loc, Register rd, int offset, int size){
		// loc is a register
		this.rd = rd;
		this.loc = loc;
		this.offset = offset;
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

	public int getOffset() {
		return offset;
	}

	public void setRd(Register rd) {
		this.rd = rd;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setLoc(StackLocation loc) {
		this.loc = loc;
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
