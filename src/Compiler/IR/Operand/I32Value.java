package Compiler.IR.Operand;

public class I32Value extends Register {

	public I32Value(){super();}

	public I32Value(String identifier){
		super(identifier);
	}

	@Override
	public Register getSSACopyWithID(int id){return new I32Value(this.getIdentifier());}
}
