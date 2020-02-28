package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.Loop;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.ClassSymbol;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.IntType;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Position;
import Compiler.Utils.SemanticException;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class SemanticInfoVisitor implements ASTVisitor {

	private Scope scope;
	private Stack<Loop> loopStack;

	public SemanticInfoVisitor(Scope scope){
		this.scope = scope;
		loopStack = new Stack<>();
	}

	public void mainFuncChecker(){
		Symbol symbol = scope.findSymbol("main");
		if(!(symbol instanceof FuncSymbol))
			throw new SemanticException(new Position(0, 0), "no main function");
		else if(!(((FuncSymbol)symbol).getRetType() instanceof IntType))
			throw new SemanticException(new Position(0, 0), "main function must return 'int'");
		else if(!((FuncSymbol)symbol).getParaSymbolList().isEmpty())
			throw new SemanticException(new Position(0, 0), "main function should not have parameters");
	}

	public void visit(ProgramNode node){
		mainFuncChecker();
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
		if(node.getExpr() != null)
			node.getExpr().accept(this);
	}

	public void visit(ClassDeclNode node){
		ClassSymbol classSymbol = node.getClassSymbol();
		scope = classSymbol.getBodyScope();
		for(FuncDeclNode funcDecl : node.getFuncDeclList())
			funcDecl.accept(this);
		scope = scope.getUpperScope();
	}

	public void visit(FuncDeclNode node){
		FuncSymbol funcSymbol = node.getFuncSymbol();
		scope = funcSymbol.getBodyScope();
		if(node.getParaDeclList() != null)
			for(VarDeclNode decl : node.getParaDeclList().getVarDeclNodeList())
				decl.accept(this);
		node.getBodyStmt().accept(this);
		scope = scope.getUpperScope();

		if(funcSymbol.getRetType() == SymbolTableAssistant.nullType){
			if(scope.getCurrentClassType() == null)
				throw new SemanticException(node.getPosition(), "return type required");
			if(!scope.getCurrentClassType().getIdentifier().equals(node.getIdentifier()))
				throw new SemanticException(node.getPosition(), "mismatched constructor name : " + node.getIdentifier());
		}
		else if(scope.getCurrentClassType() != null && scope.getCurrentClassType().getIdentifier().equals(node.getIdentifier()))
			throw new SemanticException(node.getPosition(), "constructor return type error : " + node.getIdentifier());
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

	public void visit(ClassTypeNode node){
		// inaccessible method
	}

	public void visit(IdExprNode node){
		// nothing to do
	}

	public void visit(ThisExprNode node){
		if(scope.getCurrentClassType() == null)
			throw new SemanticException(node.getPosition(), "invalid use of 'this'");
	}

	public void visit(IntConstExprNode node){
		// nothing to do
	}

	public void visit(BoolConstExprNode node){
		// nothing to do
	}

	public void visit(StringConstExprNode node){
		// nothing to do
	}

	public void visit(NullConstExprNode node){
		// nothing to do
	}

	public void visit(MemberExprNode node){
		node.getExpr().accept(this);
	}

	public void visit(ArrayIdxExprNode node){
		node.getArray().accept(this);
		node.getIdx().accept(this);
		if(node.getIdx().getType() != SymbolTableAssistant.intType)
			throw new SemanticException(node.getPosition(), "array index should be 'int'");
	}

	public void visit(FuncCallExprNode node){
		FuncSymbol funcSymbol = (FuncSymbol)node.getFunction().getSymbol();
		List<ExprNode> paraList = node.getParaList();
		List<VarSymbol> paraSymbolList = funcSymbol.getParaSymbolList();

		if(paraList.size() != paraSymbolList.size())
			throw new SemanticException(node.getPosition(), "mismatched size of arguments");

		int idx = 0;
		for (VarSymbol varSymbol : paraSymbolList) {
			if(varSymbol.getVarType() != paraList.get(idx).getType() && paraList.get(idx).getType() != SymbolTableAssistant.nullType)
				throw new SemanticException(node.getPosition(), "mismatched type of the " + (idx + 1) + "(st/nd/th) argument");
			idx++;
		}

		node.getFunction().accept(this);
		for(ExprNode para : node.getParaList()){
			para.accept(this);
		}
	}

	public void visit(NewExprNode node){
		if(node.getExprList() != null)
			for(ExprNode expr : node.getExprList()){
				expr.accept(this);
			}
	}

	public void visit(IfStmtNode node){
		if(node.getCond().getType() != SymbolTableAssistant.boolType)
			throw new SemanticException(node.getPosition(), "if condition should be 'bool'");

		node.getCond().accept(this);
		scope = node.getThenStmt().getBodyScope();
		node.getThenStmt().accept(this);
		scope = scope.getUpperScope();
		if(node.getElseStmt() != null) {
			scope = node.getElseStmt().getBodyScope();
			node.getElseStmt().accept(this);
			scope = scope.getUpperScope();
		}
	}

	public void visit(ForStmtNode node){
		loopStack.push(new Loop());
		if(node.getInit() != null) node.getInit().accept(this);
		if(node.getCond() != null) node.getCond().accept(this);
		if(node.getStep() != null) node.getStep().accept(this);
		node.getBodyStmt().accept(this);
		loopStack.pop();

		if(node.getCond() != null)
			if(node.getCond().getType() != SymbolTableAssistant.boolType)
				throw new SemanticException(node.getPosition(), "for condition should be 'bool'");
	}

	public void visit(WhileStmtNode node){
		node.getCond().accept(this);
		loopStack.push(new Loop());
		node.getBodyStmt().accept(this);
		loopStack.pop();

		if(node.getCond().getType() != SymbolTableAssistant.boolType)
			throw new SemanticException(node.getPosition(), "while condition should be 'bool'");
	}

	public void visit(ReturnStmtNode node){
		FuncSymbol funcSymbol = scope.getCurrentFuncSymbol();
		if(node.getExpr() != null){
			node.getExpr().accept(this);
			if(funcSymbol.getRetType() != node.getExpr().getType() && node.getExpr().getType() != SymbolTableAssistant.nullType)
				throw new SemanticException(node.getPosition(), "mismatched type of return value");
		}
		else{
			if(funcSymbol.getRetType() != SymbolTableAssistant.voidType
			&& funcSymbol.getRetType() != SymbolTableAssistant.nullType)
				throw new SemanticException(node.getPosition(), "redundant return value in function returning 'void'");
		}
	}

	public void visit(BreakStmtNode node){
		if(loopStack.empty())
			throw new SemanticException(node.getPosition(), "break statement not within a loop");
	}

	public void visit(ContStmtNode node){
		if(loopStack.empty())
				throw new SemanticException(node.getPosition(), "continue statement not within a loop");
	}

	public void visit(CompStmtNode node){
		if(!node.getIsFunctionBody()) scope = node.getBodyScope();
		if(node.getStmtList() != null)
			for(StmtNode stmtNode : node.getStmtList()){
				stmtNode.accept(this);
			}
		if(!node.getIsFunctionBody()) scope = scope.getUpperScope();
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
