package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class ContStmtNode extends StmtNode {

	public ContStmtNode(Position position){
		super(position);
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}

}
