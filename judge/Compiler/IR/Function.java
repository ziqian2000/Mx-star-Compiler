package Compiler.IR;

import Compiler.IR.Operand.I32Value;
import Compiler.IR.Operand.Register;

import java.util.ArrayList;
import java.util.List;

public class Function {

	private String identifier;
	private List<Register> paraList;
	private BasicBlock entryBB;
	private boolean isMemberFunc;
	private Register obj;
	private boolean hasReturnValue;

	public Function(String identifier){
		this.identifier = identifier;
		paraList = new ArrayList<>();
	}

	public void addParameter(Register register){
		paraList.add(register);
	}

	public void setEntryBB(BasicBlock entryBB) {
		this.entryBB = entryBB;
	}

	public BasicBlock getEntryBB() {
		return entryBB;
	}

	public List<Register> getParaList() {
		return paraList;
	}

	public void setIsMemberFunc(boolean isMemberFunc) {
		this.isMemberFunc = isMemberFunc;
	}

	public void setObj(Register obj) {
		this.obj = obj;
	}

	public Register getObj() {
		return obj;
	}

	public boolean getIsMemberFunc(){
		return isMemberFunc;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setHasReturnValue(boolean hasReturnValue) {
		this.hasReturnValue = hasReturnValue;
	}

	public boolean getHasReturnValue() {
		return hasReturnValue;
	}
}
