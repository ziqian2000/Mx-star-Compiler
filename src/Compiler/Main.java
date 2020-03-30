package Compiler;

import Compiler.AST.BaseNode;
import Compiler.IR.IR;
import Compiler.IRInterpreter.IRInterpreter;
import Compiler.IRVisitor.*;
import Compiler.Parser.MxstarErrorListener;
import Compiler.SemanticAnalysis.*;
import Compiler.Parser.MxstarLexer;
import Compiler.Parser.MxstarParser;
import Compiler.SymbolTable.Scope;
import Compiler.Utils.FuckingException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {

        try {

            // arguments
            boolean ifInterpret = true;
            for(String arg : args){
                switch (arg){
                    case "-semantic": ifInterpret = false; break;
                    case "-codegen" : break;
                    default: throw new FuckingException("unrecognizable argument : " + arg);
                }
            }

            // parse
            InputStream inputStream = new FileInputStream("code.txt");
            CharStream charStream = CharStreams.fromStream(inputStream);
            MxstarLexer mxstarLexer = new MxstarLexer(charStream);
            mxstarLexer.removeErrorListeners();
            mxstarLexer.addErrorListener(new MxstarErrorListener());
            CommonTokenStream commonTokenStream = new CommonTokenStream(mxstarLexer);
            MxstarParser mxstarParser = new MxstarParser(commonTokenStream);
            mxstarParser.removeErrorListeners();
            mxstarParser.addErrorListener(new MxstarErrorListener());
            ParseTree parseTree = mxstarParser.program();           // construct a parse tree

            // AST generation, semantic analysis
            BaseNode astRoot = new ASTBuilder().visit(parseTree);   // construct an AST
            Scope topScope = new Scope(null);           // the global scope
            SymbolTableAssistant.addBuiltinFunction(topScope);      // add some builtin functions into symbol table
            astRoot.accept(new ClassDeclVisitor(topScope));         // add all classes into symbol table
            astRoot.accept(new GlobalFuncDeclVisitor(topScope));    // add all global functions into symbol table
            astRoot.accept(new ClassMemberVisitor(topScope));       // add all class members into symbol table
            astRoot.accept(new SymbolTableBuilder(topScope));       // build symbol table, assign symbol, calculate type and determine value category
            astRoot.accept(new SemanticInfoVisitor(topScope));      // some other semantic exceptions

            // IR generation
            astRoot.accept(new FunctionVisitor());                  // declare function
            astRoot.accept(new MemberOffsetCalculator());           // calculate offset & class size
            IRGenerator irGenerator = new IRGenerator(topScope);
            IRAssistant.addBuiltinFunction(irGenerator.getIR(), topScope, SymbolTableAssistant.stringBuiltinFuncScope, SymbolTableAssistant.arrayBuiltinFuncScope);
                                                                    // add builtin-function
            astRoot.accept(irGenerator);                            // generate IR
            IR ir = irGenerator.getIR();

            // Optimization on IR
            new FunctionInlining(ir).run();
            new GlobalVariableResolving(ir).run();                  // a must-do, otherwise modify irGenerator

            // print and test
            new IRPrinter().run(ir, new PrintStream("ir.txt"));
            if(ifInterpret) IRInterpreter.main("ir.txt", System.out, new FileInputStream("in.txt"), false);

        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }


    }

}
