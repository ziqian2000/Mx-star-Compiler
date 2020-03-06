package Compiler.IR.Operand;

public abstract class Operand {

	private String identifier; // may be null

	public Operand(){}

	public Operand(String identifier){
		this.identifier = identifier;
	}

}
