package Compiler.IR;

import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Register;

import java.util.*;

public class Function {

	private String identifier;
	private List<Register> paraList;
	private BasicBlock entryBB;
	private BasicBlock exitBB;
	private boolean isMemberFunc;
	private Register obj;

	private List<BasicBlock> BBList; // in pre-order DFS

	public Function(String identifier){
		this.identifier = identifier;
		paraList = new ArrayList<>();
	}

	public void addParameter(Register register){
		paraList.add(register);
	}

	public void setEntryBB(BasicBlock entryBB) {
		this.entryBB = entryBB;
	}

	public BasicBlock getEntryBB() {
		return entryBB;
	}

	public void setExitBB(BasicBlock exitBB) {
		this.exitBB = exitBB;
	}

	public BasicBlock getExitBB() {
		return exitBB;
	}

	public List<Register> getParaList() {
		return paraList;
	}

	public void setIsMemberFunc(boolean isMemberFunc) {
		this.isMemberFunc = isMemberFunc;
	}

	public void setObj(Register obj) {
		this.obj = obj;
	}

	public Register getObj() {
		return obj;
	}

	public boolean getIsMemberFunc(){
		return isMemberFunc;
	}

	public String getIdentifier() {
		return identifier;
	}

	public List<BasicBlock> getBBList() {
		return BBList;
	}

	public void makeBBList(){
		BBList.clear();
		dfsBB(entryBB, new HashSet<>());
	}

	public void dfsBB(BasicBlock basicBlock, Set<BasicBlock> visited){
		if(visited.contains(basicBlock)) return;
		BBList.add(basicBlock);
		visited.add(basicBlock);
		basicBlock.getSucBBList().forEach(BB -> dfsBB(BB,visited));
	}

}
