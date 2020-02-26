package Compiler.SemanticAnalysis;

import Compiler.AST.*;

public class ASTBaseVisitor implements ASTVisitor{

	public void visit(ArrayIdxExprNode node){}
	public void visit(ArrayTypeNode node){}
	public void visit(BinaryExprNode node){}
	public void visit(BoolConstExprNode node){}
	public void visit(BoolTypeNode node){}
	public void visit(BreakStmtNode node){}
	public void visit(ClassDeclNode node){}
	public void visit(ClassTypeNode node){}
	public void visit(CompStmtNode node){}
	public void visit(ContStmtNode node){}
	public void visit(ExprStmtNode node){}
	public void visit(ForStmtNode node){}
	public void visit(FuncCallExprNode node){}
	public void visit(FuncDeclNode node){}
	public void visit(IdExprNode node){}
	public void visit(IfStmtNode node){}
	public void visit(IntConstExprNode node){}
	public void visit(IntTypeNode node){}
	public void visit(MemberExprNode node){}
	public void visit(NewExprNode node){}
	public void visit(NullConstExprNode node){}
	public void visit(ProgramNode node){}
	public void visit(ReturnStmtNode node){}
	public void visit(StringConstExprNode node){}
	public void visit(StringTypeNode node){}
	public void visit(ThisExprNode node){}
	public void visit(UnaryExprNode node){}
	public void visit(VarDeclListNode node){}
	public void visit(VarDeclNode node){}
	public void visit(VarDeclStmtNode node){}
	public void visit(VoidTypeNode node){}
	public void visit(WhileStmtNode node){}

}
