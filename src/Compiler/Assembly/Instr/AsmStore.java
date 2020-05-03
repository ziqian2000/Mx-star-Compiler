package Compiler.Assembly.Instr;

import Compiler.Assembly.Operand.PhysicalRegister;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IR.StackLocation;
import Compiler.IR.StackPointerOffset;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AsmStore extends AsmIns {

	// [ loc ] ← rs, rt
	private int size;
	private StackLocation loc;
	private Register rs, rt;

	public AsmStore(StackLocation loc, Register rs, Register rt, int size){
		this.rs = rs;
		this.loc = loc;
		this.size = size;
		this.rt = rt;
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
}
