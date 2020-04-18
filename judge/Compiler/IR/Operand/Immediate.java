package Compiler.IR.Operand;

public class Immediate extends Operand {

	private int value;

	public Immediate(int value){
		super(String.valueOf(value));
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
