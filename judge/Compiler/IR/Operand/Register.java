package Compiler.IR.Operand;

import Compiler.Assembly.Instr.AsmMove;
import Compiler.IR.StackLocation;
import Compiler.IR.StackPointerOffset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Register extends Storage implements StackLocation {

	public Register(){

	}

	public Register(String identifier){
		super(identifier);
	}

	// register allocation
	public Set<Register> adjList = new HashSet<>();
	public int degree;
	public Set<AsmMove> moveList = new HashSet<>();
	public Register alias;
	public String color;
	public StackPointerOffset spillAddr;

	public void clean(){
		adjList.clear();
		moveList.clear();
		alias = null;
		spillAddr = null;
	}

}
