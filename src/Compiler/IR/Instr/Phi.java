package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Immediate;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.IRVisitor;

import java.util.*;

public class Phi extends IRIns {

	private VirtualRegister dst;
	private Map<BasicBlock, Operand> path;

	public Phi(VirtualRegister dst){
		this.dst = dst;
		path = new LinkedHashMap<>();
	}

	public VirtualRegister getDst() {
		return dst;
	}

	public Map<BasicBlock, Operand> getPath() {
		return path;
	}

	public void removePath(BasicBlock BB){
		path.remove(BB);
		checkPathSize();
	}

	public void checkPathSize(){
		if(path.size() == 1) {
			var v = path.values().iterator().next();
			// todo : there can be some useless phi(e.g. t56), and maybe there is better implementation for it
			replaceSelfWithAnotherIns(new Move(Objects.requireNonNullElseGet(v, () -> new Immediate(0)), dst));
		}
	}

	public void setPath(Map<BasicBlock, Operand> path) {
		this.path = path;
	}

	public void replaceBBInPath(BasicBlock oldBB, BasicBlock newBB){
		if(oldBB == newBB) return;
		assert !path.containsKey(newBB);
		path.put(newBB, path.get(oldBB));
		path.remove(oldBB);
	}

	@Override
	public List<VirtualRegister> getUseRegister() {
		List<VirtualRegister> registerList = new ArrayList<>();
		for (var entry : path.entrySet()) {
			Operand opr = entry.getValue();
			if(opr instanceof VirtualRegister) registerList.add((VirtualRegister) opr);
		}
		return registerList;
	}

	@Override
	public VirtualRegister getDefRegister() {
		return dst;
	}

	@Override
	public List<Operand> getOperands() {
		List<Operand> oprList = new ArrayList<>();
		for(var entry : path.entrySet()) oprList.add(entry.getValue());
		oprList.add(dst);
		return oprList;
	}

	@Override
	public List<BasicBlock> getBBs() {
		List<BasicBlock> oprList = new ArrayList<>();
		for(var entry : path.entrySet()) oprList.add(entry.getKey());
		return oprList;
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		var newIns = new Phi((VirtualRegister) opr.get(opr.size() - 1));
		for(int i = 0; i < BB.size(); i++) newIns.path.put(BB.get(i), opr.get(i));
		return newIns;
	}

	@Override
	public void replaceUseOpr(Operand oldOpr, Operand newOpr) {
		for (var entry : path.entrySet()) {
			Operand opr = entry.getValue();
			if(opr == oldOpr) path.put(entry.getKey(), newOpr);
		}
	}

	@Override
	public void replaceDefRegister(VirtualRegister newDefRegister) {
		dst = newDefRegister;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
