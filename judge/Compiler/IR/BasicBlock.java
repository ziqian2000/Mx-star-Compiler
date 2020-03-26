package Compiler.IR;

import Compiler.IR.Instr.IRIns;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {

	String identifier;

	private List<IRIns> InstList;
	private boolean terminated;

	public BasicBlock(){
		InstList = new ArrayList<>();
	}
	public BasicBlock(String identifier){
		InstList = new ArrayList<>();
		this.identifier = identifier;
	}

	public void addInst(IRIns inst){
		if(!terminated){
			InstList.add(inst);
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

	public List<IRIns> getInstList() {
		return InstList;
	}
}
