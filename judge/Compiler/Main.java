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
            ParseTree parseTree = mxstarParser.program();           // construct a parse tree

            BaseNode astRoot = new ASTBuilder().visit(parseTree);   // construct an AST

            Scope topScope = new Scope(null);           // the global scope
            SymbolTableAssistant.addBuiltinFunction(topScope);      // add some builtin functions into symbol table
            astRoot.accept(new ClassDeclVisitor(topScope));         // add all classes into symbol table
            astRoot.accept(new GlobalFuncDeclVisitor(topScope));    // add all global functions into symbol table
            astRoot.accept(new ClassMemberVisitor(topScope));       // add all class members into symbol table
            astRoot.accept(new SymbolTableVisitor(topScope));       // build symbol table, assign symbol, calculate type and determine value category
            // assign type
            // check type

        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }


    }
}
