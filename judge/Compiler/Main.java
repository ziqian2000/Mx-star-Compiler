package Compiler;

import Compiler.AST.BaseNode;
import Compiler.Assembly.Assembly;
import Compiler.Codegen.*;
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
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        try {

            // arguments
            boolean semanticOnly = false;
            for(String arg : args){
                switch (arg){
                    case "-semantic": semanticOnly = true; break;
                    case "-codegen" : semanticOnly = false; break;
                    default: throw new FuckingException("Unrecognizable argument : " + arg);
                }
            }

            // ==================== Semantic Analysis ====================

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

            if(semanticOnly) return;

            // ==================== IR Generation ====================

            // IR generation
            astRoot.accept(new FunctionVisitor());                  // declare function
            astRoot.accept(new MemberOffsetCalculator());           // calculate offset & class size
            IRGenerator irGenerator = new IRGenerator(topScope);
            IRAssistant.addBuiltinFunction(irGenerator.getIR(), topScope, SymbolTableAssistant.stringBuiltinFuncScope, SymbolTableAssistant.arrayBuiltinFuncScope);
                                                                    // add builtin-function
            astRoot.accept(irGenerator);                            // generate IR
            IR ir = irGenerator.getIR();

            // Naive optimization on IR
            new FunctionInlining(ir).run();
            new GlobalVariableResolving(ir).run();                  // a must-do, otherwise modify irGenerator

            // Basic Optimization on IR
            new SSAConstructor(ir).run();
            new CFGSimplifier(ir).run();
            new SSADestructor(ir).run();                            // seems to have problems for strange IR, but amazingly passes all tests...
                                                                    // need to eliminate useless variable like "extra" in sequentialization
            new CFGSimplifier(ir).run();

            // print and test
//            new IRPrinter().run(ir, new PrintStream("ir.txt"));
//            IRInterpreter.main("ir.txt", System.out, new FileInputStream("test.in"), ir.getSSAForm());

            System.err.println("IR done: "+(System.currentTimeMillis() - startTime)+"ms");

            // ==================== Codegen ====================

            InstructionSelector instructionSelector = new InstructionSelector(ir);
            instructionSelector.run();
            Assembly asm = instructionSelector.getAsm();
            System.err.println("Instruction selection done: "+(System.currentTimeMillis() - startTime)+"ms");
            new RegisterAllocator(asm).run();
            System.err.println("Register allocation done: "+(System.currentTimeMillis() - startTime)+"ms");
			new FinalProcessing(asm).run();
			new PeepholeOptimization(asm).run();
			new AsmPrinter(asm).run(new PrintStream("test.s"));


        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.err.println("All done: "+(System.currentTimeMillis() - startTime)+"ms");


    }

}
