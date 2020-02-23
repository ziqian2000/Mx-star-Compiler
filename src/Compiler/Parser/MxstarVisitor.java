// Generated from C:/Users/runzhe/IdeaProjects/Compiler/src/Compiler/Parser\Mxstar.g4 by ANTLR 4.8
package Compiler.Parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxstarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxstarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxstarParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MxstarParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(MxstarParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#classDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDecl(MxstarParser.ClassDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(MxstarParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#variableDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDecl(MxstarParser.VariableDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#variableList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableList(MxstarParser.VariableListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#singleVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleVariable(MxstarParser.SingleVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#paraDeclList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParaDeclList(MxstarParser.ParaDeclListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#singleParaDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleParaDecl(MxstarParser.SingleParaDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#paraList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParaList(MxstarParser.ParaListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#functionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionType(MxstarParser.FunctionTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayType}
	 * labeled alternative in {@link MxstarParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(MxstarParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NArrayType}
	 * labeled alternative in {@link MxstarParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNArrayType(MxstarParser.NArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NArrayTypeInt}
	 * labeled alternative in {@link MxstarParser#nonArrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNArrayTypeInt(MxstarParser.NArrayTypeIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NArrayTypeBool}
	 * labeled alternative in {@link MxstarParser#nonArrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNArrayTypeBool(MxstarParser.NArrayTypeBoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NArrayTypeString}
	 * labeled alternative in {@link MxstarParser#nonArrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNArrayTypeString(MxstarParser.NArrayTypeStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NArrayTypeId}
	 * labeled alternative in {@link MxstarParser#nonArrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNArrayTypeId(MxstarParser.NArrayTypeIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmt(MxstarParser.BlockStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarDeclStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclStmt(MxstarParser.VarDeclStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStmt(MxstarParser.ExprStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ControlStmt}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlStmt(MxstarParser.ControlStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxstarParser#compoundStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStmt(MxstarParser.CompoundStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStmt}
	 * labeled alternative in {@link MxstarParser#controlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(MxstarParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStmt}
	 * labeled alternative in {@link MxstarParser#controlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(MxstarParser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link MxstarParser#controlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(MxstarParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReturnStmt}
	 * labeled alternative in {@link MxstarParser#controlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(MxstarParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ContinueStmt}
	 * labeled alternative in {@link MxstarParser#controlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(MxstarParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BreakStmt}
	 * labeled alternative in {@link MxstarParser#controlStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(MxstarParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstExpr(MxstarParser.ConstExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubscriptExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscriptExpr(MxstarParser.SubscriptExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionCallExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpr(MxstarParser.FunctionCallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExpr(MxstarParser.IdExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubExpr(MxstarParser.SubExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpr(MxstarParser.BinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExpr(MxstarParser.NewExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PostfixOpExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixOpExpr(MxstarParser.PostfixOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryOpExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOpExpr(MxstarParser.UnaryOpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MemberAccessExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberAccessExpr(MxstarParser.MemberAccessExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ThisExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisExpr(MxstarParser.ThisExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreator(MxstarParser.ArrayCreatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NArrayCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNArrayCreator(MxstarParser.NArrayCreatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ClassCreator}
	 * labeled alternative in {@link MxstarParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassCreator(MxstarParser.ClassCreatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerConstant}
	 * labeled alternative in {@link MxstarParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerConstant(MxstarParser.IntegerConstantContext ctx);
}