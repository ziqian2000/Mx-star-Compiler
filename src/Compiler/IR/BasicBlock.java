package Compiler.IR;

import Compiler.IR.Instr.*;
import Compiler.Utils.FuckingException;

import java.util.*;

public class BasicBlock {

	private static int unnamedCnt = 0;
	private String identifier;
	private IRIns headIns, tailIns;
	private boolean terminated;

	// DFS
	private BasicBlock parent;
	public int postOrderIdx;
	public List<BasicBlock> preBBList; // implemented in function
	public List<BasicBlock> sucBBList; // implemented in basic block (here)

	// dominator tree, dominance frontier
	public int dfn;
	public List<BasicBlock> bucket = new ArrayList<>();
	public BasicBlock ancestor;
	public BasicBlock best;

	public BasicBlock semiDom;
	public BasicBlock sameDom;
	public BasicBlock iDom; 		// i.e. parent in dominator tree
	public List<BasicBlock> iDomChildren = new ArrayList<>();
	public Set<BasicBlock> domFront = new LinkedHashSet<>();
	// reverse version
	public BasicBlock postIDom;
	public Set<BasicBlock> postDomFront;



	public BasicBlock(){
		this.identifier = "unnamed_" + unnamedCnt;
		unnamedCnt += 1;
	}
	public BasicBlock(String identifier){
		this.identifier = identifier;
	}

	public void appendInst(IRIns inst){
		if(!terminated)	sudoAppendInst(inst);
	}

	public void appendLastInst(IRIns inst){
		appendInst(inst);
		setTerminated(true);
	}

	public void sudoAppendInst(IRIns inst){
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

	public void sudoPrependInst(IRIns inst){
		if(isEmpty()){
			headIns = tailIns = inst;
			inst.setPrevIns(null);
			inst.setNextIns(null);
		}
		else {
			headIns.setPrevIns(inst);
			inst.setNextIns(headIns);
			inst.setPrevIns(null);
			headIns = inst;
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

	// SSA opt
	public void removeBBInPhi(BasicBlock BB){
		for(IRIns ins = getHeadIns(), nextIns; ins instanceof Phi; ins = nextIns){
			nextIns = ins.getNextIns();
			((Phi) ins).removePath(BB);
		}
	}

	public void mergeBB(BasicBlock BB){ // (this -> BB) => (this)
		// deal with PHI
		for(BasicBlock sucBB : BB.getSucBBList()){
			for(IRIns ins = sucBB.getHeadIns(); ins instanceof Phi; ins = ins.getNextIns()){
				((Phi) ins).replacePath(BB, this);
			}
		}
		// deal with predecessor, successor relation
		sucBBList = BB.getSucBBList();
		for(BasicBlock sucBB : BB.getSucBBList()){
			sucBB.getPreBBList().remove(BB);
			sucBB.getPreBBList().add(BB);
		}
		// merge two blocks
		if(getHeadIns() == getTailIns()){ // only one instruction
			setHeadIns(BB.getHeadIns());
		}
		else{
			getTailIns().removeFromList();
			getTailIns().setNextIns(BB.getHeadIns());
			BB.getHeadIns().setPrevIns(getTailIns());
		}
		setTailIns(BB.getTailIns());
		for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns())
			ins.setBelongBB(this);
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
		assert tailIns != null;
		sucBBList = new ArrayList<>();
		if(tailIns instanceof Branch) {
			if(((Branch) tailIns).getThenBB() != ((Branch) tailIns).getElseBB())
				sucBBList.addAll(Arrays.asList(((Branch)tailIns).getThenBB(), ((Branch) tailIns).getElseBB()));
			else
				sucBBList.add(((Branch) tailIns).getThenBB());
		}
		else if(tailIns instanceof Jump) sucBBList.add(((Jump) tailIns).getBB());
		else if(!(tailIns instanceof Return)) throw new FuckingException("basic block terminated by something strange");
	}

	public List<BasicBlock> getSucBBList() {
		return sucBBList;
	}
}
