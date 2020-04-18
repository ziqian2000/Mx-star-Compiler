package Compiler.IR;

import Compiler.IR.Operand.VirtualRegister;
import Compiler.IR.Operand.StaticStrConst;

import java.util.ArrayList;
import java.util.List;

public class IR {

	private List<VirtualRegister> globalVarList = new ArrayList<>();
	private List<Function> functionList = new ArrayList<>();
	private List<StaticStrConst> staticStrConstList = new ArrayList<>();

	private boolean SSAForm;

	public IR(){ SSAForm = false; }

	public void addGlobalVar(VirtualRegister globalVar){
		globalVarList.add(globalVar);
	}
	public void addFunction(Function function){functionList.add(function);}
	public void addStaticStrConst(StaticStrConst s){staticStrConstList.add(s);}

	public List<VirtualRegister> getGlobalVarList() {
		return globalVarList;
	}
	public List<Function> getFunctionList() {
		return functionList;
	}
	public List<StaticStrConst> getStaticStrConstList() {
		return staticStrConstList;
	}

	public boolean getSSAForm() {
		return SSAForm;
	}
	public void setSSAForm(boolean SSAForm) {
		this.SSAForm = SSAForm;
	}
}
