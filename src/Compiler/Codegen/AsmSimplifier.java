package Compiler.Codegen;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.Assembly.AsmFunction;
import Compiler.Assembly.Assembly;
import Compiler.Assembly.Instr.*;
import Compiler.IR.Operand.VirtualRegister;

public class AsmSimplifier extends AsmPass{

	Assembly asm;

	public AsmSimplifier(Assembly asm){
		this.asm = asm;
	}

	public boolean run(){
		boolean changed = false;
		
		for(boolean changedThisTime = true; changedThisTime; ){
			changedThisTime = false;
			for(AsmFunction func : asm.getFunctionList()) if(!func.getIsBuiltin()) {
				func.makePreOrderBBList();
				changedThisTime |= mergeBasicBlock(func);
				changedThisTime |= removeSingleJumpBasicBlock(func);
				changedThisTime |= removeRedundantMove(func);
				changedThisTime |= naiveDeadCodeElimination(func);
			}
			changed |= changedThisTime;
		}
		
		return changed;
	}

	public boolean mergeBasicBlock(AsmFunction func){
		boolean changed = false;
		for(int i = func.getPreOrderBBList().size() - 1; i >= 0; i--){
			AsmBasicBlock BB = func.getPreOrderBBList().get(i);
			if(BB.getSucBBList().size() == 1){
				AsmBasicBlock sucBB = BB.getSucBBList().get(0);
				if(sucBB != func.getEntryBB() && sucBB.getPreBBList().size() == 1){
					assert BB != sucBB;
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

	public boolean removeSingleJumpBasicBlock(AsmFunction func){
		boolean changed = false;
		for(AsmBasicBlock BB : func.getPreOrderBBList()) if(BB != func.getEntryBB()) {
			if(BB.getHeadIns() instanceof AsmJump) { // merely contain a JUMP instruction
				changed = true;
				AsmJump ins = (AsmJump) BB.getHeadIns();
				AsmBasicBlock sucBB = ins.getBB();
				sucBB.getPreBBList().remove(BB);

				for(AsmBasicBlock preBB : BB.getPreBBList()){
					preBB.getSucBBList().remove(BB);
					preBB.getSucBBList().add(sucBB);
					sucBB.getPreBBList().add(preBB);

					AsmIns jIns = preBB.getTailIns();
					if(jIns.getPrevIns() != null) jIns = jIns.getPrevIns();
					for(; jIns != null; jIns = jIns.getNextIns()){
						if(jIns instanceof AsmJump && ((AsmJump) jIns).getBB() == BB) ((AsmJump) jIns).setBB(sucBB);
						if(jIns instanceof AsmBranch && ((AsmBranch) jIns).getBB() == BB) ((AsmBranch) jIns).setBB(sucBB);
					}
				}
			}
		}
		if(changed) func.makePreOrderBBList();
		return changed;
	}

	public boolean removeRedundantMove(AsmFunction func){
		boolean changed = false;
		for(var BB : func.getPreOrderBBList()){
			AsmIns nextIns;
			for(var ins = BB.getHeadIns(); ins != null; ins = nextIns) {
				nextIns = ins.getNextIns();
				if (ins instanceof AsmMove && ((AsmMove) ins).getRs1() == ((AsmMove) ins).getRd()) {
					ins.removeFromList();
					changed = true;
				}
			}
		}
		return changed;
	}

	public boolean naiveDeadCodeElimination(AsmFunction func){
		computeDefUseChain(func);
		boolean changed = false;
		for(var BB : func.getPreOrderBBList()){
			AsmIns nextIns;
			for(var ins = BB.getHeadIns(); ins != null; ins = nextIns) {
				nextIns = ins.getNextIns();
				if(!(ins instanceof AsmStore)){
					if(ins.getDefRegister() instanceof VirtualRegister && ins.getDefRegister().asmUses.size() == 0) {
						ins.removeFromList();
						changed = true;
					}
				}
			}
		}
		return changed;
	}

	void debug(String s){
		System.err.println(s);
	}
}
