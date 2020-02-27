package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Scope;
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
		if(scope.findSymbol(identifier) != null)
			throw new SemanticException(node.getPosition(), "redefinition of function :" + identifier);
		else
			scope.addSymbol(identifier, new FuncSymbol(identifier, SymbolTableAssistant.typeNode2VarType(scope, node.getType())));
	}
}
