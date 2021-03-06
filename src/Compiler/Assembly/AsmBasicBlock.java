package Compiler.Assembly;

import Compiler.Assembly.Instr.AsmBranch;
import Compiler.Assembly.Instr.AsmIns;
import Compiler.Assembly.Instr.AsmJump;
import Compiler.IR.Operand.Register;

import java.util.*;

public class AsmBasicBlock {

	private String identifier;
	private AsmIns headIns, tailIns;

	public String getIdentifier() {
		return identifier;
	}

	// DFS
	private AsmBasicBlock parent;
	private List<AsmBasicBlock> preBBList; // implemented in function
	private List<AsmBasicBlock> sucBBList; // implemented in basic block (here)

	//  liveness analysis
	private Set<Register> use = new LinkedHashSet<>();
	private Set<Register> def = new LinkedHashSet<>();
	private Set<Register> liveIn = new LinkedHashSet<>();
	private Set<Register> liveOut = new LinkedHashSet<>();

	// dominator tree, dominance frontier
	public int dfn;
	public List<AsmBasicBlock> bucket = new ArrayList<>();
	public AsmBasicBlock ancestor;
	public AsmBasicBlock best;

	public AsmBasicBlock semiDom;
	public AsmBasicBlock sameDom;
	public AsmBasicBlock iDom; 		// i.e. parent in dominator tree
	public List<AsmBasicBlock> iDomChildren = new ArrayList<>();

	public AsmBasicBlock(String identifier){
		this.identifier = identifier;
	}

	public void appendInst(AsmIns inst){
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

	public Set<Register> getDef() {
		return def;
	}

	public Set<Register> getUse() {
		return use;
	}

	public Set<Register> getLiveIn() {
		return liveIn;
	}

	public Set<Register> getLiveOut() {
		return liveOut;
	}

	public void setLiveIn(Set<Register> liveIn) {
		this.liveIn = liveIn;
	}

	public void setLiveOut(Set<Register> liveOut) {
		this.liveOut = liveOut;
	}

	public AsmIns getHeadIns() {
		return headIns;
	}

	public void setHeadIns(AsmIns headIns) {
		this.headIns = headIns;
	}

	public AsmIns getTailIns() {
		return tailIns;
	}

	public void setTailIns(AsmIns tailIns) {
		this.tailIns = tailIns;
	}

	public boolean isEmpty(){ return headIns == null; }

	public void setParent(AsmBasicBlock parent) {
		this.parent = parent;
	}

	public AsmBasicBlock getParent() {
		return parent;
	}


	public void mergeBB(AsmBasicBlock BB){ // (this -> BB) => (this)
		// deal with predecessor, successor relation
		sucBBList = BB.getSucBBList();
		for(AsmBasicBlock sucBB : BB.getSucBBList()){
			sucBB.getPreBBList().remove(BB);
			sucBB.getPreBBList().add(this);
		}
		// merge two blocks
		if(getHeadIns() == getTailIns()){ // only one instruction
			setHeadIns(BB.getHeadIns());
		}
		else{
			getTailIns().removeFromList();
			assert !(getTailIns() instanceof AsmJump) && !(getTailIns() instanceof AsmBranch);
			getTailIns().setNextIns(BB.getHeadIns());
			BB.getHeadIns().setPrevIns(getTailIns());
		}
		setTailIns(BB.getTailIns());
		for(AsmIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns())
			ins.setBelongBB(this);
	}

	// pre & suc BB list
	// compute in order : suc BB -> dfs -> pre BB

	public List<AsmBasicBlock> getPreBBList() {
		return preBBList;
	}

	public void setPreBBList(List<AsmBasicBlock> preBBList) {
		this.preBBList = preBBList;
	}

	public void makeSucBBList(){
		sucBBList = new ArrayList<>();
		if(tailIns != null) resolveSucBBList(tailIns);
		if(tailIns != null && tailIns.getPrevIns() != null) resolveSucBBList(tailIns.getPrevIns());
		// unique
		HashSet<AsmBasicBlock> tmp = new LinkedHashSet<>(sucBBList);
		sucBBList = new ArrayList<>(tmp);
	}

	public void resolveSucBBList(AsmIns ins){
		if(ins instanceof AsmBranch) {
			sucBBList.add(((AsmBranch) ins).getBB());
		}
		else if(ins instanceof AsmJump) sucBBList.add(((AsmJump) ins).getBB());
	}

	public List<AsmBasicBlock> getSucBBList() {
		return sucBBList;
	}

}
