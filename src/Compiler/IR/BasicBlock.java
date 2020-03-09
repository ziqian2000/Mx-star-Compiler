package Compiler.IR;

import Compiler.IR.Instr.IRIns;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {

	String identifier;

	private List<IRIns> InstList;
	private boolean completed;

	public BasicBlock(){
		InstList = new ArrayList<>();
	}
	public BasicBlock(String identifier){
		InstList = new ArrayList<>();
		this.identifier = identifier;
	}

	public void addInst(IRIns inst){
		if(!completed){
			InstList.add(inst);
			inst.setBelongBB(this);
		}
	}

	public void addLastInst(IRIns inst){
		addInst(inst);
		setCompleted(true);
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isCompleted(){
		return completed;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<IRIns> getInstList() {
		return InstList;
	}
}
