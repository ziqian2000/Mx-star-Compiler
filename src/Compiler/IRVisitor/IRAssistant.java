package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.IRIns;
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

	public static Function newBuiltinFunction(IR ir, Scope scope, String identifier, String prefix){
		Function function = new Function(prefix + identifier);
		function.setIsMemberFunc(!prefix.isEmpty());
		FuncSymbol funcSymbol = (FuncSymbol)scope.findSymbol(identifier);
		// funcSymbol == null means this function won't be called by program but only compiler
		if(funcSymbol != null) funcSymbol.setFunction(function);
		ir.addFunction(function);
		return function;
	}

	public static void addBuiltinFunction(IR ir, Scope scope, Scope stringBuiltinFuncScope, Scope arrayBuiltinFuncScope){

		builtinPrint = newBuiltinFunction(ir, scope,"print", "");
		builtinPrintln = newBuiltinFunction(ir, scope,"println", "");
		builtinPrintInt = newBuiltinFunction(ir, scope,"printInt", "");
		builtinPrintlnInt = newBuiltinFunction(ir, scope,"printlnInt", "");
		builtinGetString = newBuiltinFunction(ir, scope,"getString", "");
		builtinGetInt = newBuiltinFunction(ir, scope,"getInt", "");
		builtinToString = newBuiltinFunction(ir, scope,"toString", "");

		// string
		builtinStrLength = newBuiltinFunction(ir, stringBuiltinFuncScope,"length", "string.");
		builtinStrSubstring = newBuiltinFunction(ir, stringBuiltinFuncScope,"substring", "string.");
		builtinStrParseInt = newBuiltinFunction(ir, stringBuiltinFuncScope,"parseInt", "string.");
		builtinStrOrd = newBuiltinFunction(ir, stringBuiltinFuncScope,"ord", "string.");
		builtinStrAdd = newBuiltinFunction(ir, stringBuiltinFuncScope, "add", "string.");
		builtinStrEq = newBuiltinFunction(ir, stringBuiltinFuncScope, "eq", "string.");
		builtinStrNeq = newBuiltinFunction(ir, stringBuiltinFuncScope, "neq", "string.");
		builtinStrLt = newBuiltinFunction(ir, stringBuiltinFuncScope, "lt", "string.");
		builtinStrLe = newBuiltinFunction(ir, stringBuiltinFuncScope, "le", "string.");
		builtinStrGt = newBuiltinFunction(ir, stringBuiltinFuncScope, "gt", "string.");
		builtinStrGe = newBuiltinFunction(ir, stringBuiltinFuncScope, "ge", "string.");

		// array
		builtinArraySize = newBuiltinFunction(ir, arrayBuiltinFuncScope,"size", "array.");
		I32Value obj = new I32Value(), ret = new I32Value();
		builtinArraySize.setObj(obj);
		BasicBlock BB = new BasicBlock();
		BB.addInst(new Load(ret, obj));
		BB.addLastInst(new Return(ret));
		builtinArraySize.setEntryBB(BB);
		builtinArraySize.setIsMemberFunc(true);

	}

}
