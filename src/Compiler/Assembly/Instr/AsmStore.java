package Compiler.Assembly.Instr;

import Compiler.Assembly.Operand.PhysicalRegister;
import Compiler.Codegen.AsmVisitor;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IR.StackLocation;
import Compiler.IR.StackPointerOffset;

import java.util.ArrayList;
import java.util.List;

public class AsmStore extends AsmIns {

	// [ loc ] ← rs, rt
	private int size;
	private Register rs, rt;

	private StackLocation loc; // if loc is of StackPointerOffset, then offset is useless
	private int offset; // available only when loc is a register

	public AsmStore(StackPointerOffset loc, Register rs, Register rt, int size){
		this.rs = rs;
		this.loc = loc;
		this.size = size;
		this.rt = rt;
	}

	public AsmStore(Register loc, Register rs, int offset, Register rt, int size){
		this.rs = rs;
		this.loc = loc;
		this.offset = offset;
		this.size = size;
		this.rt = rt;
	}

	public int getSize() {
		return size;
	}

	public StackLocation getLoc() {
		return loc;
	}

	public int getOffset() {
		return offset;
	}

	public Register getRs() {
		return rs;
	}

	public Register getRt() {
		return rt;
	}

	public void setLoc(StackLocation loc) {
		this.loc = loc;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public Register getDefRegister() {
		return rt;
	}

	@Override
	public List<Register> getUseRegister() {
		List<Register> regs = new ArrayList<>();

		regs.add(rs);
//		if(rt != null) regs.add(rt);

		if(loc instanceof VirtualRegister && !((VirtualRegister) loc).isGlobal()) {
			regs.add((Register) loc);
		} else if(loc instanceof PhysicalRegister)
			regs.add((Register) loc);
		else if(loc instanceof StackPointerOffset)
			regs.add(((StackPointerOffset) loc).getSp());
		return regs;
	}

	@Override
	public void replaceUseRegister(Register oldReg, Register newReg){
		if(loc == oldReg) loc = newReg;
		if(rs == oldReg) rs = newReg;
	}

	@Override
	public void replaceDefRegister(Register newReg){
		if(rt != null) rt = newReg;
		else assert false;
	}

	@Override
	public void accept(AsmVisitor asmVisitor) {
		asmVisitor.visit(this);
	}
}
