package Compiler.IR.Operand;

public class I32Pointer extends VirtualRegister {

	public I32Pointer(){}

	public I32Pointer(String identifier){
		super("*" + identifier);
	}

}
