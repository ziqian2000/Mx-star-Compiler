package Compiler.Codegen;

import Compiler.Assembly.Instr.*;

public interface AsmVisitor {

	void visit(AsmBranch instr);
	void visit(AsmCall instr);
	void visit(AsmITypeIns instr);
	void visit(AsmJump instr);
	void visit(AsmLA instr);
	void visit(AsmLI instr);
	void visit(AsmLoad instr);
	void visit(AsmMove instr);
	void visit(AsmReturn instr);
	void visit(AsmRTypeIns instr);
	void visit(AsmStore phi);
}
