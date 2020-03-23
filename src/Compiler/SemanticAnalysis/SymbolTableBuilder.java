package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.ClassSymbol;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.*;
import Compiler.Utils.SemanticException;

import java.util.ArrayList;
import java.util.List;

public class SymbolTableBuilder implements ASTVisitor{

	private Scope scope;

	public SymbolTableBuilder(Scope scope){
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
		Type type = SymbolTableAssistant.typeNode2VarType(scope, node.getType());
		if(node.getExpr() != null){
			node.getExpr().accept(this);
			if(node.getExpr().getType() != SymbolTableAssistant.nullType && node.getExpr().getType() != type)
				throw new SemanticException(node.getPosition(), "mismatched type : '" + type.getIdentifier() + "' and '" + node.getExpr().getType().getIdentifier() + "'");
		}
		if(scope.findLocalSymbol(identifier) != null)
			throw new SemanticException(node.getPosition(), "redeclaration of variable : " + identifier);
		else{
			VarSymbol varSymbol = new VarSymbol(identifier, type);
			scope.mutuallyAddSymbol(identifier, varSymbol);
			node.setSymbol(varSymbol);
		}
	}

	public void visit(ClassDeclNode node){
		ClassSymbol classSymbol = (ClassSymbol) scope.findSymbol(node.getIdentifier());
		node.setClassSymbol(classSymbol);
		scope = classSymbol.getBodyScope();
		for(FuncDeclNode funcDecl : node.getFuncDeclList())
			funcDecl.accept(this);
		scope = scope.getUpperScope();
	}

	public void visit(FuncDeclNode node){
		FuncSymbol funcSymbol = (FuncSymbol) scope.findSymbol(node.getIdentifier());
		node.setFuncSymbol(funcSymbol);

		if(node.getType() != null) {
			Type type = SymbolTableAssistant.typeNode2VarType(scope, node.getType());
			funcSymbol.setRetType(type);
		}
		else funcSymbol.setRetType(SymbolTableAssistant.nullType);

		scope = new Scope(scope);
		funcSymbol.setBodyScope(scope);
		scope.setCurrentFuncSymbol(funcSymbol);

		List<VarSymbol> paraSymbolList = new ArrayList<>();
		if(node.getParaDeclList() != null)
			for(VarDeclNode decl : node.getParaDeclList().getVarDeclNodeList()) {
				decl.accept(this);
				paraSymbolList.add((VarSymbol)decl.getSymbol());
			}
		funcSymbol.setParaSymbolList(paraSymbolList);

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

	public void visit(ClassTypeNode node){
		// inaccessible method
	}

	public void visit(IdExprNode node){
		String identifier = node.getIdentifier();
		Symbol symbol = scope.findSymbol(identifier);
		if(symbol == null || symbol instanceof ClassSymbol)
			throw new SemanticException(node.getPosition(), "undeclared symbol : " + identifier);
		else node.setSymbol(symbol);

		if(symbol instanceof FuncSymbol) node.setType((((FuncSymbol)symbol).getRetType()));
		else node.setType(((VarSymbol)symbol).getVarType());
	}

	public void visit(ThisExprNode node){
		ClassType currentClassType = scope.getCurrentClassType();
		if(currentClassType == null)
			throw new SemanticException(node.getPosition(), "'this' should be inside class");
		else node.setType(currentClassType);
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
		node.setType(SymbolTableAssistant.nullType);
	}

	public void visit(MemberExprNode node){
		node.getExpr().accept(this);
		Type type = node.getExpr().getType();
		String identifier = node.getIdentifier();
		if(type instanceof ClassType){
			ClassSymbol classSymbol = ((ClassType)type).getSelfSymbol();
			Scope classScope = classSymbol.getBodyScope();
			Symbol symbol = classScope.findClassLocalSymbol(identifier);
			if(symbol == null)
				throw new SemanticException(node.getPosition(), "undeclared symbol : " + identifier);
			else{
				node.setSymbol(symbol);
				if(symbol instanceof VarSymbol) node.setType(((VarSymbol)symbol).getVarType());
				else if(symbol instanceof FuncSymbol) node.setType(((FuncSymbol)symbol).getRetType());
				else throw new SemanticException(node.getPosition(), "unrecognized identifier : " + identifier);
			}
		}
		else if(type instanceof ArrayType){
			Symbol symbol = SymbolTableAssistant.findArrayBuiltinFuncSymbol(identifier);
			if(symbol == null)
				throw new SemanticException(node.getPosition(), "undeclared symbol : " + identifier);
			else{
				node.setSymbol(symbol);
				if(symbol instanceof VarSymbol) node.setType(((VarSymbol)symbol).getVarType());
				else if(symbol instanceof FuncSymbol) node.setType(((FuncSymbol)symbol).getRetType());
				else throw new SemanticException(node.getPosition(), "unrecognized identifier : " + identifier);
			}
		}
		else if(type instanceof StringType){
			Symbol symbol = SymbolTableAssistant.findStringBuiltinFuncSymbol(identifier);
			if(symbol == null)
				throw new SemanticException(node.getPosition(), "undeclared symbol : " + identifier);
			else{
				node.setSymbol(symbol);
				if(symbol instanceof VarSymbol) node.setType(((VarSymbol)symbol).getVarType());
				else if(symbol instanceof FuncSymbol) node.setType(((FuncSymbol)symbol).getRetType());
				else throw new SemanticException(node.getPosition(), "unrecognized identifier : " + identifier);
			}
		}
		else throw new SemanticException(node.getPosition(), "illegal access of class member : " + identifier);
	}

	public void visit(ArrayIdxExprNode node){
		node.getArray().accept(this);
		node.getIdx().accept(this);
		Type type = SymbolTableAssistant.arrayTypeDimDecrease(scope, node.getArray().getType(), 1, node);
		node.setType(type);
		node.setValueCategory(ExprNode.category.LVALUE);
		if(!(node.getArray().getType() instanceof ArrayType)) {
			throw new SemanticException(node.getPosition(), "illegal access of non-array type : " + node.getArray().getType().getIdentifier());
		}
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
		Type type = SymbolTableAssistant.typeNode2VarType(scope, node.getBaseType());
		node.setType(SymbolTableAssistant.arrayTypeDimIncrease(scope, type, node.getDim(), node));
		if(node.getExprList() != null)
			for(ExprNode expr : node.getExprList()){
				expr.accept(this);
			}
	}

	public void visit(IfStmtNode node){
		node.getCond().accept(this);
		scope = new Scope(scope);
		node.setBodyScope(scope);
		node.getThenStmt().setBodyScope(scope);
		node.getThenStmt().accept(this);
		scope = scope.getUpperScope();
		if(node.getElseStmt() != null) {
			scope = new Scope(scope);
			node.getElseStmt().setBodyScope(scope);
			node.getElseStmt().accept(this);
			scope = scope.getUpperScope();
		}
	}

	public void visit(ForStmtNode node){
		scope = new Scope(scope);
		node.setBodyScope(scope);
		if(node.getInit() != null) node.getInit().accept(this);
		if(node.getCond() != null) node.getCond().accept(this);
		if(node.getStep() != null) node.getStep().accept(this);
		node.getBodyStmt().accept(this);
		scope = scope.getUpperScope();
	}

	public void visit(WhileStmtNode node){
		node.getCond().accept(this);
		scope = new Scope(scope);
		node.setBodyScope(scope);
		node.getBodyStmt().accept(this);
		scope = scope.getUpperScope();
	}

	public void visit(ReturnStmtNode node){
		if(node.getExpr() != null)
			node.getExpr().accept(this);
	}

	public void visit(BreakStmtNode node){
		// nothing to do
	}

	public void visit(ContStmtNode node){
		// nothing to do
	}

	public void visit(CompStmtNode node){
		if(!node.getIsFunctionBody()) scope = new Scope(scope);
		node.setBodyScope(scope);
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
		Type lhsType = node.getLhs().getType();
		Type rhsType = node.getRhs().getType();
		switch (node.getOp()){
			case ADD :
				if(lhsType instanceof StringType
						&& rhsType instanceof StringType)
					node.setType(SymbolTableAssistant.stringType);
				else
				if(lhsType instanceof IntType
						&& rhsType instanceof IntType)
					node.setType(SymbolTableAssistant.intType);
				else
					throw new SemanticException(node.getPosition(), "invalid operands of types '" + lhsType.getIdentifier() + "' and '" + rhsType.getIdentifier() + "' to binary operator " + node.getOp());
				break;

			case LT	 :
			case LE :
			case GT :
			case GE :
				if(lhsType instanceof StringType
						&& rhsType instanceof StringType)
					node.setType(SymbolTableAssistant.boolType);
				else
				if(lhsType instanceof IntType
						&& rhsType instanceof IntType)
					node.setType(SymbolTableAssistant.boolType);
				else
					throw new SemanticException(node.getPosition(), "invalid operands of types '" + lhsType.getIdentifier() + "' and '" + rhsType.getIdentifier() + "' to binary operator " + node.getOp());
				break;

			case EQ :
			case NEQ :
				if(lhsType instanceof StringType
						&& rhsType instanceof StringType)
					node.setType(SymbolTableAssistant.boolType);
				else
				if(lhsType instanceof IntType
						&& rhsType instanceof IntType)
					node.setType(SymbolTableAssistant.boolType);
				else
				if(lhsType instanceof BoolType
						&& rhsType instanceof BoolType)
					node.setType(SymbolTableAssistant.boolType);
				else
				if((lhsType instanceof ArrayType
						&& rhsType instanceof NullType)
						|| (lhsType instanceof NullType
						&& rhsType instanceof ArrayType))
					node.setType(SymbolTableAssistant.boolType);
				else
				if((lhsType instanceof ClassType
						&& rhsType instanceof NullType)
						|| (lhsType instanceof NullType
						&& rhsType instanceof ClassType))
					node.setType(SymbolTableAssistant.boolType);
				else
				if(lhsType instanceof NullType
						&& rhsType instanceof NullType)
					node.setType(SymbolTableAssistant.boolType);
				else
					throw new SemanticException(node.getPosition(), "invalid operands of types '" + lhsType.getIdentifier() + "' and '" + rhsType.getIdentifier() + "' to binary operator " + node.getOp());
				break;

			case SUB :
			case MUL :
			case DIV :
			case MOD :
			case SHL :
			case SHR :
			case AND :
			case OR :
			case XOR :
				if(lhsType instanceof IntType
						&& rhsType instanceof IntType)
					node.setType(SymbolTableAssistant.intType);
				else
					throw new SemanticException(node.getPosition(), "invalid operands of types '" + lhsType.getIdentifier() + "' and '" + rhsType.getIdentifier() + "' to binary operator " + node.getOp());
				break;

			case ASS :
				if(node.getLhs().getValueCategory() != ExprNode.category.LVALUE)
					throw new SemanticException(node.getPosition(), "lvalue required as left operand of " + node.getOp());
				if(lhsType instanceof ArrayType && rhsType instanceof NullType)
					node.setType(SymbolTableAssistant.nullType);
				else
				if(lhsType instanceof ClassType && rhsType instanceof NullType)
					node.setType(SymbolTableAssistant.nullType);
				else
				if(lhsType == rhsType && !(lhsType instanceof NullType))
					node.setType(lhsType);
				else
					throw new SemanticException(node.getPosition(), "invalid operands of types '" + lhsType.getIdentifier() + "' and '" + rhsType.getIdentifier() + "' to binary operator " + node.getOp());
				break;

			case LAND :
			case LOR :
				if(lhsType instanceof BoolType
						&& rhsType instanceof BoolType)
					node.setType(SymbolTableAssistant.boolType);
				else
					throw new SemanticException(node.getPosition(), "invalid operands of types '" + lhsType.getIdentifier() + "' and '" + rhsType.getIdentifier() + "' to binary operator " + node.getOp());
				break;
		}
	}

	public void visit(UnaryExprNode node){
		node.getOpr().accept(this);
		Type type = node.getOpr().getType();
		switch (node.getOp()){
			case POS_INC :
			case POS_SUB :
				if(node.getOpr().getValueCategory() != ExprNode.category.LVALUE)
					throw new SemanticException(node.getPosition(), "lvalue required as left operand of " + node.getOp());
				if(type instanceof IntType)
					node.setType(SymbolTableAssistant.intType);
				else
					throw new SemanticException(node.getPosition(), "wrong type argument '" + type.getIdentifier() + "' to '" + node.getOp() + "'");
				break;

			case PRE_INC :
			case PRE_SUB :
				node.setValueCategory(ExprNode.category.LVALUE);
				if(node.getOpr().getValueCategory() != ExprNode.category.LVALUE)
					throw new SemanticException(node.getPosition(), "lvalue required as left operand of " + node.getOp());
				if(type instanceof IntType)
					node.setType(SymbolTableAssistant.intType);
				else
					throw new SemanticException(node.getPosition(), "wrong type argument '" + type.getIdentifier() + "' to '" + node.getOp() + "'");
				break;

			case PLU :
			case NEG :
			case COM :
				if(type instanceof IntType)
					node.setType(SymbolTableAssistant.intType);
				else
					throw new SemanticException(node.getPosition(), "wrong type argument '" + type.getIdentifier() + "' to '" + node.getOp() + "'");
				break;

			case NOT :
				if(type instanceof BoolType)
					node.setType(SymbolTableAssistant.boolType);
				else
					throw new SemanticException(node.getPosition(), "wrong type argument '" + type.getIdentifier() + "' to '" + node.getOp() + "'");
				break;
		}
	}

}
