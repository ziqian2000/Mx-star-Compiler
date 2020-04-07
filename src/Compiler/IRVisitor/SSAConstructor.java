package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;

import java.util.*;

public class SSAConstructor {

	IR ir;

	public SSAConstructor(IR ir){
		this.ir = ir;
	}

	public void run(){
		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			makeDominatorTree(func);
			makeDominanceFrontier(func);
		}
	}

	// dominance frontier computation
	Map<BasicBlock, Set<BasicBlock>> domFront = new HashMap<>();
	// todo : can be optimized, it take log time to calculate, store them in BB class
	public void makeDominanceFrontier(Function func){
		List<BasicBlock> BBList = func.getBBList();
		BBList.forEach(BB -> domFront.put(BB, new HashSet<>()));
		for(BasicBlock BB : BBList){
			// todo : why to check predecessor >= 2?
			for(BasicBlock predecessor : BB.getPreBBList()){
				BasicBlock x = predecessor;
				while(x != iDom.get(BB)){
					domFront.get(x).add(BB);
					x = iDom.get(x);
				}
			}
		}
	}

	// dominator tree construction
	// todo : can be optimized, it take log time to calculate, store them in BB class
	Map<BasicBlock, Integer> 	dfn = new HashMap<>();
	Map<Integer, BasicBlock>	vertex = new HashMap<>();
	Map<BasicBlock, List<BasicBlock>> bucket = new HashMap<>();

	Map<BasicBlock, BasicBlock> ancestor = new HashMap<>();
	Map<BasicBlock, BasicBlock> best = new HashMap<>();

	Map<BasicBlock, BasicBlock>	semiDom = new HashMap<>();
	Map<BasicBlock, BasicBlock> iDom = new HashMap<>();
	Map<BasicBlock, BasicBlock> sameDom = new HashMap<>();

	private BasicBlock ancestorWithLowestSemi(BasicBlock v){
		BasicBlock a = ancestor.get(v);
		if(ancestor.get(a) != null){
			BasicBlock b = ancestorWithLowestSemi(a);
			ancestor.put(v, ancestor.get(a));
			if(dfn.get(semiDom.get(b)) < dfn.get(semiDom.get(best.get(v))))
				best.put(v, b);
		}
		return best.get(v);
	}

	private void link(BasicBlock p, BasicBlock n){
		ancestor.put(n, p);
		best.put(n, n);
	}

	private void makeDominatorTree(Function function)
	{
		// fake dfs
		function.makeBBList();
		List<BasicBlock> preOrderBBList = function.getBBList();

		dfn.clear();
		vertex.clear();
		for(int i = 0; i < preOrderBBList.size(); i++) {
			BasicBlock n = preOrderBBList.get(i);
			dfn.put(n, i + 1);
			vertex.put(i + 1, n);
		}

		// init
		preOrderBBList.forEach(BB -> {
			bucket.put(BB, new ArrayList<>());
			semiDom.put(BB, null);
			ancestor.put(BB, null);
			iDom.put(BB, null);
			sameDom.put(BB, null);
		});
		for(int i = preOrderBBList.size() - 1; i >= 1; i--){
			BasicBlock n = preOrderBBList.get(i);
			BasicBlock p = n.getParent();
			BasicBlock s = p;
			for(BasicBlock v : n.getPreBBList()){
				BasicBlock ss = dfn.get(v) <= dfn.get(n)
						? v
						: semiDom.get(ancestorWithLowestSemi(v));
				if(dfn.get(ss) < dfn.get(s))
					s = ss;
			}
			semiDom.put(n, s);
			bucket.get(s).add(n);
			link(p, n);
			for(BasicBlock v : bucket.get(p)){
				BasicBlock y = ancestorWithLowestSemi(v);
				if(semiDom.get(y) == semiDom.get(v)) iDom.put(v, p);
				else sameDom.put(v, y);
			}
			bucket.get(p).clear();
		}
		for(int i = 1; i < preOrderBBList.size(); i++){
			BasicBlock n = preOrderBBList.get(i);
			if(sameDom.get(n) != null)
				iDom.put(n, iDom.get(sameDom.get(n)));
		}

	}

}
