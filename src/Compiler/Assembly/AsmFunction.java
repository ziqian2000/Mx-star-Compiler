package Compiler.Assembly;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;

import java.util.*;

public class AsmFunction {

	private String identifier;
	private int stackSize = 0;
	private AsmBasicBlock entryBB;
	private boolean isBuiltin;

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

	public boolean getIsBuiltin(){return isBuiltin;}

	// BB list

	public List<AsmBasicBlock> getBBList() {
		return BBList;
	}

	public void makeBBList(){
		BBList = new ArrayList<>();
		dfsBB(entryBB, null, new HashSet<>());
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

	public int allocStack(int size){
		// from top (sp)
		stackSize += size;
		return -stackSize;
	}

}
