package Compiler.Assembly;

import Compiler.Assembly.Instr.AsmIns;
import Compiler.IR.Instr.IRIns;

public class AsmBasicBlock {

	private String identifier;
	private AsmIns headIns, tailIns;

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

	public boolean isEmpty(){ return headIns == null; }

}
