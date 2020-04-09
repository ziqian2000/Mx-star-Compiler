package Compiler.IR.Operand;

public class I32Pointer extends Register {

	public I32Pointer(){super();}

	public I32Pointer(String identifier){
		super(identifier);
	}

	@Override
	public Register getSSACopyWithID(int id){return new I32Pointer(this.getIdentifier());}

}
