package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.utils.Position;

import java.util.List;

public class ProgramNode extends BaseNode {

    private List<DeclNode> declList;

    public ProgramNode(Position position, List<DeclNode> decls){
        super(position);
        this.declList = decls;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

}
