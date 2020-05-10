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
	private Map<Register, IRIns> def;
	private Map<Register, Set<IRIns>> use;

	public DeadCodeElimination(IR ir){
		this.ir = ir;
	}

	public boolean run() throws FileNotFoundException {
		changed = false;
		for(var func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			func.makePreOrderBBList();
			computeDefUseChain(func);
			constructReverseDominatorTreeAndReverseDominanceFrontier(func);
			new IRPrinter().run(ir, new PrintStream("ir.txt"));
			mark(func);
			sweep(func);
		}
		return changed;
	}

	private void computeDefUseChain(Function func){
		def = new HashMap<>();
		use = new HashMap<>();
		for(var BB : func.getPreOrderBBList()){
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(ins.getDefRegister() != null){
					def.put(ins.getDefRegister(), ins);
					use.computeIfAbsent(ins.getDefRegister(), k -> new HashSet<>());
				}
				for(var useReg : ins.getUseRegister()){
					use.computeIfAbsent(useReg, k -> new HashSet<>());
					use.get(useReg).add(ins);
				}
			}
		}
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
				var defIns = def.get(useReg);
				if(defIns != null && !defIns.mark){
					defIns.mark = true;
					worklist.add(defIns);
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
						ins.prependIns(new Jump(getNearestMarkedPostDominator(BB)));
						ins.removeFromList();
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
		while(true){
			assert BB != null;
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(ins.mark) return BB;
			}
			BB = BB.postIDom;
		}
	}

	private boolean isCritical(IRIns ins){
		// todo : add more ins type here?
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

			BB.postDomFront = new HashSet<>();
			for (BasicBlock b : counterpartBB.domFront) {
				assert reBBMap.containsKey(b);
				BB.postDomFront.add(reBBMap.get(b));
			}
		}

	}
}
