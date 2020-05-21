package Compiler.Codegen;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.Assembly.AsmFunction;

import java.util.*;

/**
 * implement some common passes
 */

public abstract class AsmPass {
	/**
	 * def-use chain
	 */
	private void cleanDefUse(AsmFunction func){
		for(var BB : func.getPreOrderBBList()){
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(ins.getDefRegister() != null){
					ins.getDefRegister().asmCleanDefUse();
				}
				for(var useReg : ins.getUseRegister()){
					if(useReg == null) continue;
					useReg.asmCleanDefUse();
				}
			}
		}
	}
	public void computeDefUseChain(AsmFunction func){
		cleanDefUse(func);
		for(var BB : func.getPreOrderBBList()){
			for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
				if(ins.getDefRegister() != null){
					ins.getDefRegister().asmDefs.add(ins);
				}
				for(var useReg : ins.getUseRegister()){
					if(useReg == null) continue;
					useReg.asmUses.add(ins);
				}
			}
		}
	}
	/**
	 * Dominator tree
	 */
	private AsmBasicBlock ancestorWithLowestSemi(AsmBasicBlock v){
		AsmBasicBlock a = v.ancestor;
		if(a.ancestor != null){
			AsmBasicBlock b = ancestorWithLowestSemi(a);
			v.ancestor = a.ancestor;
			if(b.semiDom.dfn < v.best.semiDom.dfn)
				v.best = b;
		}
		return v.best;
	}
	private void link(AsmBasicBlock p, AsmBasicBlock n){
		n.ancestor = p;
		n.best = n;
	}
	public void constructDominatorTree(AsmFunction function)
	{
		// fake dfs
		function.makePreOrderBBList();
		List<AsmBasicBlock> preOrderBBList = function.getPreOrderBBList();

		for(int i = 0; i < preOrderBBList.size(); i++) {
			AsmBasicBlock n = preOrderBBList.get(i);
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
			AsmBasicBlock n = preOrderBBList.get(i);
			AsmBasicBlock p = n.getParent();
			AsmBasicBlock s = p;
			for(AsmBasicBlock v : n.getPreBBList()){
				AsmBasicBlock ss = v.dfn <= n.dfn
						? v
						: ancestorWithLowestSemi(v).semiDom;
				if(ss.dfn < s.dfn)
					s = ss;
			}
			n.semiDom = s;
			s.bucket.add(n);
			link(p, n);
			for(AsmBasicBlock v : p.bucket){
				AsmBasicBlock y = ancestorWithLowestSemi(v);
				if(y.semiDom == v.semiDom) v.iDom = p;
				else v.sameDom = y;
			}
			p.bucket.clear();
		}
		for(int i = 1; i < preOrderBBList.size(); i++){
			AsmBasicBlock n = preOrderBBList.get(i);
			if(n.sameDom != null)
				n.iDom = n.sameDom.iDom;
		}

		for(AsmBasicBlock BB : preOrderBBList) BB.iDomChildren.clear();
		for(AsmBasicBlock BB : preOrderBBList) if(BB.iDom != null){
			BB.iDom.iDomChildren.add(BB);
		}
	}

	/**
	 * loop analysis
	 */
	protected Set<AsmBasicBlock> loopHeaders;
	protected Map<AsmBasicBlock, Set<AsmBasicBlock>> loopBackers;
	protected Map<AsmBasicBlock, Set<AsmBasicBlock>> belongingLoopHeaders;
	protected Map<AsmBasicBlock, Set<AsmBasicBlock>> loopGroups;
	protected Map<AsmBasicBlock, Set<AsmBasicBlock>> loopExits;
	private boolean isDom(AsmBasicBlock a, AsmBasicBlock b){
		// check if a dominates b
		if(a == b) return true;
		while(b.iDom != null){
			b = b.iDom;
			if(a == b) return true;
		}
		return false;
	}
	public void computeLoopInfo(AsmFunction func){
		constructDominatorTree(func);
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
		Stack<AsmBasicBlock> workList = new Stack<>();
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
}
