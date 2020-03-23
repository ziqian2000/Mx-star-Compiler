package Compiler.AST;

import Compiler.IR.Operand.Operand;
import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

public abstract class BaseNode {

    private Position position;

    // for IR generator
    private Operand resultOpr;

    public BaseNode(Position position){
        this.position = position;
    }

    public Position getPosition(){
        return position;
    }

    public void setResultOpr(Operand resultOpr) {
        this.resultOpr = resultOpr;
    }

    public Operand getResultOpr() {
        return resultOpr;
    }

    public abstract void accept(ASTVisitor astVisitor);

}
