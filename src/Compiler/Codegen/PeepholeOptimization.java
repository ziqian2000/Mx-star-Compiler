package Compiler.Codegen;

import Compiler.Assembly.Assembly;
import Compiler.Assembly.Instr.AsmMove;

public class PeepholeOptimization {

	private Assembly asm;

	public PeepholeOptimization(Assembly asm){
		this.asm = asm;
	}

	public void run(){
		eliminateUselessMove();
	}

	private void eliminateUselessMove(){
		for(var func : asm.getFunctionList()) if(!func.getIsBuiltin()){
			for(var BB : func.getPreOrderBBList()){
				for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
					if(ins instanceof AsmMove && ((AsmMove) ins).getRs1() == ((AsmMove) ins).getRd()){
						ins.removeFromList();
					}
				}
			}
		}
	}

}
