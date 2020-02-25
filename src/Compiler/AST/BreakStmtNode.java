package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.utils.Position;

public class BreakStmtNode extends StmtNode {

	public BreakStmtNode(Position position){
		super(position);
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}

}
