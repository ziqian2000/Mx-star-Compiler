package Compiler.Assembly;

public class AsmFunction {

	private String identifier;
	private int stackSize = 0;
	private AsmBasicBlock entryBB;

	public AsmFunction(String identifier){
		this.identifier = identifier;
	}

	public void setEntryBB(AsmBasicBlock entryBB) {
		this.entryBB = entryBB;
	}

	public void expandStack(int siz){stackSize += siz;}

}
