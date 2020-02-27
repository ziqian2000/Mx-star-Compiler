package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.ClassSymbol;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.SemanticException;

public class ClassMemberVisitor extends ASTBaseVisitor {

	private Scope scope;

	public ClassMemberVisitor(Scope scope){
		this.scope = scope;
	}

	public void visit(ProgramNode node){
		for(DeclNode decl : node.getDeclList()){
			if(decl instanceof ClassDeclNode)
				decl.accept(this);
		}
	}

	public void visit(ClassDeclNode node){
		ClassSymbol classSymbol = (ClassSymbol) scope.findSymbol(node.getIdentifier());
		scope = new Scope(scope);
		scope.setCurrentClassType(classSymbol.getSelfType());
		classSymbol.setBodyScope(scope);
		for(VarDeclNode varDecl : node.getVarDeclList())
			varDecl.accept(this);
		for(FuncDeclNode funcDecl : node.getFuncDeclList())
			funcDecl.accept(this);
		scope = scope.getUpperScope();
	}

	public void visit(VarDeclNode node){
		String identifier = node.getIdentifier();
		Type type = SymbolTableAssistant.typeNode2VarType(scope, node.getType());
		if(scope.findLocalSymbol(identifier) != null)
			throw new SemanticException(node.getPosition(), "redeclaration of variable : " + identifier);
		else scope.addSymbol(identifier, new VarSymbol(identifier, type));
	}

	public void visit(FuncDeclNode node){
		String identifier = node.getIdentifier();
		Type type = null;
		if(node.getType() != null)
			type = SymbolTableAssistant.typeNode2VarType(scope, node.getType());
		if(scope.findLocalSymbol(identifier) != null)
			throw new SemanticException(node.getPosition(), "redeclaration of function : " + identifier);
		else scope.addSymbol(identifier, new FuncSymbol(identifier, type));
	}

}
