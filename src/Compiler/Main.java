package Compiler;

import Compiler.Parser.MxstarLexer;
import Compiler.Parser.MxstarParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("data.in");
        CharStream charStream = CharStreams.fromStream(inputStream);
        MxstarLexer mxstarLexer = new MxstarLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(mxstarLexer);
        MxstarParser mxstarParser = new MxstarParser(commonTokenStream);
        ParseTree parseTree = mxstarParser.program();

        for(Token t: commonTokenStream.getTokens()){
            System.out.println(t);
        }

    }
}
