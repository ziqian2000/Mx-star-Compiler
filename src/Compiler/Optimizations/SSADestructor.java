package Compiler.Optimizations;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Immediate;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;

public class SSADestructor {

	IR ir;
	Map<BasicBlock, List<Pair<Operand, VirtualRegister>>> parallelCopyMap = new LinkedHashMap<>();

	public SSADestructor(IR ir){
		this.ir = ir;
	}

	public void run(){
		// always assume preBBList, sucBBList, BBList have been correctly computed
		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			func.makePreOrderBBList();
			removePhi(func);
			func.makePreOrderBBList();
			parallelCopySequentialization(func, false);
//			naiveParallelCopySequentialization(func);
		}
		ir.setSSAForm(false);
	}

	private void removePhi(Function func){
		func.getPreOrderBBList().forEach(BB -> parallelCopyMap.put(BB, new LinkedList<>()));
		Map<BasicBlock, List<Pair<Operand, VirtualRegister>>> pathMap = new LinkedHashMap<>();
		for(BasicBlock BB : func.getPreOrderBBList()){
			pathMap.clear();
			for(BasicBlock preBB : BB.getPreBBList()){

				if(preBB.getSucBBList().size() > 1){ // critical edge splitting
					BasicBlock newBB = new BasicBlock("splitting");

					((Branch)preBB.getTailIns()).replaceTargetBB(BB, newBB);
					newBB.appendLastInst(new Jump(BB));

					parallelCopyMap.put(newBB, new LinkedList<>());
					pathMap.put(preBB, parallelCopyMap.get(newBB));
				}
				else{
					pathMap.put(preBB, parallelCopyMap.get(preBB));
				}
			}
			// replace phi with copy
			IRIns ins = BB.getHeadIns();
			for(; ins instanceof Phi; ins = ins.getNextIns()){
				for(Map.Entry<BasicBlock, Operand> entry : ((Phi) ins).getPath().entrySet()){
					BasicBlock fromBB = entry.getKey();
					Operand fromOpr = entry.getValue();
					pathMap.get(fromBB).add(new Pair<>(fromOpr == null ? new Immediate(0) : fromOpr, ((Phi) ins).getDst()));
				}
			}
			// remove phi
			BB.setHeadIns(ins);
			if(ins != null) ins.setPrevIns(null);
		}

	}

	/**
	 * 	Algorithm 17.6 Parallel copy sequentialization algorithm in SSA book
	 * 	consider the graph as a ``Huan Tao Shu'' (I don't know its corresponding English name...)
	 */

	private void naiveParallelCopySequentialization(Function function){
		for(BasicBlock BB : function.getPreOrderBBList()) {
			parallelCopyMap.get(BB).forEach(pc -> BB.getTailIns().prependIns(new Move(pc.a, pc.b)));
		}
	}

	private void parallelCopySequentialization(Function function, boolean assertNoCycle){
		Map<VirtualRegister, VirtualRegister> pred = new LinkedHashMap<>();
		Map<VirtualRegister, VirtualRegister> loc = new LinkedHashMap<>();
		Queue<VirtualRegister> ready = new LinkedList<>();
		Queue<VirtualRegister> to_do = new LinkedList<>();
		for(BasicBlock BB : function.getPreOrderBBList()){
			ready.clear();
			to_do.clear();
			pred.clear();
			loc.clear();
			VirtualRegister n = new I32Value("extra");
			pred.put(n, null);
			parallelCopyMap.get(BB).forEach(pc -> {
				if(pc.a instanceof VirtualRegister){
					loc.put(pc.b, null);
					pred.put((VirtualRegister)pc.a, null);
				}
			});
			parallelCopyMap.get(BB).forEach(pc -> {
				if(pc.a instanceof VirtualRegister){
					loc.put((VirtualRegister)pc.a, (VirtualRegister)pc.a);
					pred.put(pc.b, (VirtualRegister)pc.a);
					to_do.add(pc.b);
				}
			});
			parallelCopyMap.get(BB).forEach(pc -> {
				if(pc.a instanceof VirtualRegister){
					if(loc.get(pc.b) == null){
						ready.add(pc.b);
					}
				}
			});
			while(!to_do.isEmpty()){
				while(!ready.isEmpty()){
					VirtualRegister b = ready.poll();
					VirtualRegister a = pred.get(b);
					VirtualRegister c = loc.get(a);
					BB.getTailIns().prependIns(new Move(c, b));
					loc.put(a, b);
					if(a == c && pred.get(a) != null) ready.add(a);
				}
				VirtualRegister b = to_do.poll();
				if(b == loc.get(pred.get(b))){
					if(!assertNoCycle) BB.getTailIns().prependIns(new Move(b, n));
					loc.put(b, n);
					ready.add(b);
				}
			}

			parallelCopyMap.get(BB).forEach(pc -> {
				if(!(pc.a instanceof VirtualRegister)){
					BB.getTailIns().prependIns(new Move(pc.a, pc.b));
				}
			});
		}
	}

	void debug(String s){
		System.err.println(s);
	}

}
