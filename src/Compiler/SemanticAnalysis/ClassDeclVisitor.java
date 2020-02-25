package Compiler.SemanticAnalysis;

import Compiler.AST.ClassDeclNode;
import Compiler.AST.DeclNode;
import Compiler.AST.ProgramNode;
import Compiler.Symbol.ClassSymbol;
import Compiler.Utils.SemanticException;

public class ClassDeclVisitor extends ASTBaseVisitor {

	private Scope scope;

	public ClassDeclVisitor(Scope scope){
		this.scope = scope;
	}

	public void visit(ProgramNode node){
		for(DeclNode decl : node.getDeclList()){
			decl.accept(this);
		}
	}

	public void visit(ClassDeclNode node){
		String identifier = node.getIdentifier();
		if(scope.findClass(identifier) != null)
			throw new SemanticException(node.getPosition(), "redefinition of class " + identifier);
		else
			scope.addClass(identifier, new ClassSymbol()); // TODO: class symbol need identifier
	}


}
