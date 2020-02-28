package Compiler.Utils;

import org.antlr.v4.runtime.Token;

public class Position {
    private int line, column;

    public Position(Token token){
        this.line = token.getLine();
        this.column = token.getCharPositionInLine();
    }

    public Position(int line, int column){
        this.line = line;
        this.column = column;
    }

    public int getLine(){
        return this.line;
    }

    public int getColumn(){
        return this.column;
    }

    public String toString(){
        return line + ":" + column;
    }

}
