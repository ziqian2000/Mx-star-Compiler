package Compiler.IR.Operand;

public class Storage extends Operand {

	// IR
	private String name;

	public Storage(){}

	public Storage(String identifier){
		super(identifier);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
