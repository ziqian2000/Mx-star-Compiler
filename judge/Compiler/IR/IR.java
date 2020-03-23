package Compiler.IR;

import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.StaticStrConst;

import java.util.ArrayList;
import java.util.List;

public class IR {

	private List<Register> globalVarList = new ArrayList<>();
	private List<Function> functionList = new ArrayList<>();
	private List<StaticStrConst> staticStrConstList = new ArrayList<>();

	public void addGlobalVar(Register globalVar){
		globalVarList.add(globalVar);
	}
	public void addFunction(Function function){functionList.add(function);}
	public void addStaticStrConst(StaticStrConst s){staticStrConstList.add(s);}

	public List<Register> getGlobalVarList() {
		return globalVarList;
	}
	public List<Function> getFunctionList() {
		return functionList;
	}
	public List<StaticStrConst> getStaticStrConstList() {
		return staticStrConstList;
	}
}
