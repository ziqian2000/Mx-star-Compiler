package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.ClassSymbol;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.ArrayType;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.SemanticException;

import java.util.List;

public class SymbolTableVisitor extends ASTBaseVisitor{

	private Scope scope;

	public SymbolTableVisitor(Scope scope){
		this.scope = scope;
	}

	// When visiting a node, the scope should already be corresponding.

	public void visit(ProgramNode node){
		for(DeclNode decl : node.getDeclList()){
			decl.accept(this);
		}
	}

	public void visit(VarDeclListNode node){
		for(VarDeclNode varDecl : node.getVarDeclNodeList()){
			varDecl.accept(this);
		}
	}

	public void visit(VarDeclNode node){
		String identifier = node.getIdentifier();
		Type type = SymbolTableAssistant.TypeNode2VarType(scope, node.getType());
		if(scope.findSymbol(identifier) != null)
			throw new SemanticException(node.getPosition(), "redeclaration of variable : " + identifier);
		else scope.addSymbol(identifier, new VarSymbol(identifier, type));
	}

	public void visit(ClassDeclNode node){
		ClassSymbol classSymbol = (ClassSymbol) scope.findSymbol(node.getIdentifier());
		scope = classSymbol.getBodyScope();
		for(FuncDeclNode funcDecl : node.getFuncDeclList())
			funcDecl.accept(this);
		scope = scope.getUpperScope();
	}

	public void visit(FuncDeclNode node){
		FuncSymbol funcSymbol = (FuncSymbol) scope.findSymbol(node.getIdentifier());
		if(node.getType() != null) {
			Type type = SymbolTableAssistant.TypeNode2VarType(scope, node.getType());
			funcSymbol.setRetType(type);
		}
		scope = new Scope(scope);
		funcSymbol.setBodyScope(scope);
		if(node.getParaDeclList() != null)
			for(VarDeclNode decl : node.getParaDeclList().getVarDeclNodeList())
				decl.accept(this);
		node.getBodyStmt().accept(this);
		scope = scope.getUpperScope();
	}

	public void visit(IntTypeNode node){
		// inaccessible method
	}

	public void visit(VoidTypeNode node){
		// inaccessible method
	}

	public void visit(StringTypeNode node){
		// inaccessible method
	}

	public void visit(BoolTypeNode node){
		// inaccessible method
	}

	public void visit(ArrayTypeNode node){
		// inaccessible method
	}

	public void visit(IdExprNode node){
		String identifier = node.getIdentifier();
		Symbol symbol = scope.findSymbol(identifier);
		if(symbol == null || symbol instanceof ClassSymbol)
			throw new SemanticException(node.getPosition(), "undeclared symbol : " + identifier);
		else node.setSymbol(symbol);
	}

	public void visit(ThisExprNode node){
		// nothing to do
		// TODO: type?
	}

	public void visit(IntConstExprNode node){
		node.setType(SymbolTableAssistant.intType);
	}

	public void visit(BoolConstExprNode node){
		node.setType(SymbolTableAssistant.boolType);
	}

	public void visit(StringConstExprNode node){
		node.setType(SymbolTableAssistant.stringType);
	}

	public void visit(NullConstExprNode node){
		node.setType(SymbolTableAssistant.voidType);
	}

	public void visit(MemberExprNode node){
		node.getExpr().accept(this);
		// TODO: identifier? type?
	}

	public void visit(ArrayIdxExprNode node){
		node.getArray().accept(this);
		node.getIdx().accept(this);
//		node.setType(SymbolTableAssistant.ArrayTypeDimDecrease(scope, node.getArray().getType(), 1, node));
	}

	public void visit(FuncCallExprNode node){
		node.getFunction().accept(this);
		for(ExprNode para : node.getParaList()){
			para.accept(this);
		}
		node.setSymbol(node.getFunction().getSymbol());
		node.setType(((FuncSymbol)node.getSymbol()).getRetType());
	}

	public void visit(NewExprNode node){
		Type type = SymbolTableAssistant.TypeNode2VarType(scope, node.getBaseType());
		node.setType(type); // TODO: array type ?
		for(ExprNode expr : node.getExprList()){
			expr.accept(this);
		}
	}

	public void visit(ClassTypeNode node){
		// nothing to do
	}

	public void visit(IfStmtNode node){
		node.getCond().accept(this);
		scope = new Scope(scope);
		node.getThenStmt().accept(this);
		scope = scope.getUpperScope();
		if(node.getElseStmt() != null) {
			scope = new Scope(scope);
			node.getThenStmt().accept(this);
			scope = scope.getUpperScope();
		}
	}

	public void visit(ForStmtNode node){
		scope = new Scope(scope);
		if(node.getInit() != null) node.getInit().accept(this);
		if(node.getCond() != null) node.getCond().accept(this);
		if(node.getStep() != null) node.getStep().accept(this);
		node.getBodyStmt().accept(this);
		scope = scope.getUpperScope();
	}

	public void visit(WhileStmtNode node){
		node.getCond().accept(this);
		scope = new Scope(scope);
		node.getBodyStmt().accept(this);
		scope = scope.getUpperScope();
	}

	public void visit(ReturnStmtNode node){
		// nothing to do
	}

	public void visit(BreakStmtNode node){
		// nothing to do
	}

	public void visit(ContStmtNode node){
		// nothing to do
	}

	public void visit(CompStmtNode node){
		if(!node.getFunctionBody()) scope = new Scope(scope);
		for(StmtNode stmtNode : node.getStmtList()){
			stmtNode.accept(this);
		}
		if(!node.getFunctionBody()) scope = scope.getUpperScope();
	}

	public void visit(VarDeclStmtNode node){
		node.getVarDeclList().accept(this);
	}

	public void visit(ExprStmtNode node){
		node.getExpr().accept(this);
	}

	public void visit(BinaryExprNode node){
		node.getLhs().accept(this);
		node.getRhs().accept(this);
	}

	public void visit(UnaryExprNode node){
		node.getOpr().accept(this);
	}


}
