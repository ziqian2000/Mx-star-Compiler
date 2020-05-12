package Compiler.Codegen;

import Compiler.Assembly.Assembly;
import Compiler.Assembly.Instr.AsmCall;
import Compiler.Assembly.Instr.AsmITypeIns;
import Compiler.IR.Operand.Immediate;

public class FinalProcessing {

	private Assembly asm;

	public FinalProcessing(Assembly asm){
		this.asm = asm;
	}

	public void run(){
		setSP();
		mainFuncRenaming();
		builtinFunctionRenaming();
	}

	private void setSP(){
		for(var func : asm.getFunctionList()) if(!func.getIsBuiltin()){
			int stackSize = func.getAlignedStackSize();
			if(stackSize > 0){
				assert func.getEntryBB().getPreBBList().isEmpty();
				func.getEntryBB().getHeadIns().prependIns(new AsmITypeIns(asm.getPhyReg("sp"), new Immediate(-stackSize), asm.getPhyReg("sp"), AsmITypeIns.Op.ADDI));
				func.getExitBB().getTailIns().prependIns(new AsmITypeIns(asm.getPhyReg("sp"), new Immediate(stackSize), asm.getPhyReg("sp"), AsmITypeIns.Op.ADDI));
			}
		}
	}

	private void mainFuncRenaming(){
		for(var func : asm.getFunctionList()){

			if(func.getIdentifier().equals("main")){
				func.setIdentifier("_main");
			}
			if(func.getIdentifier().equals("__init")){
				func.setIdentifier("main");
			}

		}
	}

	private void builtinFunctionRenaming(){
		for(var func : asm.getFunctionList()) if(!func.getIsBuiltin()){
			for(var BB : func.getBBList()){
				for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
					if(ins instanceof AsmCall){
						var id = ((AsmCall) ins).getFunc().getIdentifier();
						if(((AsmCall) ins).getFunc().getIsBuiltin()){
							((AsmCall) ins).getFunc().setIdentifier(id.replace('.', '_'));
						}
					}
				}
			}
		}
	}

}
