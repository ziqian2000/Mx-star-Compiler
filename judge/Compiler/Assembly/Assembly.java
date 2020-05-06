package Compiler.Assembly;

import Compiler.Assembly.Operand.PhysicalRegister;
import Compiler.IR.Operand.StaticStrConst;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Assembly {

	// physical registers
	public String[] phyRegName = {		  "zero", "ra", "sp", "gp", "tp", "t0", "t1", "t2",
											"s0", "s1", "a0", "a1", "a2", "a3", "a4", "a5",
											"a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7",
											"s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6" };

	public String[] allocatableRegName = {		  "ra",					  "t0", "t1", "t2",
											"s0", "s1", "a0", "a1", "a2", "a3", "a4", "a5",
											"a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7",
											"s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6" };

	public String[] calleeSaveRegisterName =
										{
											"s0", "s1",
														"s2", "s3", "s4", "s5", "s6", "s7",
											"s8", "s9", "s10", "s11"};

	public String[] callerSaveRegisterName =
										{	  	  "ra", 				  "t0", "t1", "t2",
												  		"a0", "a1", "a2", "a3", "a4", "a5",
											"a6", "a7",
																	"t3", "t4", "t5", "t6" };

	public HashMap<String, PhysicalRegister> phyReg = new HashMap<>();

	// functions
	private List<AsmFunction> functionList = new ArrayList<>();

	// lib function
	public AsmFunction mallocAsmFunc = new AsmFunction("malloc", true);

	// global variables & string
	public List<VirtualRegister> globalVarList = new ArrayList<>(); // todo : global var init can be done in pre-processing
	public List<StaticStrConst> stringList = new ArrayList<>();


	public Assembly(){
		for(String name : phyRegName) phyReg.put(name, new PhysicalRegister(name));
	}

	public PhysicalRegister getPhyReg(String name){
		PhysicalRegister retReg = phyReg.get(name);
		if(retReg == null) throw new FuckingException(name + " is no a physical register, fuck you.");
		return retReg;
	}

	public String[] getAllocatableRegName() {
		return allocatableRegName;
	}

	public String[] getCalleeSaveRegisterName() {
		return calleeSaveRegisterName;
	}

	public String[] getCallerSaveRegisterName() {
		return callerSaveRegisterName;
	}

	public void addFunction(AsmFunction function){functionList.add(function);}

	public List<AsmFunction> getFunctionList() {
		return functionList;
	}
}
