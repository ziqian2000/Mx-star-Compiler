package Compiler.IR;

import Compiler.IR.Operand.I32Pointer;
import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.VirtualRegister;

import java.util.ArrayList;
import java.util.List;

public class Function {

	private String identifier;
	private List<VirtualRegister> paraList;
	private BasicBlock entryBB;
	private boolean isMemberFunc;
	private I32Value obj;

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

	public BasicBlock getEntryBB() {
		return entryBB;
	}

	public List<VirtualRegister> getParaList() {
		return paraList;
	}

	public void setIsMemberFunc(boolean isMemberFunc) {
		this.isMemberFunc = isMemberFunc;
	}

	public void setObj(I32Value obj) {
		this.obj = obj;
	}

	public I32Value getObjPtr() {
		return obj;
	}

	public boolean getIsMemberFunc(){
		return isMemberFunc;
	}

	public String getIdentifier() {
		return identifier;
	}
}
