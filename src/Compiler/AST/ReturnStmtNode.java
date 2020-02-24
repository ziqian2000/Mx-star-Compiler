package Compiler.AST;

import Compiler.utils.Position;

public class ReturnStmtNode extends StmtNode {

	public ReturnStmtNode(Position position){
		super(position);
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}

}
