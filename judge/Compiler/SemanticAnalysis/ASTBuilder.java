package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.Parser.MxstarBaseVisitor;
import Compiler.Parser.MxstarParser;
import Compiler.Utils.Position;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends MxstarBaseVisitor<BaseNode> {

    @Override public BaseNode visitProgram(MxstarParser.ProgramContext ctx) {
        List<DeclNode> declList = new ArrayList<>();
        if(ctx.declaration() != null){
            for(ParserRuleContext decl : ctx.declaration()){
                BaseNode declNode = visit(decl);
                if(declNode instanceof VarDeclListNode) declList.addAll(((VarDeclListNode)declNode).getVarList());
                else declList.add((DeclNode)declNode);
            }
        }
        return new ProgramNode(new Position(ctx.getStart()), declList);
    }

    @Override public BaseNode visitDeclaration(MxstarParser.DeclarationContext ctx) {
        if(ctx.classDecl() != null) return visit(ctx.classDecl());
        else if(ctx.functionDecl() != null) return visit(ctx.functionDecl());
        else if(ctx.variableDecl() != null) return visit(ctx.variableDecl());
        else return null;
    }

    @Override public BaseNode visitClassDecl(MxstarParser.ClassDeclContext ctx) {
        String identifier = ctx.Identifier().getText();
        List<FuncDeclNode> funcDeclList = new ArrayList<>();
        List<VarDeclNode> varDeclList = new ArrayList<>();
        for(ParserRuleContext decl : ctx.functionDecl()){
            funcDeclList.add((FuncDeclNode)visit(decl));
        }
        for(ParserRuleContext decl : ctx.variableDecl()){
            varDeclList.addAll(((VarDeclListNode)visit(decl)).getVarList());
        }
        return new ClassDeclNode(new Position(ctx.getStart()), identifier, funcDeclList, varDeclList);
    }

    @Override public BaseNode visitFunctionDecl(MxstarParser.FunctionDeclContext ctx) {
        return new FuncDeclNode(new Position(ctx.getStart()),
                                ctx.functionType() != null ? (TypeNode) visit(ctx.functionType()) : null,
                                ctx.Identifier().getText(),
                                ctx.paraDeclList() != null ? (VarDeclListNode) visit(ctx.paraDeclList()) : null,
                                (StmtNode) visit(ctx.compoundStmt()));
    }

    @Override public BaseNode visitVariableDecl(MxstarParser.VariableDeclContext ctx) {
        TypeNode typeNode = (TypeNode)visit(ctx.type());
        VarDeclListNode varDeclListNode = (VarDeclListNode)visit(ctx.variableList());
        varDeclListNode.setType(typeNode);
        return varDeclListNode;
    }

    @Override public BaseNode visitVariableList(MxstarParser.VariableListContext ctx) {
        VarDeclListNode varDeclListNode = new VarDeclListNode(new Position(ctx.getStart()));
        for(ParserRuleContext singleVar : ctx.singleVariable()){
            varDeclListNode.addVarDecl((VarDeclNode) visit(singleVar));
        }
        return varDeclListNode;
    }

    @Override public BaseNode visitSingleVariable(MxstarParser.SingleVariableContext ctx) {
        String identifier = ctx.Identifier().getText();
        ExprNode expr = ctx.expression() != null ? (ExprNode) visit(ctx.expression()) : null;
        return new VarDeclNode(new Position(ctx.getStart()), null, identifier, expr);
    }

    @Override public BaseNode visitParaDeclList(MxstarParser.ParaDeclListContext ctx) {
        VarDeclListNode varDeclListNode = new VarDeclListNode(new Position(ctx.getStart()));
        for(ParserRuleContext singleVar : ctx.singleParaDecl()){
            varDeclListNode.addVarDecl((VarDeclNode) visit(singleVar));
        }
        return varDeclListNode;
    }

    @Override public BaseNode visitSingleParaDecl(MxstarParser.SingleParaDeclContext ctx) {
        String identifier = ctx.Identifier().getText();
        TypeNode type = (TypeNode) visit(ctx.type());
        return new VarDeclNode(new Position(ctx.getStart()), type, identifier, null);
    }

    @Override public BaseNode visitParaList(MxstarParser.ParaListContext ctx) {
        // inaccessible method
        return null;
    }

    @Override public BaseNode visitFunctionType(MxstarParser.FunctionTypeContext ctx) {
        if(ctx.type() != null) return visit(ctx.type());
        else return new VoidTypeNode(new Position(ctx.getStart()));
    }

    @Override public BaseNode visitArrayType(MxstarParser.ArrayTypeContext ctx) {
        TypeNode type = (TypeNode)visit(ctx.type());
        return new ArrayTypeNode(new Position(ctx.getStart()), type);
    }

    @Override public BaseNode visitNArrayType(MxstarParser.NArrayTypeContext ctx) {
        return visit(ctx.nonArrayType());
    }

    @Override public BaseNode visitNArrayTypeInt(MxstarParser.NArrayTypeIntContext ctx) {
        return new IntTypeNode(new Position(ctx.getStart()));
    }

    @Override public BaseNode visitNArrayTypeBool(MxstarParser.NArrayTypeBoolContext ctx) {
        return new BoolTypeNode(new Position(ctx.getStart()));
    }

    @Override public BaseNode visitNArrayTypeString(MxstarParser.NArrayTypeStringContext ctx) {
        return new StringTypeNode(new Position(ctx.getStart()));
    }
    @Override public BaseNode visitNArrayTypeId(MxstarParser.NArrayTypeIdContext ctx) {
        return new ClassTypeNode(new Position(ctx.getStart()), ctx.getText());
    }

    @Override public BaseNode visitBlockStmt(MxstarParser.BlockStmtContext ctx) {
        return visit(ctx.compoundStmt());
    }

    @Override public BaseNode visitVarDeclStmt(MxstarParser.VarDeclStmtContext ctx) {
        return new VarDeclStmtNode(new Position(ctx.getStart()), (VarDeclListNode) visit(ctx.variableDecl()));
    }

    @Override public BaseNode visitExprStmt(MxstarParser.ExprStmtContext ctx) {
        return new ExprStmtNode(new Position(ctx.getStart()), (ExprNode) visit(ctx.expression()));
    }

    @Override public BaseNode visitControlStmt(MxstarParser.ControlStmtContext ctx) {
        return visit(ctx.controlStatement());
    }

    @Override public BaseNode visitCompoundStmt(MxstarParser.CompoundStmtContext ctx) {
        List<StmtNode> stmtList = new ArrayList<>();
        for(ParserRuleContext stmt : ctx.statement()){
            stmtList.add((StmtNode) visit(stmt));
        }
        return new CompStmtNode(new Position(ctx.getStart()), stmtList);
    }

    @Override public BaseNode visitIfStmt(MxstarParser.IfStmtContext ctx) {
        return new IfStmtNode(new Position(ctx.getStart()),
                                (ExprNode) visit(ctx.expression()),
                                (StmtNode) visit(ctx.thenStmt),
                                ctx.elseStmt != null ? (StmtNode)visit(ctx.elseStmt) : null);
    }

    @Override public BaseNode visitForStmt(MxstarParser.ForStmtContext ctx) {
        return new ForStmtNode(new Position(ctx.getStart()),
                                ctx.init != null ? (ExprNode) visit(ctx.init) : null,
                                ctx.cond != null ? (ExprNode) visit(ctx.cond) : null,
                                ctx.step != null ? (ExprNode) visit(ctx.step) : null,
                                (StmtNode) visit(ctx.statement()));
    }

    @Override public BaseNode visitWhileStmt(MxstarParser.WhileStmtContext ctx) {
        return new WhileStmtNode(new Position(ctx.getStart()),
                                (ExprNode) visit(ctx.expression()),
                                (StmtNode) visit(ctx.statement()));
    }

    @Override public BaseNode visitReturnStmt(MxstarParser.ReturnStmtContext ctx) {
        return new ReturnStmtNode(new Position(ctx.getStart()));
    }

    @Override public BaseNode visitContinueStmt(MxstarParser.ContinueStmtContext ctx) {
        return new ContStmtNode(new Position(ctx.getStart()));
    }

    @Override public BaseNode visitBreakStmt(MxstarParser.BreakStmtContext ctx) {
        return new BreakStmtNode(new Position(ctx.getStart()));
    }

    @Override public BaseNode visitConstExpr(MxstarParser.ConstExprContext ctx) {
        return visit(ctx.constant());
    }

    @Override public BaseNode visitSubscriptExpr(MxstarParser.SubscriptExprContext ctx) {
        ExprNode array = (ExprNode) visit(ctx.array);
        ExprNode index = (ExprNode) visit(ctx.index);
        return new ArrayIdxExprNode(new Position(ctx.getStart()), array, index);
    }

    @Override public BaseNode visitFunctionCallExpr(MxstarParser.FunctionCallExprContext ctx) {
        ExprNode function = (ExprNode) visit(ctx.expression());
        List<ExprNode> paraList = new ArrayList<>();
        if(ctx.paraList() != null)
            for(ParserRuleContext expr : ctx.paraList().expression()){
                paraList.add((ExprNode) visit(expr));
            }
        return new FuncCallExprNode(new Position(ctx.getStart()), function, paraList);
    }

    @Override public BaseNode visitIdExpr(MxstarParser.IdExprContext ctx) {
        return new IdExprNode(new Position(ctx.getStart()), ctx.Identifier().getText());
    }

    @Override public BaseNode visitSubExpr(MxstarParser.SubExprContext ctx) {
        return visit(ctx.expression());
    }

    @Override public BaseNode visitBinaryExpr(MxstarParser.BinaryExprContext ctx) {
        ExprNode lhs = (ExprNode) visit(ctx.lhs), rhs = (ExprNode) visit(ctx.rhs);
        BinaryExprNode.Op op;
        switch (ctx.op.getText()){
            case "*" : op = BinaryExprNode.Op.MUL; break;
            case "/" : op = BinaryExprNode.Op.DIV; break;
            case "+" : op = BinaryExprNode.Op.ADD; break;
            case "-" : op = BinaryExprNode.Op.SUB; break;
            case "%" : op = BinaryExprNode.Op.MOD; break;
            case "<<" : op = BinaryExprNode.Op.SHL; break;
            case ">>" : op = BinaryExprNode.Op.SHR; break;
            case "<" : op = BinaryExprNode.Op.LT; break;
            case "<=" : op = BinaryExprNode.Op.LE; break;
            case ">" : op = BinaryExprNode.Op.GT; break;
            case ">=" : op = BinaryExprNode.Op.GE; break;
            case "==" : op = BinaryExprNode.Op.EQ; break;
            case "!=" : op = BinaryExprNode.Op.NEQ; break;
            case "=" : op = BinaryExprNode.Op.ASS; break;
            case "&" : op = BinaryExprNode.Op.AND; break;
            case "|" : op = BinaryExprNode.Op.OR; break;
            case "^" : op = BinaryExprNode.Op.XOR; break;
            case "&&" : op = BinaryExprNode.Op.LAND; break;
            case "||" : op = BinaryExprNode.Op.LOR; break;
            default : op = null; break;
        }
        return new BinaryExprNode(new Position(ctx.getStart()), op, lhs, rhs);
    }

    @Override public BaseNode visitNewExpr(MxstarParser.NewExprContext ctx) {
        return visit(ctx.creator());
    }

    @Override public BaseNode visitPostfixOpExpr(MxstarParser.PostfixOpExprContext ctx) {
        ExprNode opr = (ExprNode) visit(ctx.expression());
        UnaryExprNode.Op op;
        switch (ctx.op.getText()){
            case "++" : op = UnaryExprNode.Op.POS_INC; break;
            case "--" : op = UnaryExprNode.Op.POS_SUB; break;
            default : op = null; break;
        }
        return new UnaryExprNode(new Position(ctx.getStart()), op, opr);
    }

    @Override public BaseNode visitUnaryOpExpr(MxstarParser.UnaryOpExprContext ctx) {
        ExprNode opr = (ExprNode) visit(ctx.expression());
        UnaryExprNode.Op op;
        switch (ctx.op.getText()){
            case "++" : op = UnaryExprNode.Op.PRE_INC; break;
            case "--" : op = UnaryExprNode.Op.PRE_SUB; break;
            case "+" : op = UnaryExprNode.Op.PLU; break;
            case "-" : op = UnaryExprNode.Op.NEG; break;
            case "!" : op = UnaryExprNode.Op.NOT; break;
            case "~" : op = UnaryExprNode.Op.COM; break;
            default : op = null; break;
        }
        return new UnaryExprNode(new Position(ctx.getStart()), op, opr);
    }

    @Override public BaseNode visitMemberAccessExpr(MxstarParser.MemberAccessExprContext ctx) {
        ExprNode exprNode = (ExprNode) visit(ctx.expression());
        return new MemberExprNode(new Position(ctx.getStart()), exprNode, ctx.Identifier().getText());
    }

    @Override public BaseNode visitThisExpr(MxstarParser.ThisExprContext ctx) {
        return new ThisExprNode(new Position(ctx.getStart()));
    }

    @Override public BaseNode visitArrayCreator(MxstarParser.ArrayCreatorContext ctx) {
        int dim = (ctx.getChildCount() - ctx.expression().size() - 1) >> 1;
        List<ExprNode> exprList = new ArrayList<>();
        for(ParserRuleContext expr : ctx.expression()){
            exprList.add((ExprNode) visit(expr));
        }
        return new NewExprNode(new Position(ctx.getStart()), (TypeNode)visit(ctx.nonArrayType()), dim, exprList);
    }

    @Override public BaseNode visitNArrayCreator(MxstarParser.NArrayCreatorContext ctx) {
        return new NewExprNode(new Position(ctx.getStart()), (TypeNode)visit(ctx.nonArrayType()), 0, null);
    }

    @Override public BaseNode visitClassCreator(MxstarParser.ClassCreatorContext ctx) {
        return new NewExprNode(new Position(ctx.getStart()), (TypeNode)visit(ctx.nonArrayType()), 0, null);
    }

    @Override public BaseNode visitIntegerConstant(MxstarParser.IntegerConstantContext ctx) {
        return new IntConstExprNode(new Position(ctx.getStart()), Integer.parseInt(ctx.IntegerConstant().getText()));
    }

    @Override public BaseNode visitStringConstant(MxstarParser.StringConstantContext ctx) {
        return new StringConstExprNode(new Position(ctx.getStart()), ctx.StringConstant().getText());
    }

    @Override public BaseNode visitNullConstant(MxstarParser.NullConstantContext ctx) {
        return new NullConstExprNode(new Position(ctx.getStart()));
    }

    @Override public BaseNode visitBoolConstant(MxstarParser.BoolConstantContext ctx) {
        return new BoolConstExprNode(new Position(ctx.getStart()), Boolean.parseBoolean(ctx.BoolConstant().getText()));
    }
}
