package Compiler.IRVisitor.Optimizations;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.Instr.Phi;

import java.util.*;

/**
 * implement some common passes
 */

public abstract class Pass {

	/**
	 * loop analysis
	 */
	protected Set<BasicBlock> loopHeaders;
	protected Map<BasicBlock, Set<BasicBlock>> loopBackers;
	protected Map<BasicBlock, Set<BasicBlock>> belongingLoopHeaders;
	protected Map<BasicBlock, Set<BasicBlock>> loopGroups;
	protected Map<BasicBlock, Set<BasicBlock>> loopExits;
	private boolean isDom(BasicBlock a, BasicBlock b){
		// check if a dominates b
		if(a == b) return true;
		while(b.iDom != null){
			b = b.iDom;
			if(a == b) return true;
		}
		return false;
	}
	public void computeLoopInfo(Function func){
		constructDominatorTree(func, false);
		// init
		loopHeaders = new LinkedHashSet<>();
		loopBackers = new LinkedHashMap<>();
		belongingLoopHeaders = new LinkedHashMap<>();
		loopGroups = new LinkedHashMap<>();
		loopExits = new LinkedHashMap<>();
		for(var BB : func.getPreOrderBBList()){
			loopBackers.put(BB, new LinkedHashSet<>());
			belongingLoopHeaders.put(BB, new LinkedHashSet<>());
			loopGroups.put(BB, new LinkedHashSet<>());
			loopExits.put(BB, new LinkedHashSet<>());
		}
		// find all loop headers and backers
		for(var backer : func.getPreOrderBBList()){
			for(var header : backer.getSucBBList()){
				if(isDom(header, backer)){
					loopHeaders.add(header);
					loopBackers.get(header).add(backer);
				}
			}
		}
		// find loop body / make loop group
		Stack<BasicBlock> workList = new Stack<>();
		for(var header : loopHeaders) {
			workList.clear();
			loopGroups.get(header).add(header);
			belongingLoopHeaders.get(header).add(header);
			for (var backer : loopBackers.get(header)) {

				workList.add(backer);
				loopGroups.get(header).add(backer);
				belongingLoopHeaders.get(backer).add(header);

				while (!workList.isEmpty()) {
					var BB = workList.pop();
					for (var pred : BB.getPreBBList()) {
						if (!loopGroups.get(header).contains(pred)) {

							workList.add(pred);
							loopGroups.get(header).add(pred);
							belongingLoopHeaders.get(pred).add(header);
						}
					}
				}
			}
		}
		// find loop exit
		for(var header : loopHeaders){
			for(var bodyBB : loopGroups.get(header)){
				for(var sucBB : bodyBB.getSucBBList()){
					if(!loopGroups.get(header).contains(sucBB)){
						loopExits.get(header).add(bodyBB);
					}
				}
			}
		}
	}
	/**
	 * def-use chain, in SSA form
	 */
	private void cleanDefUse(Function func){
		for(var BB : func.getPreOrderBBList()){
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(ins.getDefRegister() != null){
					ins.getDefRegister().cleanDefUse();
				}
				for(var useReg : ins.getUseRegister()){
					assert useReg != null || ins instanceof Phi;
					if(useReg == null) continue;
					useReg.cleanDefUse();
				}
			}
		}
	}
	public void computeDefUseChain(Function func){
		cleanDefUse(func);
		for(var BB : func.getPreOrderBBList()){
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(ins.getDefRegister() != null){
					ins.getDefRegister().def = ins;
				}
				for(var useReg : ins.getUseRegister()){
					assert useReg != null || ins instanceof Phi;
					if(useReg == null) continue;
					useReg.uses.add(ins);
				}
			}
		}
	}

	/**
	 * dominance frontier computation
	 */
	public void constructDominanceFrontier(Function func){
		List<BasicBlock> BBList = func.getPreOrderBBList();
		BBList.forEach(BB -> BB.domFront.clear());
		for(BasicBlock BB : BBList){
			for(BasicBlock predecessor : BB.getPreBBList()){
				BasicBlock x = predecessor;
				while(x != BB.iDom){
					x.domFront.add(BB);
					x = x.iDom;
				}
			}
		}
	}
	/**
	 * Dominator tree
	 */
	private BasicBlock ancestorWithLowestSemi(BasicBlock v){
		BasicBlock a = v.ancestor;
		if(a.ancestor != null){
			BasicBlock b = ancestorWithLowestSemi(a);
			v.ancestor = a.ancestor;
			if(b.semiDom.dfn < v.best.semiDom.dfn)
				v.best = b;
		}
		return v.best;
	}
	private void link(BasicBlock p, BasicBlock n){
		n.ancestor = p;
		n.best = n;
	}
	public void constructDominatorTree(Function function, boolean postOrder)
	{
		// fake dfs
		if(postOrder) function.reverseMakePreOrderBBList();
		else function.makePreOrderBBList();
		List<BasicBlock> preOrderBBList = function.getPreOrderBBList();

		for(int i = 0; i < preOrderBBList.size(); i++) {
			BasicBlock n = preOrderBBList.get(i);
			n.dfn = i + 1;
		}

		// init
		preOrderBBList.forEach(BB -> {
			BB.bucket.clear();
			BB.semiDom = null;
			BB.ancestor = null;
			BB.iDom = null;
			BB.sameDom = null;
		});
		for(int i = preOrderBBList.size() - 1; i >= 1; i--){
			BasicBlock n = preOrderBBList.get(i);
			BasicBlock p = n.getParent();
			BasicBlock s = p;
			for(BasicBlock v : n.getPreBBList()){
				BasicBlock ss = v.dfn <= n.dfn
						? v
						: ancestorWithLowestSemi(v).semiDom;
				if(ss.dfn < s.dfn)
					s = ss;
			}
			n.semiDom = s;
			s.bucket.add(n);
			link(p, n);
			for(BasicBlock v : p.bucket){
				BasicBlock y = ancestorWithLowestSemi(v);
				if(y.semiDom == v.semiDom) v.iDom = p;
				else v.sameDom = y;
			}
			p.bucket.clear();
		}
		for(int i = 1; i < preOrderBBList.size(); i++){
			BasicBlock n = preOrderBBList.get(i);
			if(n.sameDom != null)
				n.iDom = n.sameDom.iDom;
		}

		for(BasicBlock BB : preOrderBBList) BB.iDomChildren.clear();
		for(BasicBlock BB : preOrderBBList) if(BB.iDom != null){
			BB.iDom.iDomChildren.add(BB);
		}
	}

}
