package Compiler.AST;

import Compiler.utils.Position;

import java.util.List;

public class ProgramNode extends BaseNode {

    private List<DeclNode> decls;

    public ProgramNode(Position position, List<DeclNode> decls){
        super(position);
        this.decls = decls;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

}
