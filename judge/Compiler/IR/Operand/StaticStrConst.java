package Compiler.IR.Operand;

public class StaticStrConst extends Memory{

	private String value;

	public StaticStrConst(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
