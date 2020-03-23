package Compiler.IRVisitor;

import Compiler.IR.Instr.*;

public interface IRVisitor {

	void visit(Alloc instr);
	void visit(Move instr);
	void visit(Binary instr);
	void visit(Call instr);
	void visit(Branch instr);
	void visit(Jump instr);
	void visit(Load instr);
	void visit(Return instr);
	void visit(Store instr);
	void visit(Unary instr);

}
