package Compiler.IR;

import Compiler.Assembly.AsmFunction;

public class StackPointerOffset implements StackLocation {

	private int offset;
	private AsmFunction function;
	private boolean fromBottom;

	public StackPointerOffset(int offset, boolean fromBottom, AsmFunction function){
		this.offset = offset;
		this.fromBottom = fromBottom;
		this.function = function;
	}

}
