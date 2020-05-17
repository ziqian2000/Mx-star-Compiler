package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.Immediate;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 	This file implements dead code elimination and basic block merging, along
 * 	with a collection of other peephole control flow optimizations.  For example:
 * 		[x] Remove redundant basic blocks. e.g. The conditions is an immediate.
 * 		[x] Removes basic blocks with no predecessors. --- Handled automatically when making basic block list.
 *  	[x] Merges a basic block into its predecessor if there is only one and the predecessor only has one successor.
 *  	[x] Eliminates PHI nodes for basic blocks with a single predecessor. --- implemented in Phi.java
 *  	[x] Eliminates a basic block that only contains an unconditional branch. -- only applies to non-SSA form
 *  	[x] Eliminate MOVE nodes where src = dst
 *
 * 	It's a LLVM pass from https://github.com/llvm-mirror/llvm/blob/release_60/lib/Transforms/Scalar/SimplifyCFGPass.cpp
 */

public class CFGSimplifier {

	IR ir;

	public CFGSimplifier(IR ir){
		this.ir = ir;
	}

	public boolean run(){
		boolean changed = false;
		
		for(boolean changedThisTime = true; changedThisTime; ){
			changedThisTime = false;
			for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
				func.makePreOrderBBList();
				changedThisTime |= removeRedundantBranch(func);
				changedThisTime |= removeDeadBasicBlockInPhi(func);
				changedThisTime |= mergeBasicBlock(func);
				if(!ir.getSSAForm()) {
					changedThisTime |= removeSingleJumpBasicBlock(func);
					changedThisTime |= removeRedundantMove(func);
				}
			}
			changed |= changedThisTime;
		}
		
		return changed;
	}

	public boolean mergeBasicBlock(Function func){
		boolean changed = false;
		for(int i = func.getPreOrderBBList().size() - 1; i >= 0; i--){
			BasicBlock BB = func.getPreOrderBBList().get(i);
			if(BB.getSucBBList().size() == 1){
				BasicBlock sucBB = BB.getSucBBList().get(0);
				if(sucBB != func.getEntryBB() && sucBB.getPreBBList().size() == 1){
					assert BB != sucBB;
					assert !(sucBB.getHeadIns() instanceof Phi);
					changed = true;
					BB.mergeBB(sucBB);
					if(sucBB == func.getExitBB())
						func.setExitBB(BB);
				}
			}
		}
		if(changed) func.makePreOrderBBList();
		return changed;
	}

	public boolean removeSingleJumpBasicBlock(Function func){
		boolean changed = false;
		for(BasicBlock BB : func.getPreOrderBBList()) if(BB != func.getEntryBB()) {
			if(BB.getHeadIns() instanceof Jump) { // merely contain a JUMP instruction
				changed = true;
				Jump ins = (Jump) BB.getHeadIns();
				BasicBlock sucBB = ins.getBB();
				sucBB.getPreBBList().remove(BB);

				for(BasicBlock preBB : BB.getPreBBList()){
					preBB.getSucBBList().remove(BB);
					preBB.getSucBBList().add(sucBB);
					sucBB.getPreBBList().add(preBB);

					IRIns jumpIns = preBB.getTailIns();
					if(jumpIns instanceof Jump) ((Jump) jumpIns).replaceTargetBB(sucBB);
					else if(jumpIns instanceof Branch) ((Branch) jumpIns).replaceTargetBB(BB, sucBB);
					else throw new FuckingException("a fucking instruction at the end of fucking a basic block");
				}
			}
		}
		if(changed) func.makePreOrderBBList();
		return changed;
	}

	public boolean removeRedundantBranch(Function func){
		boolean changed = false;
		boolean needBBListMaking = false;
		for(BasicBlock BB : func.getPreOrderBBList()){
			if(BB.getTailIns() instanceof Branch){
				Branch ins = (Branch) BB.getTailIns();

				if(ins.getThenBB() == ins.getElseBB()){
					changed = true;
					ins.replaceSelfWithAnotherIns(new Jump(ins.getThenBB()));
				}
				else if(ins.getCond() instanceof Immediate){
					changed = true;
					needBBListMaking = true;
					int imm = ((Immediate) ins.getCond()).getValue();
					BasicBlock selected 	= imm == 0 ? ins.getElseBB() : ins.getThenBB();
					BasicBlock unselected 	= imm == 0 ? ins.getThenBB() : ins.getElseBB();
					ins.replaceSelfWithAnotherIns(new Jump(selected));
					unselected.removeBBInPhi(BB);
					BB.getSucBBList().remove(unselected);
					unselected.getPreBBList().remove(BB);
				}

			}
		}
		if(needBBListMaking) func.makePreOrderBBList();
		return changed;
	}

	public boolean removeDeadBasicBlockInPhi(Function func){
		boolean changed = false;
		for(BasicBlock BB : func.getPreOrderBBList()){
			for(IRIns ins = BB.getHeadIns(), nextIns; ins instanceof Phi; ins = nextIns){
				nextIns = ins.getNextIns();

				Map<BasicBlock, Operand> path = ((Phi) ins).getPath();
				List<BasicBlock> removeList = new ArrayList<>();

				for(BasicBlock fromBB : path.keySet()){
					if(!BB.getPreBBList().contains(fromBB)){
						removeList.add(fromBB);
						changed = true;
					}
				}
				removeList.forEach(((Phi) ins)::removePath);
			}
		}
		return changed;
	}

	public boolean removeRedundantMove(Function func){
		boolean changed = false;
		for(var BB : func.getPreOrderBBList()){
			IRIns nextIns;
			for(var ins = BB.getHeadIns(); ins != null; ins = nextIns) {
				nextIns = ins.getNextIns();
				if (ins instanceof Move && ((Move) ins).getSrc() == ins.getDefRegister()) {
					ins.removeFromList();
					changed = true;
				}
			}
		}
		return changed;
	}
}
