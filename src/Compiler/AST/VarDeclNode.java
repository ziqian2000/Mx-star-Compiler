package Compiler.AST;

import Compiler.utils.Position;

public class VarDeclNode extends DeclNode {

	private TypeNode type;
	private String identifier;
	private ExprNode expr;

	public VarDeclNode(Position position, TypeNode type, String identifier, ExprNode expr){
		super(position);
		this.type = type;
		this.identifier = identifier;
		this.expr = expr;
	}

	public void setType(TypeNode type){
		this.type = type;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
