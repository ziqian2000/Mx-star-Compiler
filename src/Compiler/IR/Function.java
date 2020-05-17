package Compiler.IR;

import Compiler.IR.Operand.VirtualRegister;

import java.lang.reflect.Array;
import java.util.*;

public class Function {

	private String identifier;
	private List<VirtualRegister> paraList;
	private BasicBlock entryBB;
	private BasicBlock exitBB;
	private boolean isMemberFunc;
	private boolean isBuiltin = false;
	private VirtualRegister obj;

	private List<BasicBlock> preOrderBBList; // in pre-order
	private List<BasicBlock> postOrderBBList; // in post-order
	private Set<VirtualRegister> globals;

	public Function(String identifier){
		this.identifier = identifier;
		paraList = new ArrayList<>();
		globals = new LinkedHashSet<>();
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
		return preOrderBBList.contains(BB);
	}

	// BB list

	public List<BasicBlock> getPreOrderBBList() {
		return preOrderBBList;
	}

	public void makePreOrderBBList(){
		preOrderBBList = new ArrayList<>();
		postOrderBBList = new ArrayList<>();
		dfsBB(entryBB, null, new LinkedHashSet<>());
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
		preOrderBBList.add(basicBlock);
		basicBlock.makeSucBBList();
		basicBlock.getSucBBList().forEach(BB -> dfsBB(BB, basicBlock, visited));
		postOrderBBList.add(basicBlock);
	}

	public void computePostOrderIdx(){
		for(int i = 0; i < postOrderBBList.size(); i++)
			postOrderBBList.get(i).postOrderIdx = i;
	}

	// DCE
	public List<Object> makeReverseCopy(){
		// What's copied: preBBList, sucBBList, parent, entryBB, exitBB, preOrderBBList
		Map<BasicBlock, BasicBlock> BBMap = new LinkedHashMap<>();
		Map<BasicBlock, BasicBlock> reBBMap = new LinkedHashMap<>();
		preOrderBBList.forEach(BB -> {
			BBMap.put(BB, new BasicBlock("reverse_" + BB.getIdentifier()));
			reBBMap.put(BBMap.get(BB), BB);
		});
		for(var BB : getPreOrderBBList()){
			var newBB = BBMap.get(BB);
			newBB.preBBList = new ArrayList<>();
			newBB.sucBBList = new ArrayList<>();
			BB.getPreBBList().forEach(preBB -> newBB.sucBBList.add(BBMap.get(preBB)));
			BB.getSucBBList().forEach(sucBB -> newBB.preBBList.add(BBMap.get(sucBB)));
		}

		var reverseFunc = new Function("reverse_" + getIdentifier());
		reverseFunc.setEntryBB(BBMap.get(exitBB));
		reverseFunc.setExitBB(BBMap.get(entryBB));
		reverseFunc.reverseMakePreOrderBBList();

		return Arrays.asList(reverseFunc, BBMap, reBBMap); // return the reverse function and the map of basic blocks
	}
	public void reverseMakePreOrderBBList(){
		preOrderBBList = new ArrayList<>();
		reverseDFSBB(entryBB, null, new LinkedHashSet<>());
	}
	public void reverseDFSBB(BasicBlock basicBlock, BasicBlock parBB, Set<BasicBlock> visited){
		if(basicBlock == null || visited.contains(basicBlock)) return;
		basicBlock.setParent(parBB);
		visited.add(basicBlock);
		preOrderBBList.add(basicBlock);
		basicBlock.getSucBBList().forEach(BB -> reverseDFSBB(BB, basicBlock, visited));
	}

}
