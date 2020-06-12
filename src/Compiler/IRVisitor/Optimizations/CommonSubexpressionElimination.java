package Compiler.IRVisitor.Optimizations;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.IRAssistant;

import java.util.*;

/**
 * dominator-based value numbering
 */

public class CommonSubexpressionElimination extends Pass{

	private boolean changed;
	private IR ir;

	private Map<BinaryExpr, VirtualRegister> binaryHashTable = new LinkedHashMap<>();
	private Map<UnaryExpr, VirtualRegister> unaryHashTable = new LinkedHashMap<>();
	private Map<Map<BasicBlock, Operand>, VirtualRegister> phiHashTable = new LinkedHashMap<>();
	private Map<VirtualRegister, VirtualRegister> valueNumber = new LinkedHashMap<>();

	public CommonSubexpressionElimination(IR ir){
		this.ir = ir;
	}

	public boolean run(){
		changed = false;
		for(var func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			func.makePreOrderBBList();
			func.computePostOrderIdx();
			constructDominatorTree(func, false);
			init(func);
			visit(func.getEntryBB());
		}
		return changed;
	}

	private void init(Function func){
		valueNumber.clear();
		for(VirtualRegister global : ir.getGlobalVarList()) valueNumber.put(global, global);
		for(VirtualRegister args : func.getParaList()) valueNumber.put(args, args);
		if(func.getObj() != null) valueNumber.put(func.getObj(), func.getObj());
	}

	private void visit(BasicBlock BB){

		List<BinaryExpr> allocatedBinaryExpr = new ArrayList<>();
		List<UnaryExpr> allocatedUnaryExpr = new ArrayList<>();
		List<VirtualRegister> allocatedValueNumber = new ArrayList<>();

		IRIns ins, nextIns;
		// ================================================================ process PHI
		for(ins = BB.getHeadIns(); ins instanceof Phi; ins = nextIns){
			nextIns = ins.getNextIns();
			// check if meaningless
			Operand v = ((Phi) ins).getPath().values().iterator().next();
			boolean meaningless = true;
			for(var value : ((Phi) ins).getPath().values()){
				if(value != v){
					meaningless = false;
					break;
				}
			}
			if(meaningless && v instanceof VirtualRegister){ // the situation where all arguments are same constants imm are handled in SCCP
				valueNumber.put(((Phi) ins).getDst(), (VirtualRegister) v);
				allocatedValueNumber.add(((Phi) ins).getDst());

				ins.removeFromList();
				changed = true;
			}
			else{
				// check if redundant
				var tmp = phiHashTable.get(((Phi) ins).getPath());
				if(tmp != null){
					valueNumber.put(((Phi) ins).getDst(), tmp);
					allocatedValueNumber.add(((Phi) ins).getDst());

					ins.removeFromList();
					changed = true;
				}
				else{
					// neither meaningless nor redundant
					valueNumber.put(((Phi) ins).getDst(), ((Phi) ins).getDst());
					allocatedValueNumber.add(((Phi) ins).getDst());

					phiHashTable.put(((Phi) ins).getPath(), ((Phi) ins).getDst());
				}
			}
		}
		// deallocate all PHI with BB
		phiHashTable.clear();

		// ================================================================ process assignments
		for(; ins != null; ins = nextIns){
			assert !(ins instanceof Phi);
			nextIns = ins.getNextIns();

			// rewrite use registers with value number
			var useRegs = ins.getUseRegister();
			for(var useReg : useRegs) {
				if(valueNumber.get(useReg) != useReg) {
					assert valueNumber.get(useReg) != null;
					ins.replaceUseOpr(useReg, valueNumber.get(useReg));
				}
			}

			if(ins instanceof Binary){
				var expr = new BinaryExpr(((Binary) ins).getLhs(), ((Binary) ins).getRhs(), ((Binary) ins).getOp());
				var tmp = binaryHashTable.get(expr);
				if(tmp != null){
					valueNumber.put((VirtualRegister) ((Binary) ins).getDst(), tmp);
					allocatedValueNumber.add((VirtualRegister) ((Binary) ins).getDst());

					ins.removeFromList();
					changed = true;
				}
				else{
					valueNumber.put((VirtualRegister) ((Binary) ins).getDst(), (VirtualRegister) ((Binary) ins).getDst());
					allocatedValueNumber.add((VirtualRegister) ((Binary) ins).getDst());

					binaryHashTable.put(expr, (VirtualRegister) ((Binary) ins).getDst());
					allocatedBinaryExpr.add(expr);

					var commutedOp = IRAssistant.commute(((Binary) ins).getOp());
					if(commutedOp != null){
						var commutedExpr = new BinaryExpr(((Binary) ins).getRhs(), ((Binary) ins).getLhs(), commutedOp);

						binaryHashTable.put(commutedExpr, (VirtualRegister) ((Binary) ins).getDst());
						allocatedBinaryExpr.add(commutedExpr);
					}
				}
			}
			else if(ins instanceof Unary){
				var expr = new UnaryExpr(((Unary) ins).getOpr(), ((Unary) ins).getOp());
				var tmp = unaryHashTable.get(expr);
				if(tmp != null){
					valueNumber.put((VirtualRegister) ((Unary) ins).getDst(), tmp);
					allocatedValueNumber.add((VirtualRegister) ((Unary) ins).getDst());

					ins.removeFromList();
					changed = true;
				}
				else{
					valueNumber.put((VirtualRegister) ((Unary) ins).getDst(), (VirtualRegister) ((Unary) ins).getDst());
					allocatedValueNumber.add((VirtualRegister) ((Unary) ins).getDst());

					unaryHashTable.put(expr, (VirtualRegister) ((Unary) ins).getDst());
					allocatedUnaryExpr.add(expr);
				}
			}
			else if(ins instanceof Move){
				if(((Move) ins).getSrc() instanceof VirtualRegister){
					valueNumber.put((VirtualRegister) ((Move) ins).getDst(), (VirtualRegister) ((Move) ins).getSrc());
					allocatedValueNumber.add((VirtualRegister) ((Move) ins).getDst());

					ins.removeFromList();
					changed = true;
				}
				else{
					valueNumber.put(ins.getDefRegister(), ins.getDefRegister());
					allocatedValueNumber.add(ins.getDefRegister());
				}
			}
			else{
				if(ins.getDefRegister() != null){
					valueNumber.put(ins.getDefRegister(), ins.getDefRegister());
					allocatedValueNumber.add(ins.getDefRegister());
				}
			}
		}

		// ================================================================ adjust PHI in successors
		for(var sucBB : BB.getSucBBList()){
			for(var phiIns = sucBB.getHeadIns(); phiIns instanceof Phi; phiIns = phiIns.getNextIns()){
				for(var entry : ((Phi) phiIns).getPath().entrySet()){
					var reg = entry.getValue();
					if(!(reg instanceof VirtualRegister)) continue;

					var tmp = valueNumber.get(reg);
					if(tmp != null && tmp != reg){
						((Phi) phiIns).getPath().put(entry.getKey(), tmp);
					}
				}
			}
		}

		// ================================================================ traverse all children
		List<BasicBlock> children = new ArrayList<>(BB.iDomChildren);
		children.sort(new ReversePostOrderComparator());
		for(int i = 1; i < children.size(); i++) // I'm not very familiar with java's sorting algorithm
			assert children.get(i-1).postOrderIdx > children.get(i).postOrderIdx;
		for(var child : children) visit(child);

		// deallocate
		for(var expr : allocatedBinaryExpr) binaryHashTable.remove(expr);
		for(var expr : allocatedUnaryExpr) unaryHashTable.remove(expr);
		for(var reg : allocatedValueNumber) valueNumber.remove(reg);
	}

}

class UnaryExpr{
	Operand u;
	Unary.Op op;

	public UnaryExpr(Operand u, Unary.Op op){
		this.u = u;
		this.op = op;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UnaryExpr expr = (UnaryExpr)o;
		return u == expr.u && op == expr.op;
	}

	@Override
	public int hashCode() {
		return Objects.hash(u, op);
	}
}

class BinaryExpr {
	Operand u, v;
	Binary.Op op;

	public BinaryExpr(Operand u, Operand v, Binary.Op op){
		this.u = u;
		this.v = v;
		this.op = op;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BinaryExpr expr = (BinaryExpr)o;
		return u == expr.u && v == expr.v && op == expr.op;
	}

	@Override
	public int hashCode() {
		return Objects.hash(u, v, op);
	}
}

class ReversePostOrderComparator implements Comparator<BasicBlock>{

	@Override
	public int compare(BasicBlock o1, BasicBlock o2) {
		return o2.postOrderIdx - o1.postOrderIdx;
	}
}