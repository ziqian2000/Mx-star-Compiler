package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class ClassTypeNode extends TypeNode {

	public ClassTypeNode(Position position, String identifier){
		super(position, identifier);
	}


	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
