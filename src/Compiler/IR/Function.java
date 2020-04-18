package Compiler.IR;

import Compiler.IR.Operand.VirtualRegister;

import java.util.*;

public class Function {

	private String identifier;
	private List<VirtualRegister> paraList;
	private BasicBlock entryBB;
	private BasicBlock exitBB;
	private boolean isMemberFunc;
	private boolean isBuiltin = false;
	private VirtualRegister obj;

	private List<BasicBlock> BBList; // in pre-order DFS
	private Set<VirtualRegister> globals;

	public Function(String identifier){
		this.identifier = identifier;
		paraList = new ArrayList<>();
		globals = new HashSet<>();
	}

	public void addParameter(VirtualRegister register){
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

	public List<VirtualRegister> getParaList() {
		return paraList;
	}

	public void setIsMemberFunc(boolean isMemberFunc) {
		this.isMemberFunc = isMemberFunc;
	}

	public void setIsBuiltin(Boolean isBuiltin){ this.isBuiltin = isBuiltin;}

	public boolean getIsBuiltin(){return isBuiltin;}

	public void setObj(VirtualRegister obj) {
		this.obj = obj;
	}

	public VirtualRegister getObj() {
		return obj;
	}

	public boolean getIsMemberFunc(){
		return isMemberFunc;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void addGlobals(VirtualRegister global){
		globals.add(global);
	}

	public Set<VirtualRegister> getGlobals() {
		return globals;
	}

	public boolean reachable(BasicBlock BB) {
		return BBList.contains(BB);
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
			if(basicBlock != null && !basicBlock.getPreBBList().contains(parBB))
				basicBlock.getPreBBList().add(parBB);
			return;
		}
		if(parBB != null) basicBlock.setPreBBList(new ArrayList<>(Collections.singleton(parBB)));
		else basicBlock.setPreBBList(new ArrayList<>());
		basicBlock.setParent(parBB);
		visited.add(basicBlock);
		BBList.add(basicBlock);
		basicBlock.makeSucBBList();
		basicBlock.getSucBBList().forEach(BB -> dfsBB(BB, basicBlock, visited));
	}

}
