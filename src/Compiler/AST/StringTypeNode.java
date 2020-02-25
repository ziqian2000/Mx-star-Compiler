package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class StringTypeNode extends BasicTypeNode {

	public StringTypeNode(Position position){
		super(position, "string");
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
