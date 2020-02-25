package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class VoidTypeNode extends BasicTypeNode {

	public VoidTypeNode(Position position){
		super(position, "void");
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
