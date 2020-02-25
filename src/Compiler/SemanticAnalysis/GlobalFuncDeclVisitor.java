package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.Symbol.FuncSymbol;
import Compiler.Utils.SemanticException;

public class GlobalFuncDeclVisitor extends ASTBaseVisitor {

	private Scope scope;

	public GlobalFuncDeclVisitor(Scope scope){
		this.scope = scope;
	}

	public void visit(ProgramNode node){
		for(DeclNode decl : node.getDeclList()){
			decl.accept(this);
		}
	}

	public void visit(FuncDeclNode node){

		String identifier = node.getIdentifier();
		if(scope.findFunc(identifier) != null)
			throw new SemanticException(node.getPosition(), "redefinition of function " + identifier);
		else
			scope.addFunc(identifier, new FuncSymbol()); // TODO: function symbol need identifier
	}
}
