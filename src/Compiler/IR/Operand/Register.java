package Compiler.IR.Operand;

import Compiler.Assembly.Instr.AsmMove;
import Compiler.IR.Instr.IRIns;
import Compiler.IR.StackLocation;
import Compiler.IR.StackPointerOffset;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Register extends Storage implements StackLocation {

	// register allocation
	public Set<Register> adjList = new LinkedHashSet<>();
	public int degree;
	public Set<AsmMove> moveList = new LinkedHashSet<>();
	public Register alias;
	public String color;
	public StackPointerOffset spillAddr;

	// SSA optimization
	public IRIns def = null;
	public Set<IRIns> use = new LinkedHashSet<>();
	public void cleanDefUse(){def = null; use.clear();}

	// register allocation
	public int spillPriority;
	public boolean addForSpill = false;

	public Register(){

	}

	public Register(String identifier){
		super(identifier);
	}

	public void clean(){
		adjList.clear();
		moveList.clear();
		alias = null;
		spillAddr = null;
	}

}
