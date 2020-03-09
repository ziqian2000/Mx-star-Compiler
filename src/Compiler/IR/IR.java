package Compiler.IR;

import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;

import java.util.ArrayList;
import java.util.List;

public class IR {

	private List<VirtualRegister> globalVarList = new ArrayList<>();
	private List<Function> functionList = new ArrayList<>();

	public void addGlobalVar(VirtualRegister globalVar){
		globalVarList.add(globalVar);
	}
	public void addFunction(Function function){functionList.add(function);}

	public List<Function> getFunctionList() {
		return functionList;
	}
}
