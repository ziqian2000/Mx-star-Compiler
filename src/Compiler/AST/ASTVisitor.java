package Compiler.AST;

public interface ASTVisitor {
    void visit(ProgramNode node);
    void visit(IntTypeNode node);
    void visit(VoidTypeNode node);
    void visit(StringTypeNode node);
    void visit(BoolTypeNode node);
    void visit(ArrayTypeNode node);
    void visit(VarDeclNode node);
    void visit(VarDeclListNode node);
    void visit(IdExprNode node);
    void visit(ThisExprNode node);
    void visit(IntConstExprNode node);
    void visit(BoolConstExprNode node);
    void visit(StringConstExprNode node);
    void visit(NullConstExprNode node);
    void visit(MemberExprNode node);
    void visit(ArrayIdxExprNode node);
    void visit(FuncCallExprNode node);
    void visit(NewExprNode node);
    void visit(ClassTypeNode node);
    void visit(IfStmtNode node);
    void visit(ForStmtNode node);
    void visit(WhileStmtNode node);
    void visit(ReturnStmtNode node);
    void visit(BreakStmtNode node);
    void visit(ContStmtNode node);
    void visit(CompStmtNode node);
    void visit(VarDeclStmtNode node);
    void visit(ExprStmtNode node);
    void visit(BinaryExprNode node);
    void visit(UnaryExprNode node);
    void visit(FuncDeclNode node);
    void visit(ClassDeclNode node);
}
