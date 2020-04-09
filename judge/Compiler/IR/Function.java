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
	private boolean isBuiltin = false;
	private Register obj;

	private List<BasicBlock> BBList; // in pre-order DFS
	private Set<Register> globals;

	public Function(String identifier){
		this.identifier = identifier;
		paraList = new ArrayList<>();
		globals = new HashSet<>();
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

	public void setIsBuiltin(Boolean isBuiltin){ this.isBuiltin = isBuiltin;}

	public boolean getIsBuiltin(){return isBuiltin;}

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

	public void addGlobals(Register global){
		globals.add(global);
	}

	public Set<Register> getGlobals() {
		return globals;
	}

	// BB list

	public List<BasicBlock> getBBList() {
		return BBList;
	}

	public void makeBBList(){
		BBList = new ArrayList<>();
		dfsBB(entryBB, null, new HashSet<>());
	}

	public void dfsBB(BasicBlock basicBlock, BasicBlock parBB, Set<BasicBlock> visited){
		if(basicBlock == null || visited.contains(basicBlock)) {
			if(basicBlock != null)
				basicBlock.getPreBBList().add(parBB);
			return;
		}
		basicBlock.setPreBBList(new ArrayList<>(Collections.singleton(parBB)));
		basicBlock.setParent(parBB);
		visited.add(basicBlock);
		BBList.add(basicBlock);
		basicBlock.makeSucBBList();
		basicBlock.getSucBBList().forEach(BB -> dfsBB(BB, basicBlock, visited));
	}

}
