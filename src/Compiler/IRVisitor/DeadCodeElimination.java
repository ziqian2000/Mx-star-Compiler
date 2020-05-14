package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.Register;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class DeadCodeElimination extends Pass{

	boolean changed;
	private IR ir;

	public DeadCodeElimination(IR ir){
		this.ir = ir;
	}

	public boolean run() throws FileNotFoundException {
		changed = false;
		for(var func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			func.makePreOrderBBList();
			computeDefUseChain(func);
			constructReverseDominatorTreeAndReverseDominanceFrontier(func);
			mark(func);
			sweep(func);
			func.makePreOrderBBList();
		}
		return changed;
	}

	private void mark(Function func){
		Set<IRIns> worklist = new HashSet<>();
		for(var BB : func.getPreOrderBBList()){
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(isCritical(ins)){
					ins.mark = true;
					worklist.add(ins);
				}
				else ins.mark = false;
			}
		}
		while(!worklist.isEmpty()){
			var ins = worklist.iterator().next();
			worklist.remove(ins);

			for(var useReg : ins.getUseRegister()){
				assert useReg != null || ins instanceof Phi;
				if(useReg == null) continue;
				var defIns = useReg.def;
				if(defIns != null && !defIns.mark){
					defIns.mark = true;
					worklist.add(defIns);
				}
			}

			if(ins instanceof Phi){
				for(var BB : ((Phi) ins).getPath().keySet()){
					var tailIns = BB.getTailIns();
					if(!tailIns.mark){
						tailIns.mark = true;
						worklist.add(tailIns);
					}
				}
			}

			for(var BB : ins.getBelongBB().postDomFront){
				var branchIns = BB.getTailIns();
				assert branchIns instanceof Branch;
				if(!branchIns.mark){
					branchIns.mark = true;
					worklist.add(branchIns);
				}
			}
		}
	}

	private void sweep(Function func){
		for(var BB : func.getPreOrderBBList()){
			IRIns nextIns;
			for(var ins = BB.getHeadIns(); ins != null; ins = nextIns){
				nextIns = ins.getNextIns();
				if(!ins.mark){
					if(ins instanceof Branch){
						changed = true;
						var nearestMarkedBB = getNearestMarkedPostDominator(BB);
						ins.replaceSelfWithAnotherIns(new Jump(nearestMarkedBB));

					}
					else if(!(ins instanceof Jump)){
						changed = true;
						ins.removeFromList();
					}
				}
			}
		}
	}

	private BasicBlock getNearestMarkedPostDominator(BasicBlock BB){
		BB = BB.postIDom;
		while(true){
			assert BB != null;
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(ins.mark) return BB;
			}
			BB = BB.postIDom;
		}
	}

	private boolean isCritical(IRIns ins){
		return ins instanceof Return
			|| ins instanceof Call
			|| ins instanceof Alloc
			|| ins instanceof Load
			|| ins instanceof Store;
	}

	// make a reverse CFG so that reverse dominance frontier and reverse dominator tree can be built easily
	private void constructReverseDominatorTreeAndReverseDominanceFrontier(Function func){
		var ret = func.makeReverseCopy();
		Function reverseFunc = (Function) ret.get(0);
		Map<BasicBlock, BasicBlock> BBMap = (Map<BasicBlock, BasicBlock>) ret.get(1);
		Map<BasicBlock, BasicBlock> reBBMap = (Map<BasicBlock, BasicBlock>) ret.get(2);

		constructDominatorTree(reverseFunc, true);
		constructDominanceFrontier(reverseFunc);

		for(var BB : func.getPreOrderBBList()){
			var counterpartBB = BBMap.get(BB);

			BB.postIDom = reBBMap.get(counterpartBB.iDom);

//			if(reBBMap.containsKey(counterpartBB.iDom))
//				System.err.println(BB.getIdentifier() + " (post dom) " + reBBMap.get(counterpartBB.iDom).getIdentifier());

			BB.postDomFront = new HashSet<>();
			for (BasicBlock b : counterpartBB.domFront) {
				assert reBBMap.containsKey(b);
				BB.postDomFront.add(reBBMap.get(b));
			}
		}

	}
}
