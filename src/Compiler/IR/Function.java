package Compiler.IR;

import Compiler.IR.Operand.I32Pointer;
import Compiler.IR.Operand.VirtualRegister;

import java.util.ArrayList;
import java.util.List;

public class Function {

	private String identifier;
	private List<VirtualRegister> paraList;
	private BasicBlock entryBB;
	private boolean isMemberFunc;
	private I32Pointer objPtr;

	public Function(String identifier){
		this.identifier = identifier;
		paraList = new ArrayList<>();
	}

	public void addParameter(VirtualRegister virtualRegister){
		paraList.add(virtualRegister);
	}

	public void setEntryBB(BasicBlock entryBB) {
		this.entryBB = entryBB;
	}

	public List<VirtualRegister> getParaList() {
		return paraList;
	}

	public void setIsMemberFunc(boolean isMemberFunc) {
		this.isMemberFunc = isMemberFunc;
	}

	public void setObjPtr(I32Pointer objPtr) {
		this.objPtr = objPtr;
	}

	public I32Pointer getObjPtr() {
		return objPtr;
	}

	public boolean getIsMemberFunc(){
		return isMemberFunc;
	}
}
