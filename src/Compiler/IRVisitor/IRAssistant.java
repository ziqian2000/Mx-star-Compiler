package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.Load;
import Compiler.IR.Instr.Return;
import Compiler.IR.Operand.I32Value;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Type.ArrayType;
import Compiler.SymbolTable.Type.ClassType;
import Compiler.SymbolTable.Type.StringType;
import Compiler.SymbolTable.Type.Type;

public class IRAssistant {

	public static Function
	builtinPrint,
	builtinPrintln,
	builtinPrintInt,
	builtinPrintlnInt,
	builtinGetString,
	builtinGetInt,
	builtinToString,

	builtinStrLength,
	builtinStrSubstring,
	builtinStrParseInt,
	builtinStrOrd,
	builtinStrAdd,
	builtinStrEq,
	builtinStrNeq,
	builtinStrLt,
	builtinStrLe,
	builtinStrGt,
	builtinStrGe,

	builtinArraySize;

	public static boolean isReferenceType(Type type){
		return type instanceof ArrayType || type instanceof ClassType || type instanceof StringType;
	}

	public static Function newBuiltinFunction(IR ir, Scope scope, String identifier, String prefix, boolean isBuiltin){
		Function function = new Function(prefix + identifier);
		function.setIsMemberFunc(!prefix.isEmpty());
		FuncSymbol funcSymbol = (FuncSymbol)scope.findSymbol(identifier);
		// funcSymbol == null means this function won't be called by program but only compiler
		if(funcSymbol != null) funcSymbol.setFunction(function);
		ir.addFunction(function);
		function.setIsBuiltin(isBuiltin);
		return function;
	}

	public static void addBuiltinFunction(IR ir, Scope scope, Scope stringBuiltinFuncScope, Scope arrayBuiltinFuncScope){

		builtinPrint = newBuiltinFunction(ir, scope,"print", "", true);
		builtinPrintln = newBuiltinFunction(ir, scope,"println", "", true);
		builtinPrintInt = newBuiltinFunction(ir, scope,"printInt", "", true);
		builtinPrintlnInt = newBuiltinFunction(ir, scope,"printlnInt", "", true);
		builtinGetString = newBuiltinFunction(ir, scope,"getString", "", true);
		builtinGetInt = newBuiltinFunction(ir, scope,"getInt", "", true);
		builtinToString = newBuiltinFunction(ir, scope,"toString", "", true);

		// string
		builtinStrLength = newBuiltinFunction(ir, stringBuiltinFuncScope,"length", "string.", true);
		builtinStrSubstring = newBuiltinFunction(ir, stringBuiltinFuncScope,"substring", "string.", true);
		builtinStrParseInt = newBuiltinFunction(ir, stringBuiltinFuncScope,"parseInt", "string.", true);
		builtinStrOrd = newBuiltinFunction(ir, stringBuiltinFuncScope,"ord", "string.", true);
		builtinStrAdd = newBuiltinFunction(ir, stringBuiltinFuncScope, "add", "string.", true);
		builtinStrEq = newBuiltinFunction(ir, stringBuiltinFuncScope, "eq", "string.", true);
		builtinStrNeq = newBuiltinFunction(ir, stringBuiltinFuncScope, "neq", "string.", true);
		builtinStrLt = newBuiltinFunction(ir, stringBuiltinFuncScope, "lt", "string.", true);
		builtinStrLe = newBuiltinFunction(ir, stringBuiltinFuncScope, "le", "string.", true);
		builtinStrGt = newBuiltinFunction(ir, stringBuiltinFuncScope, "gt", "string.", true);
		builtinStrGe = newBuiltinFunction(ir, stringBuiltinFuncScope, "ge", "string.", true);

		// array
		builtinArraySize = newBuiltinFunction(ir, arrayBuiltinFuncScope,"size", "array.", false);
		I32Value obj = new I32Value(), ret = new I32Value();
		builtinArraySize.setObj(obj);
		BasicBlock BB = new BasicBlock();
		BB.appendInst(new Load(ret, obj));
		BB.appendLastInst(new Return(ret));
		builtinArraySize.setEntryBB(BB);
		builtinArraySize.setExitBB(BB);
		builtinArraySize.setIsMemberFunc(true);

	}

}
