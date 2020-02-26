package Compiler;

import Compiler.AST.BaseNode;
import Compiler.SemanticAnalysis.*;
import Compiler.Parser.MxstarLexer;
import Compiler.Parser.MxstarParser;
import Compiler.SymbolTable.Scope;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {

        try {
            InputStream inputStream = new FileInputStream("data.in");
            CharStream charStream = CharStreams.fromStream(inputStream);
            MxstarLexer mxstarLexer = new MxstarLexer(charStream);
            CommonTokenStream commonTokenStream = new CommonTokenStream(mxstarLexer);
            MxstarParser mxstarParser = new MxstarParser(commonTokenStream);
            ParseTree parseTree = mxstarParser.program();

            ASTBuilder astBuilder = new ASTBuilder();
            BaseNode astRoot = astBuilder.visit(parseTree);

            Scope topScope = new Scope(null);
            astRoot.accept(new ClassDeclVisitor(topScope));
            astRoot.accept(new GlobalFuncDeclVisitor(topScope));
            astRoot.accept(new ClassMemberVisitor(topScope));
//            astRoot.accept(new SymbolTableVisitor(topScope));

        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }


    }
}
