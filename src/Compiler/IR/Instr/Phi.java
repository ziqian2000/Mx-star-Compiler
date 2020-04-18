package Compiler.IR.Instr;

import Compiler.IR.BasicBlock;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.FuckingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Phi extends IRIns {

	private VirtualRegister dst;
	private Map<BasicBlock, VirtualRegister> path;

	public Phi(VirtualRegister dst){
		this.dst = dst;
		path = new HashMap<>();
	}

	public VirtualRegister getDst() {
		return dst;
	}

	public Map<BasicBlock, VirtualRegister> getPath() {
		return path;
	}

	public void removePath(BasicBlock BB){
		path.remove(BB);
		if(path.size() == 1){
			replaceSelfWithAnotherIns(new Move(path.values().iterator().next(), dst));
		}
	}

	public void replacePath(BasicBlock oldBB, BasicBlock newBB){
		if(oldBB == newBB) return;
		assert !path.containsKey(newBB);
		path.put(newBB, path.get(oldBB));
		path.remove(oldBB);
	}

	@Override
	public List<VirtualRegister> getUseRegister() {
		throw new FuckingException("Don't call me");
	}

	@Override
	public VirtualRegister getDefRegister() {
		return dst;
	}

	@Override
	public List<Operand> getOperands() {
		throw new FuckingException("Don't call me");
	}

	@Override
	public List<BasicBlock> getBBs() {
		throw new FuckingException("Don't call me");
	}

	@Override
	public IRIns copySelf(List<Operand> opr, List<BasicBlock> BB) {
		throw new FuckingException("Don't call me");
	}

	@Override
	public void setDefRegister(VirtualRegister newDefRegister) {
		dst = newDefRegister;
	}

	public void accept(IRVisitor irVisitor){
		irVisitor.visit(this);
	}
}
