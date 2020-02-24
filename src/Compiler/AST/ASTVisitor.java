package Compiler.AST;

import Compiler.AST.ProgramNode;

public interface ASTVisitor {
    void visit(ProgramNode node);
    // TODO: a lot of visit() to decl...
}
