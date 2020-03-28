package Compiler.IR;

import Compiler.IR.Instr.IRIns;

import java.util.List;

public class BasicBlock {

	String identifier;
	private IRIns headIns, tailIns;
	private boolean terminated;

	public BasicBlock(){
	}
	public BasicBlock(String identifier){
		this.identifier = identifier;
	}

	public void addInst(IRIns inst){
		if(!terminated){
			if(isEmpty()){
				headIns = tailIns = inst;
			}
			else {
				tailIns.setNextIns(inst);
				inst.setPrevIns(tailIns);
				tailIns = inst;
			}
			inst.setBelongBB(this);
		}
	}

	public void addLastInst(IRIns inst){
		addInst(inst);
		setTerminated(true);
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

	public IRIns getTailIns() {
		return tailIns;
	}

	public boolean isEmpty(){ return headIns == null; }
}
