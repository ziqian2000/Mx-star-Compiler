package Compiler.Codegen;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.Assembly.AsmFunction;
import Compiler.Assembly.Assembly;
import Compiler.Assembly.Instr.*;
import Compiler.Assembly.Operand.PhysicalRegister;
import Compiler.IR.Operand.Register;
import Compiler.IR.Operand.VirtualRegister;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class AsmPrinter implements AsmVisitor{

	PrintStream output;
	private Assembly asm;

	Set<AsmBasicBlock> BBVisitMap = new LinkedHashSet<>();
	Map<AsmBasicBlock, String> BBNameMap = new LinkedHashMap<>();
	int nameCnt = 0;

	public AsmPrinter(Assembly asm){
		this.asm = asm;
	}

	public void run(PrintStream printStream){
		output = printStream;

		indPrLn(".section" + "\t" + ".data" + "\n");

		for(var global : asm.globalVarList){
			indPrLn(".globl" + "\t" + global.getIdentifier());
			prLn(global.getIdentifier() + ":");
			indPrLn(".zero" + "\t" + "4");
			prLn("");
		}

		for(var string : asm.stringList){
			indPrLn(".globl" + "\t" + string.getIdentifier());
			prLn(string.getIdentifier() + ":");
			indPrLn(".string" + "\t" + string.getValue());
			prLn("");
		}

		indPrLn(".text" + "\n");
		for(var func : asm.getFunctionList())
			if(!func.getIsBuiltin())
				visit(func);
	}

	// utility method

	public void pr(String str){
		output.print(str);
	}

	public void prLn(String str){
		output.print(str + "\n");
	}

	public void indPrLn(String str) {
		prLn("\t" + str);
	}

	public String BB2Str(AsmBasicBlock BB){
		if(BBNameMap.get(BB) == null)
			BBNameMap.put(BB, "." + (BB.getIdentifier() == null ? "b" : BB.getIdentifier()) + "_" + nameCnt++);
		return BBNameMap.get(BB);
	}

	// visit

	public void optimizedBBPrintingForLessJump(AsmBasicBlock BB, Set<AsmBasicBlock> visited){
		var tailIns = BB.getTailIns();
		if(tailIns instanceof AsmReturn) {
			visit(BB);
			visited.add(BB);
			return;
		}

		assert tailIns instanceof AsmJump;
		AsmBasicBlock firstBB = null;
		if(!visited.contains(((AsmJump) tailIns).getBB())) {
			tailIns.removeFromList();
			firstBB = ((AsmJump) tailIns).getBB();
		}

		visit(BB);
		visited.add(BB);

		if(firstBB != null) optimizedBBPrintingForLessJump(firstBB, visited);
		for(var suc : BB.getSucBBList()) if(!visited.contains(suc)) optimizedBBPrintingForLessJump(suc, visited);

	}

	public void visit(AsmFunction func){
		indPrLn(".globl" + "\t" + func.getIdentifier());
		prLn(func.getIdentifier() + ":");
		func.makePreOrderBBList();
		optimizedBBPrintingForLessJump(func.getEntryBB(), new LinkedHashSet<>());
		prLn("");
	}

	public void visit(AsmBasicBlock BB){
		prLn(BB2Str(BB) + ":");
		for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
			ins.accept(this);
		}

	}

	public void visit(AsmBranch ins) {
		indPrLn(ins.getOpStr() + "\t" + ins.getRs1().getIdentifier() + ", " + ins.getRs2().getIdentifier() + ", " + BB2Str(ins.getBB()));
	}

	public void visit(AsmCall ins){
		indPrLn("call" + "\t" + ins.getFunc().getIdentifier());
	}

	public void visit(AsmITypeIns ins){
		indPrLn(ins.getOpStr() + "\t" + ins.getRd().getIdentifier() + ", " + ins.getRs1().getIdentifier() + ", " + ins.getImm().getIdentifier());
	}

	public void visit(AsmJump ins){
		indPrLn("j" + "\t" + BB2Str(ins.getBB()));
	}

	public void visit(AsmLA ins){
		indPrLn("la" + "\t" + ins.getRd().getIdentifier() + ", " + ins.getSymbol().getIdentifier());
	}

	public void visit(AsmLI ins){
		indPrLn("li" + "\t" + ins.getRd().getIdentifier() + ", " + ins.getImm().getIdentifier());
	}

	public void visit(AsmLoad ins){
		String opcode;
		switch (ins.getSize()){
			case 1 : opcode = "lb"; break;
			case 2 : opcode = "lh"; break;
			case 4 : opcode = "lw"; break;
			default: opcode = "WTF"; assert false;
		}

		var loc = ins.getLoc();
		String src;
		if(loc instanceof PhysicalRegister) {
			 src = ins.getOffset() + "(" +  ((Register) loc).getIdentifier() + ")";
		}
		else if(loc instanceof VirtualRegister) { // global variable
			src = ((VirtualRegister) loc).getIdentifier();
//			assert ((VirtualRegister) loc).isGlobal();
		}
		else src = loc.toString();

		indPrLn(opcode + "\t" + ins.getRd().getIdentifier() + ", " + src);

	}

	public void visit(AsmMove ins){
		indPrLn("mv" + "\t" + ins.getRd().getIdentifier() + ", " + ins.getRs1().getIdentifier());
	}

	public void visit(AsmReturn ins){
		indPrLn("ret");
	}

	public void visit(AsmRTypeIns ins){
		indPrLn(ins.getOpStr() + "\t" + ins.getRd().getIdentifier() + ", " + ins.getRs1().getIdentifier() + ", " + ins.getRs2().getIdentifier());
	}

	public void visit(AsmStore ins){
		String opcode;
		switch (ins.getSize()){
			case 1 : opcode = "sb"; break;
			case 2 : opcode = "sh"; break;
			case 4 : opcode = "sw"; break;
			default: opcode = "WTF"; assert false;
		}

		if(ins.getRt() == null){
			var loc = ins.getLoc();
			String src;
			if(loc instanceof Register) src = ins.getOffset() + "(" +  ((Register) loc).getIdentifier() + ")";
			else src = loc.toString();

			indPrLn(opcode + "\t" + ins.getRs().getIdentifier() + ", " + src);
		}
		else{
			// global
			assert ins.getLoc() instanceof VirtualRegister;
			assert ((VirtualRegister) ins.getLoc()).isGlobal();
			indPrLn(opcode + "\t" + ins.getRs().getIdentifier() + ", " + ((Register) ins.getLoc()).getIdentifier() + ", " + ins.getRt().getIdentifier());
		}
	}
}
