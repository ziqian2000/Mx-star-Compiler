package Compiler.SemanticAnalysis;

import Compiler.AST.ClassDeclNode;
import Compiler.AST.DeclNode;
import Compiler.AST.ProgramNode;
import Compiler.SymbolTable.Symbol.ClassSymbol;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Type.ClassType;
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
		if(scope.findSymbol(identifier) != null) {
			throw new SemanticException(node.getPosition(), "redefinition of class : " + identifier);
		}
		else{
			ClassSymbol classSymbol = new ClassSymbol(identifier, new ClassType(identifier));
			ClassType classType = new ClassType(identifier);
			classType.setSelfSymbol(classSymbol);
			classSymbol.setSelfType(classType);
			scope.addSymbol(identifier, classSymbol);
			scope.addType(identifier, classType);
		}
	}


}
