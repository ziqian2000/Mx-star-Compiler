package Compiler.IR;

import Compiler.Assembly.AsmFunction;
import Compiler.Assembly.Operand.PhysicalRegister;

public class StackPointerOffset implements StackLocation {

	/**
	 * This class is necessary since before register allocation we cannot know the size of stack of each function.
	 */

	private int offset;
	private AsmFunction function;
	private boolean fromBottom;
	private PhysicalRegister sp;

	public StackPointerOffset(int offset, boolean fromBottom, AsmFunction function, PhysicalRegister sp){
		this.offset = offset;
		this.fromBottom = fromBottom;
		this.function = function;
		this.sp = sp;
	}

	public PhysicalRegister getSp() {
		return sp;
	}

	@Override
	public String toString() {
		int spOffset = offset + (fromBottom ? function.getAlignedStackSize() : 0);
		return spOffset + "(sp)";
	}
}
