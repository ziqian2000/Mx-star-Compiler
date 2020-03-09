package Compiler.IRVisitor;

import Compiler.AST.*;
import Compiler.IR.*;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.*;
import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.*;
import Compiler.Utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IRGenerator implements ASTVisitor {

	private Scope scope;
	private IR ir;
	private BasicBlock curBB;
	private Stack<BasicBlock> loopContStack = new Stack<>();
	private Stack<BasicBlock> loopBreakStack = new Stack<>();

	public IRGenerator(Scope scope){
		this.scope = scope;
		this.ir = new IR();
	}

	public IR getIR(){
		return ir;
	}

	// visit

	public void visit(ProgramNode node){

		// global variable
		for(DeclNode decl : node.getDeclList()) {
			if (decl instanceof VarDeclNode) {
				// TODO: the memory will be allocated in next pass
				VarSymbol varSymbol = (VarSymbol)((VarDeclNode) decl).getSymbol();
				Type varType = varSymbol.getVarType();
				I32Value var = new I32Value(((VarDeclNode) decl).getIdentifier());
				varSymbol.setStorage(var);
				ir.addGlobalVar(var);
			}
		}

		// function & class
		for(DeclNode decl : node.getDeclList()) {
			if(!(decl instanceof VarDeclNode))
				decl.accept(this);
		}
	}

	public void visit(FuncDeclNode node){
		FuncSymbol funcSymbol = node.getFuncSymbol();
		scope = funcSymbol.getBodyScope();
		ClassType classType = scope.getCurrentClassType();
		Function function = new Function((classType == null ? "" : classType.getIdentifier() + ".") + node.getIdentifier());
		function.setIsMemberFunc(classType != null);
		funcSymbol.setFunction(function);
		ir.addFunction(function);

		// add parameters
		if(function.getIsMemberFunc())
			function.setObj(new I32Value("this"));
		for(VarSymbol varSymbol : funcSymbol.getParaSymbolList()){
			Type varType = varSymbol.getVarType();
			function.addParameter(new I32Value(varSymbol.getIdentifier()));
		}

		BasicBlock entryBB = new BasicBlock();
		function.setEntryBB(entryBB);
		curBB = entryBB;
		node.getBodyStmt().accept(this);

		scope = scope.getUpperScope();
		// TODO: may have no return value
	}

	public void visit(ClassDeclNode node){
		scope = node.getClassSymbol().getBodyScope();
		for(FuncDeclNode funcDecl : node.getFuncDeclList()){
			funcDecl.accept(this);
		}
		scope = scope.getUpperScope();
	}

	public void visit(IfStmtNode node){
		scope = node.getBodyScope();

		node.getCond().accept(this);
		Operand resultOpr = node.getCond().getResultOpr();
		if(node.getElseStmt() == null) {
			BasicBlock thenBB = new BasicBlock();
			BasicBlock exitBB = new BasicBlock();
			curBB.addLastInst(new Branch(resultOpr, thenBB, exitBB));

			curBB = thenBB;
			node.getThenStmt().accept(this);
			curBB.addLastInst(new Jump(exitBB));

			curBB = exitBB;
		}
		else{
			BasicBlock thenBB = new BasicBlock();
			BasicBlock elseBB = new BasicBlock();
			BasicBlock exitBB = new BasicBlock();
			curBB.addLastInst(new Branch(resultOpr, thenBB, elseBB));

			curBB = thenBB;
			node.getThenStmt().accept(this);
			curBB.addLastInst(new Jump(exitBB));

			curBB = elseBB;
			node.getThenStmt().accept(this);
			curBB.addLastInst(new Jump(exitBB));

			curBB = exitBB;
		}

		scope = scope.getUpperScope();
	}

	public void visit(WhileStmtNode node){
		scope = node.getBodyScope();

		node.getCond().accept(this);
		Operand resultOpr = node.getCond().getResultOpr();
		BasicBlock condBB = new BasicBlock();
		BasicBlock loopBB = new BasicBlock();
		BasicBlock exitBB = new BasicBlock();
		loopContStack.push(condBB);
		loopBreakStack.push(exitBB);

		curBB.addLastInst(new Jump(condBB));
		condBB.addLastInst(new Branch(resultOpr, loopBB, exitBB));
		curBB = loopBB;
		node.getBodyStmt().accept(this);
		curBB.addLastInst(new Jump(condBB));
		curBB = exitBB;

		loopContStack.pop();
		loopBreakStack.pop();

		scope = scope.getUpperScope();
	}

	public void visit(ForStmtNode node){
		scope = node.getBodyScope();

		node.getInit().accept(this);
		node.getCond().accept(this);
		Operand resultOpr = node.getCond().getResultOpr();

		BasicBlock condBB = new BasicBlock();
		BasicBlock loopBB = new BasicBlock();
		BasicBlock exitBB = new BasicBlock();
		BasicBlock stepBB = new BasicBlock();

		loopContStack.push(stepBB);
		loopBreakStack.push(exitBB);

		curBB.addLastInst(new Jump(condBB));
		condBB.addLastInst(new Branch(resultOpr, loopBB, exitBB));

		curBB = stepBB;
		node.getStep().accept(this);
		stepBB.addLastInst(new Jump(condBB));

		curBB = loopBB;
		node.getBodyStmt().accept(this);
		curBB.addLastInst(new Jump(stepBB));
		curBB = exitBB;

		loopContStack.pop();
		loopBreakStack.pop();

		scope = scope.getUpperScope();
	}

	public void visit(BreakStmtNode node){
		curBB.addLastInst(new Jump(loopBreakStack.peek()));
	}

	public void visit(ContStmtNode node){
		curBB.addLastInst(new Jump(loopContStack.peek()));
	}

	public void visit(UnaryExprNode node){
		node.getOpr().accept(this);
		Operand src = node.getOpr().getResultOpr();
		switch (node.getOp()){
			case POS_INC : { // i++
				Operand tmp = new I32Value();
				node.setResultOpr(tmp);
				curBB.addInst(new Move(src, tmp));
				curBB.addInst(new Binary(Binary.Op.ADD, src, new Immediate(1), src));
				break;
			}
			case POS_SUB : { // i--
				Operand tmp = new I32Value();
				node.setResultOpr(tmp);
				curBB.addInst(new Move(src, tmp));
				curBB.addInst(new Binary(Binary.Op.SUB, src, new Immediate(1), src));
				break;
			}
			case PRE_INC : { // ++i
				node.setResultOpr(src);
				curBB.addInst(new Binary(Binary.Op.ADD, src, new Immediate(1), src));
				break;
			}
			case PRE_SUB : { // --i
				node.setResultOpr(src);
				curBB.addInst(new Binary(Binary.Op.SUB, src, new Immediate(1), src));
				break;
			}
			case PLU : { // +
				node.setResultOpr(src);
				break;
			}
			case NEG : { // -
				Operand dst = new I32Value();
				node.setResultOpr(dst);
				curBB.addInst(new Unary(Unary.Op.NEG, src, dst));
				break;
			}
			case COM : { // ~
				Operand dst = new I32Value();
				node.setResultOpr(dst);
				curBB.addInst(new Unary(Unary.Op.COM, src, dst));
				break;
			}
			case NOT : { // !
				Operand dst = new I32Value();
				node.setResultOpr(dst);
				BasicBlock trueBB = new BasicBlock();
				BasicBlock falseBB = new BasicBlock();
				BasicBlock exitBB = new BasicBlock();
				curBB.addLastInst(new Branch(src, trueBB, falseBB));
				trueBB.addInst(new Move(new Immediate(0), dst));
				falseBB.addInst(new Move(new Immediate(1), dst));
				trueBB.addLastInst(new Jump(exitBB));
				falseBB.addLastInst(new Jump(exitBB));
				curBB = exitBB;
				break;
			}
		}

	}

	public void visit(BinaryExprNode node){
		node.getLhs().accept(this);
		node.getRhs().accept(this);
		Operand lhs = node.getLhs().getResultOpr(), rhs = node.getRhs().getResultOpr();
		switch (node.getOp()){
			case SUB :
			case MUL :
			case DIV :
			case MOD :
			case SHL :
			case SHR :
			case AND :
			case OR :
			case XOR : {
				Operand dst = new I32Value();
				curBB.addInst(new Binary(Binary.Op.valueOf(String.valueOf(node.getOp())), lhs, rhs, dst));
				break;
			}

			case ADD : {
				// TODO: String ~ String
				Operand dst = new I32Value();
				curBB.addInst(new Binary(Binary.Op.ADD, lhs, rhs, dst));
				break;
			}

			case LT	:
			case LE :
			case GT :
			case GE : {
				// TODO: String ~ String
				Operand dst = new I32Value();
				curBB.addInst(new Binary(Binary.Op.valueOf(String.valueOf(node.getOp())), lhs, rhs, dst));
				break;
			}

			case EQ :
			case NEQ : {
				// TODO: String ~ String, * ~ Null
				Operand dst = new I32Value();
				curBB.addInst(new Binary(Binary.Op.valueOf(String.valueOf(node.getOp())), lhs, rhs, dst));
				break;
			}

			case LAND : {
				Operand dst = new I32Value();
				node.setResultOpr(dst);
				BasicBlock trueBB = new BasicBlock();
				BasicBlock falseBB = new BasicBlock();
				BasicBlock exitBB = new BasicBlock();
				curBB.addLastInst(new Branch(lhs, trueBB, falseBB));
				trueBB.addInst(new Move(rhs, dst));
				falseBB.addInst(new Move(new Immediate(0), dst));
				trueBB.addLastInst(new Jump(exitBB));
				falseBB.addLastInst(new Jump(exitBB));
				curBB = exitBB;
				break;
			}
			case LOR : {
				Operand dst = new I32Value();
				node.setResultOpr(dst);
				BasicBlock trueBB = new BasicBlock();
				BasicBlock falseBB = new BasicBlock();
				BasicBlock exitBB = new BasicBlock();
				curBB.addLastInst(new Branch(lhs, trueBB, falseBB));
				trueBB.addInst(new Move(new Immediate(1), dst));
				falseBB.addInst(new Move(rhs, dst));
				trueBB.addLastInst(new Jump(exitBB));
				falseBB.addLastInst(new Jump(exitBB));
				curBB = exitBB;
				break;
			}

			case ASS : {
				curBB.addInst(new Move(rhs, lhs));
				break;
			}
		}
	}

	public void visit(MemberExprNode node){
		if(node.getSymbol() instanceof VarSymbol){
			I32Pointer base_ptr = (I32Pointer)node.getExpr().getResultOpr();
			int offset = ((VarSymbol) node.getSymbol()).getOffset();
			I32Pointer dst_ptr = new I32Pointer();
			node.setResultOpr(dst_ptr);
			curBB.addInst(new Binary(Binary.Op.ADD, base_ptr, new Immediate(offset), dst_ptr));
		}
	}

	public void visit(ArrayIdxExprNode node){
		node.getArray().accept(this);
		node.getIdx().accept(this);

		I32Value base_ptr = (I32Value) node.getArray().getResultOpr();
		I32Value index = (I32Value) node.getIdx().getResultOpr();
		I32Value offset = new I32Value();
		I32Pointer ptr = new I32Pointer();

		ArrayType arrayType = (ArrayType) node.getArray().getType();
		int dim = arrayType.getDim();

		curBB.addInst(new Binary(Binary.Op.MUL, index, new Immediate(dim == 1 ? arrayType.getType().getSize() : Config.POINTER_SIZE), offset));
		curBB.addInst(new Binary(Binary.Op.ADD, base_ptr, offset, ptr));
	}

	public void visit(BoolConstExprNode node){
		node.setResultOpr(new Immediate(node.getValue() ? 1 : 0));
	}

	public void visit(NullConstExprNode node){
		node.setResultOpr(new Immediate(0));
	}

	public void visit(StringConstExprNode node){
		StaticStrConst staticStrConst = new StaticStrConst(node.getValue());
	}

	public void visit(IntConstExprNode node){
		node.setResultOpr(new Immediate(node.getValue()));
	}

	public void visit(CompStmtNode node){
		scope = node.getBodyScope();
		for(StmtNode stmtNode : node.getStmtList()){
			stmtNode.accept(this);
		}
		scope = scope.getUpperScope();
	}

	public void visit(ExprStmtNode node){
		node.getExpr().accept(this);
	}

	public void visit(FuncCallExprNode node){
		node.getFunction().accept(this);
		FuncSymbol funcSymbol = (FuncSymbol) node.getFunction().getSymbol();

		Function function = funcSymbol.getFunction();

		List<Operand> paraList = new ArrayList<>();
		for(VarSymbol varSymbol : funcSymbol.getParaSymbolList()){
			paraList.add(varSymbol.getStorage());
		}

		Operand dst;
		if(node.getType() instanceof VoidType) dst = null;
		else dst = new I32Value();

		curBB.addInst(new Call(function,
				function.getIsMemberFunc() ? (I32Pointer)((MemberExprNode)node.getFunction()).getExpr().getResultOpr() : null,
				paraList,
				dst));
	}

	public void visit(IdExprNode node){
		if(node.getSymbol() instanceof VarSymbol)
			node.setResultOpr(((VarSymbol)node.getSymbol()).getStorage());
	}


	public void visit(ReturnStmtNode node){
		if(node.getExpr() == null)
			curBB.addLastInst(new Return(null));
		else{
			node.getExpr().accept(this);
			Operand val = node.getExpr().getResultOpr();
			curBB.addLastInst(new Return(val));
		}
	}

	public void visit(ThisExprNode node){
		node.setResultOpr(scope.getCurrentFuncSymbol().getFunction().getObjPtr());
	}

	public void visit(NewExprNode node){
		if(node.getType() instanceof ArrayType){
			I32Value result = new I32Value();
			for(ExprNode expr : node.getExprList()){
				expr.accept(this);
			}
			newArray(node.getExprList(), node.getDim(), 0, ((ArrayType) node.getType()).getType(), result);
			node.setResultOpr(result);
		}
		else{ // ClassType
			I32Value ptr = new I32Value();
			curBB.addInst(new Alloc(new Immediate(((ClassType)node.getType()).getSize()), ptr));
		}
		// TODO: no String type?
	}

	public void visit(VarDeclStmtNode node){
		node.getVarDeclList().accept(this);
	}

	public void visit(VarDeclListNode node){
		for(VarDeclNode decl : node.getVarDeclNodeList()){
			decl.accept(this);
		}
	}

	public void visit(VarDeclNode node){
		I32Value var = new I32Value(node.getIdentifier());
		((VarSymbol)node.getSymbol()).setStorage(var);
		if(node.getExpr() != null){
			node.getExpr().accept(this);
			curBB.addInst(new Move(node.getExpr().getResultOpr(), var));
		}
	}

	public void newArray(List<ExprNode> exprNodeList, int totDim, int curDim, Type type, Operand result){
		ExprNode expr = exprNodeList.get(curDim);
		Operand siz = expr.getResultOpr();
		Operand mem_siz = new I32Value();
		Operand mem_siz_plus1 = new I32Value();

		if(exprNodeList.size() != curDim - 1)
			curBB.addInst(new Binary(Binary.Op.MUL, siz, new Immediate(Config.POINTER_SIZE), mem_siz));
		else // the last dim
			curBB.addInst(new Binary(Binary.Op.MUL, siz, new Immediate(type.getSize()), mem_siz));

		curBB.addInst(new Binary(Binary.Op.ADD, mem_siz, new Immediate(Config.BASIC_TYPE_SIZE), mem_siz_plus1));
		if(result instanceof I32Value) curBB.addInst(new Alloc(mem_siz_plus1, result));
		else{
			Operand tmp = new I32Value();
			curBB.addInst(new Alloc(mem_siz_plus1, tmp));
			curBB.addInst(new Store(tmp, result));
		}

		if(exprNodeList.size() != curDim - 1) {

			BasicBlock condBB = new BasicBlock();
			BasicBlock loopBB = new BasicBlock();
			BasicBlock exitBB = new BasicBlock();

			Operand curIdx = new I32Value();
			Operand curPtr = new I32Pointer();

			// init
			curBB.addInst(new Move(new Immediate(0), curIdx));
			curBB.addInst(new Binary(Binary.Op.ADD, result, new Immediate(Config.BASIC_TYPE_SIZE), curPtr));
			curBB.addLastInst(new Jump(condBB));

			// cond
			Operand cmpResult = new I32Value();
			condBB.addInst(new Binary(Binary.Op.LT, curIdx, siz, cmpResult));
			condBB.addLastInst(new Branch(cmpResult, loopBB, exitBB));

			// loop body
			curBB = loopBB;
			newArray(exprNodeList, totDim, curDim + 1, type, curPtr);
			curBB.addInst(new Binary(Binary.Op.ADD, curIdx, new Immediate(1), curIdx));
			curBB.addInst(new Binary(Binary.Op.ADD, curPtr, new Immediate(Config.POINTER_SIZE), curPtr));
			curBB.addLastInst(new Jump(condBB));

			curBB = exitBB;
		}

	}

	public void visit(IntTypeNode node){
		// inaccessible method
	}

	public void visit(VoidTypeNode node){
		// inaccessible method
	}

	public void visit(StringTypeNode node){
		// inaccessible method
	}

	public void visit(BoolTypeNode node){
		// inaccessible method
	}

	public void visit(ArrayTypeNode node){
		// inaccessible method
	}

	public void visit(ClassTypeNode node){
		// inaccessible method
	}

}
