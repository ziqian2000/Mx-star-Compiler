package Compiler.IRVisitor;

import Compiler.SymbolTable.Type.ArrayType;
import Compiler.SymbolTable.Type.ClassType;
import Compiler.SymbolTable.Type.StringType;
import Compiler.SymbolTable.Type.Type;

public class IRAssistant {

	public static boolean isReferenceType(Type type){
		return type instanceof ArrayType || type instanceof ClassType || type instanceof StringType;
	}
}
