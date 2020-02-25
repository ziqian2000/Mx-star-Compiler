package Compiler.SemanticAnalysis;

import Compiler.AST.DeclNode;
import Compiler.AST.FuncDeclNode;
import Compiler.AST.ProgramNode;
import Compiler.AST.VarDeclListNode;
import Compiler.Utils.SemanticException;

public class GlobalVarDeclVisitor extends ASTBaseVisitor {

	private Scope scope;

	public GlobalVarDeclVisitor(Scope scope){
		this.scope = scope;
	}

	public void visit(ProgramNode node){
		for(DeclNode decl : node.getDeclList()){
			decl.accept(this);
		}
	}

	public void visit(VarDeclListNode node){
		// TODO: ...
	}
}
