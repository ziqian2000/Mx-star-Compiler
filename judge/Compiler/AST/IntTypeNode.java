package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class IntTypeNode extends BasicTypeNode {

	public IntTypeNode(Position position){
		super(position, "int");
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
