package Compiler.Assembly;

import Compiler.IR.Function;

import java.util.*;

public class AsmFunction {

	private String identifier;
	private int stackSizeFromTop = 0;
	private int stackSizeFromBot = 0;
	private AsmBasicBlock entryBB;
	private AsmBasicBlock exitBB;
	private boolean isBuiltin;

	// debug
	private boolean stackSizeLocked = false;

	private List<AsmBasicBlock> BBList; // in pre-order DFS

	public AsmFunction(Function func){
		this.identifier = func.getIdentifier();
		this.isBuiltin = func.getIsBuiltin();
	}

	public AsmFunction(String identifier, boolean isBuiltin){
		this.identifier = identifier;
		this.isBuiltin = isBuiltin;
	}

	public void setEntryBB(AsmBasicBlock entryBB) {
		this.entryBB = entryBB;
	}

	public void setExitBB(AsmBasicBlock exitBB) {
		this.exitBB = exitBB;
	}

	public boolean getIsBuiltin(){return isBuiltin;}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getAlignedStackSize(){
		stackSizeLocked = true;
		return (stackSizeFromTop + stackSizeFromBot + 15) / 16 * 16;
	}

	public AsmBasicBlock getEntryBB() {
		return entryBB;
	}

	public AsmBasicBlock getExitBB() {
		return exitBB;
	}

	// BB list

	public List<AsmBasicBlock> getPreOrderBBList() {
		return BBList;
	}

	public void makePreOrderBBList(){
		BBList = new ArrayList<>();
		dfsBB(entryBB, null, new LinkedHashSet<>());
	}

	public void dfsBB(AsmBasicBlock basicBlock, AsmBasicBlock parBB, Set<AsmBasicBlock> visited){
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

	public int stackAllocFromBot(int size){
		assert !stackSizeLocked;
		// from top (sp)
		stackSizeFromBot += size;
		return -stackSizeFromBot;
	}

	public void setStackSizeFromTopMax(int stackSizeFromTop) {
		assert !stackSizeLocked;
		if(this.stackSizeFromTop < stackSizeFromTop)
			this.stackSizeFromTop = stackSizeFromTop;
	}
}
