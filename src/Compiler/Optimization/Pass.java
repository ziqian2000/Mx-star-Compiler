package Compiler.Optimization;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.Instr.Phi;

import java.util.*;

/**
 * implement some common passes
 */

public abstract class Pass {

	/**
	 * def-use chain
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
					useReg.use.add(ins);
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
			// todo : why to check predecessor >= 2?
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
