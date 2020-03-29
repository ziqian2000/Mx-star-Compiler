package Compiler.IRVisitor;

import Compiler.AST.*;
import Compiler.IR.*;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.*;
import Compiler.SemanticAnalysis.ASTBaseVisitor;
import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SemanticAnalysis.SymbolTableAssistant;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.*;
import Compiler.Utils.Config;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class IRGenerator extends ASTBaseVisitor implements ASTVisitor {

	private Scope scope;
	private IR ir;
	private BasicBlock curBB;
	private Function curFunction;
	private List<Return> curFuncRetIns = new ArrayList<>();
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

		// __init func begins here
		Function initFunc = new Function("__init");
		ir.addFunction(initFunc);
		BasicBlock initBB = new BasicBlock();
		curBB = initBB;
		initFunc.setEntryBB(initBB);

		// global variable
		for(DeclNode decl : node.getDeclList()) {
			if (decl instanceof VarDeclNode) {

				VarDeclNode varDeclNode = (VarDeclNode)decl;

				I32Pointer ptr = new I32Pointer(varDeclNode.getIdentifier());
				ptr.setGlobal(true);
				curBB.addInst(new Alloc(new Immediate(IRAssistant.isReferenceType(((VarDeclNode) decl).getType().getTypeInfo()) ? Config.POINTER_SIZE : Config.BASIC_TYPE_SIZE), ptr));
				((VarSymbol)varDeclNode.getSymbol()).setStorage(ptr);
				if(varDeclNode.getExpr() != null){
					varDeclNode.getExpr().accept(this);
					curBB.addInst(new Store(convertPtr2Val(varDeclNode.getExpr().getResultOpr()), ptr));
				}

				ir.addGlobalVar(ptr);
			}
		}

		// __init func ends here
		curBB.addInst(new Call(((FuncSymbol)scope.findSymbol("main")).getFunction(), null, new ArrayList<>(), null));
		curBB.addLastInst(new Return(null));

		// function & class
		for(DeclNode decl : node.getDeclList()) {
			if(!(decl instanceof VarDeclNode))
				decl.accept(this);
		}
	}

	public void visit(FuncDeclNode node){
		FuncSymbol funcSymbol = node.getFuncSymbol();
		scope = funcSymbol.getBodyScope();
		Function function = funcSymbol.getFunction();
		ir.addFunction(function);
		curFunction = function;
		curFuncRetIns.clear();

		// add parameters
		if(function.getIsMemberFunc())
			function.setObj(new I32Value("this"));
		for(VarSymbol varSymbol : funcSymbol.getParaSymbolList()){
			Type varType = varSymbol.getVarType();
			I32Value i32Value = new I32Value(varSymbol.getIdentifier());
			varSymbol.setStorage(i32Value);
			function.addParameter(i32Value);
		}

		// traverse
		BasicBlock entryBB = new BasicBlock("entry");
		function.setEntryBB(entryBB);
		curBB = entryBB;
		node.getBodyStmt().accept(this);

		// no return
		boolean hasRetValue = funcSymbol.getRetType() != SymbolTableAssistant.nullType
				&& funcSymbol.getRetType() != SymbolTableAssistant.voidType;
		if(!(curBB.getTailIns() instanceof Return)){
			if(hasRetValue) curBB.addLastInst(new Return(new Immediate(0)));
			else curBB.addLastInst(new Return(null));
			curFuncRetIns.add((Return) curBB.getTailIns());
		}

		// all basic block has been terminated, so use sudoAddInst
		// merge all returns into exit block
		if(curFuncRetIns.size() == 0) throw new FuckingException("no return found when merging returns");
		else if(curFuncRetIns.size() == 1) function.setExitBB(curFuncRetIns.get(0).getBelongBB());
		else{
			BasicBlock exitBlock = new BasicBlock("func_exit");
			function.setExitBB(exitBlock);
			I32Value tmpRetValue = hasRetValue ? new I32Value() : null;
			for(Return ret : curFuncRetIns){
				ret.removeFromList();
				if(hasRetValue) ret.getBelongBB().sudoAddInst(new Move(ret.getRetValue(), tmpRetValue));
				ret.getBelongBB().sudoAddInst(new Jump(exitBlock));
			}
			exitBlock.addLastInst(new Return(tmpRetValue));
		}

		scope = scope.getUpperScope();
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
		Operand resultOpr = convertPtr2Val(node.getCond().getResultOpr());
		if(node.getElseStmt() == null) {
			BasicBlock thenBB = new BasicBlock("if_then");
			BasicBlock exitBB = new BasicBlock("if_exit");
			curBB.addLastInst(new Branch(resultOpr, thenBB, exitBB));

			// then
			curBB = thenBB;
			node.getThenStmt().accept(this);
			curBB.addLastInst(new Jump(exitBB));

			// exit
			curBB = exitBB;
		}
		else{
			BasicBlock thenBB = new BasicBlock("if_then");
			BasicBlock elseBB = new BasicBlock("if_else");
			BasicBlock exitBB = new BasicBlock("if_exit");
			curBB.addLastInst(new Branch(resultOpr, thenBB, elseBB));

			// then
			curBB = thenBB;
			node.getThenStmt().accept(this);
			curBB.addLastInst(new Jump(exitBB));

			// else
			curBB = elseBB;
			node.getElseStmt().accept(this);
			curBB.addLastInst(new Jump(exitBB));

			// exit
			curBB = exitBB;
		}

		scope = scope.getUpperScope();
	}

	public void visit(WhileStmtNode node){
		scope = node.getBodyScope();

		BasicBlock condBB = new BasicBlock();
		BasicBlock loopBB = new BasicBlock();
		BasicBlock exitBB = new BasicBlock();
		loopContStack.push(condBB);
		loopBreakStack.push(exitBB);

		curBB.addLastInst(new Jump(condBB));

		// cond
		curBB = condBB;
		node.getCond().accept(this);
		Operand resultOpr = convertPtr2Val(node.getCond().getResultOpr());
		curBB.addLastInst(new Branch(resultOpr, loopBB, exitBB));

		// loop body
		curBB = loopBB;
		node.getBodyStmt().accept(this);
		curBB.addLastInst(new Jump(condBB));

		// exit
		curBB = exitBB;

		loopContStack.pop();
		loopBreakStack.pop();

		scope = scope.getUpperScope();
	}

	public void visit(ForStmtNode node){
		scope = node.getBodyScope();

		if(node.getInit() != null) node.getInit().accept(this);

		BasicBlock condBB = new BasicBlock();
		BasicBlock loopBB = new BasicBlock();
		BasicBlock exitBB = new BasicBlock();
		BasicBlock stepBB = new BasicBlock();

		loopContStack.push(stepBB);
		loopBreakStack.push(exitBB);

		curBB.addLastInst(new Jump(condBB));

		// cond
		curBB = condBB;
		if(node.getCond() != null) node.getCond().accept(this);
		Operand resultOpr = node.getCond() != null ? convertPtr2Val(node.getCond().getResultOpr()) : new Immediate(1); // always be true
		curBB.addLastInst(new Branch(resultOpr, loopBB, exitBB));

		// step
		curBB = stepBB;
		if(node.getStep() != null) node.getStep().accept(this);
		curBB.addLastInst(new Jump(condBB));

		// loop body
		curBB = loopBB;
		node.getBodyStmt().accept(this);
		curBB.addLastInst(new Jump(stepBB));

		// exit
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
				if(src instanceof I32Pointer){
					curBB.addInst(new Load(tmp, src));
					I32Value tmp2 = new I32Value();
					curBB.addInst(new Binary(Binary.Op.ADD, tmp, new Immediate(1), tmp2));
					curBB.addInst(new Store(tmp2, src));
				}
				else{
					curBB.addInst(new Move(src, tmp));
					curBB.addInst(new Binary(Binary.Op.ADD, src, new Immediate(1), src));
				}
				break;
			}
			case POS_SUB : { // i--
				Operand tmp = new I32Value();
				node.setResultOpr(tmp);
				if(src instanceof I32Pointer){
					curBB.addInst(new Load(tmp, src));
					I32Value tmp2 = new I32Value();
					curBB.addInst(new Binary(Binary.Op.SUB, tmp, new Immediate(1), tmp2));
					curBB.addInst(new Store(tmp2, src));
				}
				else{
					curBB.addInst(new Move(src, tmp));
					curBB.addInst(new Binary(Binary.Op.SUB, src, new Immediate(1), src));
				}
				break;
			}
			case PRE_INC : { // ++i
				node.setResultOpr(src);
				if(src instanceof I32Pointer){
					I32Value tmp = new I32Value();
					curBB.addInst(new Load(tmp, src));
					curBB.addInst(new Binary(Binary.Op.ADD, tmp, new Immediate(1), tmp));
					curBB.addInst(new Store(tmp, src));
				}
				else{
					curBB.addInst(new Binary(Binary.Op.ADD, src, new Immediate(1), src));
				}
				break;
			}
			case PRE_SUB : { // --i
				node.setResultOpr(src);
				if(src instanceof I32Pointer){
					I32Value tmp = new I32Value();
					curBB.addInst(new Load(tmp, src));
					curBB.addInst(new Binary(Binary.Op.SUB, tmp, new Immediate(1), tmp));
					curBB.addInst(new Store(tmp, src));
				}
				else{
					curBB.addInst(new Binary(Binary.Op.SUB, src, new Immediate(1), src));
				}
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
				node.getLhs().accept(this);
				node.getRhs().accept(this);
				Operand lhs = node.getLhs().getResultOpr(), rhs = node.getRhs().getResultOpr();
				Operand lhsVar = convertPtr2Val(lhs), rhsVar = convertPtr2Val(rhs);

				Operand dst = new I32Value();
				node.setResultOpr(dst);
				curBB.addInst(new Binary(Binary.Op.valueOf(String.valueOf(node.getOp())), lhsVar, rhsVar, dst));
				break;
			}

			case ADD : {
				node.getLhs().accept(this);
				node.getRhs().accept(this);
				Operand lhs = node.getLhs().getResultOpr(), rhs = node.getRhs().getResultOpr();
				Operand lhsVar = convertPtr2Val(lhs), rhsVar = convertPtr2Val(rhs);

				Operand dst = new I32Value();
				node.setResultOpr(dst);
				if(node.getLhs().getType() == SymbolTableAssistant.stringType){
					// String ~ String
					curBB.addInst(new Call(IRAssistant.builtinStrAdd, null, new ArrayList<>(Arrays.asList(lhsVar, rhsVar)), dst));

				} else {
					// int ~ int
					curBB.addInst(new Binary(Binary.Op.ADD, lhsVar, rhsVar, dst));
				}
				break;
			}

			case LT	:
			case LE :
			case GT :
			case GE : {
				node.getLhs().accept(this);
				node.getRhs().accept(this);
				Operand lhs = node.getLhs().getResultOpr(), rhs = node.getRhs().getResultOpr();
				Operand lhsVar = convertPtr2Val(lhs), rhsVar = convertPtr2Val(rhs);

				Operand dst = new I32Value();
				node.setResultOpr(dst);
				if(node.getLhs().getType() == SymbolTableAssistant.stringType){
					// String ~ String
					Function callFunc = null;
					switch (node.getOp()){
						case LT	: callFunc = IRAssistant.builtinStrLt; break;
						case LE : callFunc = IRAssistant.builtinStrLe; break;
						case GT : callFunc = IRAssistant.builtinStrGt; break;
						case GE : callFunc = IRAssistant.builtinStrGe; break;
					}
					curBB.addInst(new Call(callFunc, null, new ArrayList<>(Arrays.asList(lhsVar, rhsVar)), dst));
				} else {
					// int ~ int
					curBB.addInst(new Binary(Binary.Op.valueOf(String.valueOf(node.getOp())), lhsVar, rhsVar, dst));
				}
				break;
			}

			case EQ :
			case NEQ : {
				node.getLhs().accept(this);
				node.getRhs().accept(this);
				Operand lhs = node.getLhs().getResultOpr(), rhs = node.getRhs().getResultOpr();
				Operand lhsVar = convertPtr2Val(lhs), rhsVar = convertPtr2Val(rhs);

				Operand dst = new I32Value();
				node.setResultOpr(dst);
				if(node.getLhs().getType() == SymbolTableAssistant.stringType){
					// String ~ String
					Function callFunc = null;
					switch (node.getOp()){
						case EQ	: callFunc = IRAssistant.builtinStrEq; break;
						case NEQ : callFunc = IRAssistant.builtinStrNeq; break;
					}
					curBB.addInst(new Call(callFunc, null, new ArrayList<>(Arrays.asList(lhsVar, rhsVar)), dst));
				} else {
					// int ~ int 		* ~ null
					curBB.addInst(new Binary(Binary.Op.valueOf(String.valueOf(node.getOp())), lhsVar, rhsVar, dst));
				}
				break;
			}

			case LAND : {
				Operand dst = new I32Value();
				node.setResultOpr(dst);
				BasicBlock trueBB = new BasicBlock();
				BasicBlock falseBB = new BasicBlock();
				BasicBlock exitBB = new BasicBlock();

				// init, lhs
				node.getLhs().accept(this);
				Operand lhsVar = convertPtr2Val(node.getLhs().getResultOpr());
				curBB.addLastInst(new Branch(lhsVar, trueBB, falseBB));

				// falseBB
				falseBB.addInst(new Move(new Immediate(0), dst));
				falseBB.addLastInst(new Jump(exitBB));

				// trueBB
				curBB = trueBB;
				node.getRhs().accept(this);
				Operand rhsVar = convertPtr2Val(node.getRhs().getResultOpr());
				curBB.addInst(new Move(rhsVar, dst));
				curBB.addLastInst(new Jump(exitBB));

				// exit
				curBB = exitBB;
				break;
			}
			case LOR : {
				Operand dst = new I32Value();
				node.setResultOpr(dst);
				BasicBlock trueBB = new BasicBlock();
				BasicBlock falseBB = new BasicBlock();
				BasicBlock exitBB = new BasicBlock();

				// init, lhs
				node.getLhs().accept(this);
				Operand lhsVar = convertPtr2Val(node.getLhs().getResultOpr());
				curBB.addLastInst(new Branch(lhsVar, trueBB, falseBB));

				// trueBB
				trueBB.addInst(new Move(new Immediate(1), dst));
				trueBB.addLastInst(new Jump(exitBB));

				// falseBB
				curBB = falseBB;
				node.getRhs().accept(this);
				Operand rhsVar = convertPtr2Val(node.getRhs().getResultOpr());
				curBB.addInst(new Move(rhsVar, dst));
				curBB.addLastInst(new Jump(exitBB));

				// exit
				curBB = exitBB;
				break;
			}

			case ASS : {
				node.getLhs().accept(this);
				node.getRhs().accept(this);
				Operand lhs = node.getLhs().getResultOpr(), rhs = node.getRhs().getResultOpr();
				Operand lhsVar = convertPtr2Val(lhs), rhsVar = convertPtr2Val(rhs);

				assign(rhs, (Register)lhs);
				break;
			}
			default:
				throw new IllegalStateException("Unexpected value: " + node.getOp());
		}
	}

	public void visit(MemberExprNode node){
		node.getExpr().accept(this);
		if(node.getSymbol() instanceof VarSymbol){
			I32Value base_addr = (I32Value) convertPtr2Val( node.getExpr().getResultOpr());
			int offset = ((VarSymbol) node.getSymbol()).getOffset();
			I32Pointer dst_ptr = new I32Pointer();
			node.setResultOpr(dst_ptr);
			curBB.addInst(new Binary(Binary.Op.ADD, base_addr, new Immediate(offset), dst_ptr));
		} else {
			node.setResultOpr(node.getExpr().getResultOpr());
		}
	}

	public void visit(ArrayIdxExprNode node){
		node.getArray().accept(this);
		node.getIdx().accept(this);

		I32Value base_addr = (I32Value) convertPtr2Val(node.getArray().getResultOpr());

		Operand index = convertPtr2Val(node.getIdx().getResultOpr());
		Operand index_plus1 = new I32Value();

		I32Value offset = new I32Value();
		I32Pointer target_addr = new I32Pointer();

		ArrayType arrayType = (ArrayType) node.getArray().getType();
		int dim = arrayType.getDim();

		curBB.addInst(new Binary(Binary.Op.ADD, index, new Immediate(1), index_plus1));
		curBB.addInst(new Binary(Binary.Op.MUL, index_plus1, new Immediate(dim == 1 ? arrayType.getType().getSize() : Config.POINTER_SIZE), offset));
		curBB.addInst(new Binary(Binary.Op.ADD, base_addr, offset, target_addr));

		node.setResultOpr(target_addr);
	}

	public void visit(BoolConstExprNode node){
		node.setResultOpr(new Immediate(node.getValue() ? 1 : 0));
	}

	public void visit(NullConstExprNode node){
		node.setResultOpr(new Immediate(0));
	}

	public void visit(StringConstExprNode node){
		StaticStrConst staticStrConst = new StaticStrConst(node.getValue());
		ir.addStaticStrConst(staticStrConst);
		node.setResultOpr(staticStrConst);
	}

	public void visit(IntConstExprNode node){
		node.setResultOpr(new Immediate(node.getValue()));
	}

	public void visit(CompStmtNode node){
		scope = node.getBodyScope();
		if(node.getStmtList() != null)
			for(StmtNode stmtNode : node.getStmtList()){
				stmtNode.accept(this);
			}
		if(!node.getIsFunctionBody()) scope = scope.getUpperScope();
	}

	public void visit(ExprStmtNode node){
		node.getExpr().accept(this);
	}

	public void visit(FuncCallExprNode node){
		node.getFunction().accept(this);
		FuncSymbol funcSymbol = (FuncSymbol) node.getFunction().getSymbol();

		Function function = funcSymbol.getFunction();

		// add parameter
		List<Operand> paraList = new ArrayList<>();
		for(ExprNode paraExpr : node.getParaList()){
			paraExpr.accept(this);
			paraList.add(convertPtr2Val(paraExpr.getResultOpr()));
		}

		// save return value
		Operand dst;
		if(node.getType() instanceof VoidType) dst = null;
		else dst = new I32Value();
		node.setResultOpr(dst);

		// member function, set obj
		Operand obj = null;
		if(function.getIsMemberFunc()){
			// call outside class
			if(node.getFunction() instanceof MemberExprNode) obj = convertPtr2Val(((MemberExprNode)node.getFunction()).getExpr().getResultOpr());
			// call inside class
			else obj = curFunction.getObj();
		}

		curBB.addInst(new Call(function,
				obj,
				paraList,
				dst));
	}

	public void visit(IdExprNode node){
		if(node.getSymbol() instanceof VarSymbol){
			VarSymbol varSymbol = (VarSymbol)node.getSymbol();
			if(varSymbol.getScope().isClassScope()){ // member variable
				I32Pointer ptr = new I32Pointer();
				curBB.addInst(new Binary(Binary.Op.ADD, curFunction.getObj(), new Immediate(varSymbol.getOffset()), ptr));
				node.setResultOpr(ptr);
			} else { // not member variable
				node.setResultOpr(((VarSymbol)node.getSymbol()).getStorage());
			}
		}
	}


	public void visit(ReturnStmtNode node){
		if(node.getExpr() == null)
			curBB.addLastInst(new Return(null));
		else{
			node.getExpr().accept(this);
			Operand val = convertPtr2Val(node.getExpr().getResultOpr());
			curBB.addLastInst(new Return(val));
		}
		curFuncRetIns.add((Return)curBB.getTailIns());
	}

	public void visit(ThisExprNode node){
		node.setResultOpr(scope.getCurrentFuncSymbol().getFunction().getObj());
	}

	public void visit(NewExprNode node){
		I32Value result = new I32Value();
		node.setResultOpr(result);
		if(node.getType() instanceof ArrayType){ // ArrayType
			for(ExprNode expr : node.getExprList()){
				expr.accept(this);
			}
			newArray(node.getExprList(), node.getDim(), 0, ((ArrayType) node.getType()).getType(), result);
		}
		else{ // ClassType
			curBB.addInst(new Alloc(new Immediate((node.getType()).getSize()), result));

			Function constructor = ((ClassType)node.getType()).getSelfSymbol().getConstructor();
			if(constructor != null)
				curBB.addInst(new Call(constructor, result, new ArrayList<>(), null));
		}
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
			curBB.addInst(new Move(convertPtr2Val(node.getExpr().getResultOpr()), var));
		}
	}

	// more method

	public Operand convertPtr2Val(Operand src){ // only convert pointer to value
		if(src instanceof I32Pointer) {
			I32Value value = new I32Value();
			curBB.addInst(new Load(value, src));
			return value;
		}
		else return src;
	}

	public void assign(Operand src, Register dst){
		Operand src_tmp = convertPtr2Val(src);

		if(dst instanceof I32Value) curBB.addInst(new Move(src_tmp, dst));
		else curBB.addInst(new Store(src_tmp, dst));
	}

	public void newArray(List<ExprNode> exprNodeList, int totDim, int curDim, Type type, Operand result){
		ExprNode expr = exprNodeList.get(curDim);
		Operand siz = convertPtr2Val(expr.getResultOpr());
		Operand mem_siz = new I32Value();
		Operand mem_siz_plus1 = new I32Value();

		// calculate current dim size
		if(totDim - 1 != curDim)
			curBB.addInst(new Binary(Binary.Op.MUL, siz, new Immediate(Config.POINTER_SIZE), mem_siz));
		else // the last dim
			curBB.addInst(new Binary(Binary.Op.MUL, siz, new Immediate(type.getSize()), mem_siz));

		// 4 byte for size
		curBB.addInst(new Binary(Binary.Op.ADD, mem_siz, new Immediate(Config.BASIC_TYPE_SIZE), mem_siz_plus1));

		// alloc
		if(result instanceof I32Value) {
			curBB.addInst(new Alloc(mem_siz_plus1, result));
			curBB.addInst(new Store(siz, result));
		}
		else{
			Operand tmp = new I32Value();
			curBB.addInst(new Alloc(mem_siz_plus1, tmp));
			curBB.addInst(new Store(siz, tmp));
			curBB.addInst(new Store(tmp, result));
		}

		// recursion
		if(exprNodeList.size() - 1 != curDim) {

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

}
