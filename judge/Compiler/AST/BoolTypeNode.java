package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public class BoolTypeNode extends BasicTypeNode {

	public BoolTypeNode(Position position){
		super(position, "bool");
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
