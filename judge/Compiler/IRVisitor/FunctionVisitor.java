package Compiler.IRVisitor;

import Compiler.AST.*;
import Compiler.IR.Function;
import Compiler.SemanticAnalysis.ASTBaseVisitor;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.ClassType;
import Compiler.Utils.Config;

public class FunctionVisitor extends ASTBaseVisitor{

	public void visit(ProgramNode node){
		for(DeclNode decl : node.getDeclList())
			if(decl instanceof FuncDeclNode || decl instanceof ClassDeclNode)
				decl.accept(this);
	}

	public void visit(FuncDeclNode node){
		FuncSymbol funcSymbol = node.getFuncSymbol();
		Scope scope = funcSymbol.getBodyScope();
		ClassType classType = scope.getCurrentClassType();
		Function function = new Function((classType == null ? "" : classType.getIdentifier() + ".") + node.getIdentifier());
		function.setIsMemberFunc(classType != null);
		funcSymbol.setFunction(function);
	}

	public void visit(ClassDeclNode node){
		for(FuncDeclNode funcDeclNode : node.getFuncDeclList()){
			funcDeclNode.accept(this);
		}
	}


}
