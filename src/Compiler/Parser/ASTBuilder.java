package Compiler.Parser;

import Compiler.AST.BaseNode;
import Compiler.AST.DeclNode;
import Compiler.AST.ProgramNode;
import Compiler.Parser.MxstarBaseVisitor;
import Compiler.Parser.MxstarParser;
import Compiler.utils.Position;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends MxstarBaseVisitor<BaseNode> {

    @Override public BaseNode visitProgram(MxstarParser.ProgramContext ctx) {
        List<DeclNode> declList = new ArrayList<>();
        if(ctx.declaration() != null){
            for(ParserRuleContext decl : ctx.declaration()){
                BaseNode declNode = visit(decl);
                // TODO: check if it is of variable declaration containing multiple declarations in one line
                declList.add((DeclNode)declNode);
            }
        }
        return new ProgramNode(new Position(ctx.getStart()), declList);
    }

    @Override public BaseNode visitDeclaration(MxstarParser.DeclarationContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitClassDecl(MxstarParser.ClassDeclContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitFunctionDecl(MxstarParser.FunctionDeclContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitVariableDecl(MxstarParser.VariableDeclContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitVariableList(MxstarParser.VariableListContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitSingleVariable(MxstarParser.SingleVariableContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitParaDeclList(MxstarParser.ParaDeclListContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitSingleParaDecl(MxstarParser.SingleParaDeclContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitParaList(MxstarParser.ParaListContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitFunctionType(MxstarParser.FunctionTypeContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitArrayType(MxstarParser.ArrayTypeContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitNArrayType(MxstarParser.NArrayTypeContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitNArrayTypeInt(MxstarParser.NArrayTypeIntContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitNArrayTypeBool(MxstarParser.NArrayTypeBoolContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitNArrayTypeString(MxstarParser.NArrayTypeStringContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitNArrayTypeId(MxstarParser.NArrayTypeIdContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitBlockStmt(MxstarParser.BlockStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitVarDeclStmt(MxstarParser.VarDeclStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitExprStmt(MxstarParser.ExprStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitControlStmt(MxstarParser.ControlStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitCompoundStmt(MxstarParser.CompoundStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitIfStmt(MxstarParser.IfStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitForStmt(MxstarParser.ForStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitWhileStmt(MxstarParser.WhileStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitReturnStmt(MxstarParser.ReturnStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitContinueStmt(MxstarParser.ContinueStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitBreakStmt(MxstarParser.BreakStmtContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitConstExpr(MxstarParser.ConstExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitSubscriptExpr(MxstarParser.SubscriptExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitFunctionCallExpr(MxstarParser.FunctionCallExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitIdExpr(MxstarParser.IdExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitSubExpr(MxstarParser.SubExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitBinaryExpr(MxstarParser.BinaryExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitNewExpr(MxstarParser.NewExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitPostfixOpExpr(MxstarParser.PostfixOpExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitUnaryOpExpr(MxstarParser.UnaryOpExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitMemberAccessExpr(MxstarParser.MemberAccessExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitThisExpr(MxstarParser.ThisExprContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitArrayCreator(MxstarParser.ArrayCreatorContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitNArrayCreator(MxstarParser.NArrayCreatorContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitClassCreator(MxstarParser.ClassCreatorContext ctx) { return visitChildren(ctx); }

    @Override public BaseNode visitIntegerConstant(MxstarParser.IntegerConstantContext ctx) { return visitChildren(ctx); }

}
