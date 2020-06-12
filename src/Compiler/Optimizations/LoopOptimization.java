package Compiler.Optimizations;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LoopOptimization extends Pass{

	boolean changed;
	private IR ir;

	public LoopOptimization(IR ir){
		this.ir = ir;
	}

	public boolean run() {
		changed = false;
		for(var func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			loopInvariantCodeMotion(func);
		}
		return changed;
	}

	List<VirtualRegister> loopInvariantReg = new ArrayList<>();
	private void loopInvariantCodeMotion(Function func){
		boolean continueFinding = true;

		while(continueFinding) {
			continueFinding = false;

			func.makePreOrderBBList();
			computeDefUseChain(func);
			computeLoopInfo(func);
			loopInvariantReg.clear();

			// find at least one loop with invariants
			for (var header : loopHeaders) {
				for (var BB : loopGroups.get(header)) {
					for (var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns())
						if(ins instanceof Binary || ins instanceof Move || ins instanceof Unary) {

							if (ins.getDefRegister() == null) continue;

							boolean invariant = true;
							for (var use : ins.getUseRegister()) { // skips immediate and string
								if (!isInvariant(use, header)) {
									invariant = false;
									break;
								}
							}
							if (invariant) {
								loopInvariantReg.add(ins.getDefRegister());
							}

						}
				}
				if (loopInvariantReg.size() > 0) {
					// at least one invariant

					// pre-header
					var preHeader = new BasicBlock(header.getIdentifier() + "_pre_header");
					preHeader.sudoAppendInst(new Jump(header));
					for(var pre : header.getPreBBList()) if(!loopGroups.get(header).contains(pre)) {
						var tailIns = pre.getTailIns();
						if(tailIns instanceof Jump) ((Jump) tailIns).replaceTargetBB(preHeader);
						else if(tailIns instanceof Branch) ((Branch) tailIns).replaceTargetBB(header, preHeader);
						else assert false;
					}

					// phi splitting
					for(var ins = header.getHeadIns(); ins instanceof Phi; ins = ins.getNextIns()){
						Map<BasicBlock, Operand> pathInPreHeader = new LinkedHashMap<>();
						Map<BasicBlock, Operand> pathInHeader = new LinkedHashMap<>();
						for(var entry : ((Phi) ins).getPath().entrySet()){
							var pre = entry.getKey();
							if(!loopGroups.get(header).contains(pre)) // outside loop
								pathInPreHeader.put(entry.getKey(), entry.getValue());
							else pathInHeader.put(entry.getKey(), entry.getValue());
						}

						var tmp = new I32Value(((Phi) ins).getDst().getIdentifier() + "_licm_phi_tmp");
						var newPhi = new Phi(tmp); newPhi.setPath(pathInPreHeader);
						preHeader.getTailIns().prependIns(newPhi);
						newPhi.checkPathSize();

						pathInHeader.put(preHeader, tmp);
						((Phi) ins).setPath(pathInHeader);
						((Phi) ins).checkPathSize();
					}

					// hoisting
					for (var reg : loopInvariantReg) {
						var ins = reg.def;
						ins.removeFromList();
						preHeader.getTailIns().prependIns(ins);
					}

					changed = continueFinding = true;
					break;
				}
			}
		}
	}
	boolean isInvariant(VirtualRegister reg, BasicBlock header){
		if(reg.def == null) return true; // global variable
		return loopInvariantReg.contains(reg) || !loopGroups.get(header).contains(reg.def.getBelongBB());
	}

	void debug(String s){
		System.err.println(s);
	}

}
