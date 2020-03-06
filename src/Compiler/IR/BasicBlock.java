package Compiler.IR;

import Compiler.IR.Instr.IR;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {

	private List<IR> InstList;
	private boolean completed;

	public BasicBlock(){
		InstList = new ArrayList<>();
	}

	public void addInst(IR inst){
		if(!completed){
			InstList.add(inst);
			inst.setBelongBB(this);
		}
	}

	public void addLastInst(IR inst){
		addInst(inst);
		setCompleted(true);
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isCompleted(){
		return completed;
	}
}
