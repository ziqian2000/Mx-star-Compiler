package Compiler.IR;

import Compiler.IR.Instr.Branch;
import Compiler.IR.Instr.IRIns;
import Compiler.IR.Instr.Jump;
import Compiler.IR.Instr.Return;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicBlock {

	private String identifier;
	private IRIns headIns, tailIns;
	private boolean terminated;

	// DFS
	private BasicBlock parent;
	private List<BasicBlock> preBBList; // implemented in function
	private List<BasicBlock> sucBBList; // implemented in basic block (here)

	public BasicBlock(){

	}
	public BasicBlock(String identifier){
		this.identifier = identifier;
	}

	public void addInst(IRIns inst){
		if(!terminated)	sudoAddInst(inst);
	}

	public void addLastInst(IRIns inst){
		addInst(inst);
		setTerminated(true);
	}

	public void sudoAddInst(IRIns inst){
		if(isEmpty()){
			headIns = tailIns = inst;
			inst.setPrevIns(null);
			inst.setNextIns(null);
		}
		else {
			tailIns.setNextIns(inst);
			inst.setPrevIns(tailIns);
			inst.setNextIns(null);
			tailIns = inst;
		}
		inst.setBelongBB(this);
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public boolean isTerminated(){
		return terminated;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public IRIns getHeadIns() {
		return headIns;
	}

	public void setHeadIns(IRIns headIns) {
		this.headIns = headIns;
	}

	public IRIns getTailIns() {
		return tailIns;
	}

	public void setTailIns(IRIns tailIns) {
		this.tailIns = tailIns;
	}

	public boolean isEmpty(){ return headIns == null; }

	public void setParent(BasicBlock parent) {
		this.parent = parent;
	}

	public BasicBlock getParent() {
		return parent;
	}

	// pre & suc BB list
	// compute in order : suc BB -> dfs -> pre BB

	public List<BasicBlock> getPreBBList() {
		return preBBList;
	}

	public void setPreBBList(List<BasicBlock> preBBList) {
		this.preBBList = preBBList;
	}

	public void makeSucBBList(){
		sucBBList = new ArrayList<>();
		if(tailIns instanceof Branch) sucBBList.addAll(Arrays.asList(((Branch)tailIns).getThenBB(), ((Branch) tailIns).getElseBB()));
		else if(tailIns instanceof Jump) sucBBList.add(((Jump) tailIns).getBB());
		else if(!(tailIns instanceof Return)) throw new FuckingException("basic block terminated by something strange");
	}

	public List<BasicBlock> getSucBBList() {
		return sucBBList;
	}
}
