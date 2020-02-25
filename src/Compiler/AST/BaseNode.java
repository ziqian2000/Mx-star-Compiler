package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.utils.Position;

public abstract class BaseNode {

    private Position position;

    public BaseNode(Position position){
        this.position = position;
    }

    public Position getPosition(){
        return position;
    }

    public abstract void accept(ASTVisitor astVisitor);

}
