package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.*;
import Compiler.Utils.SemanticException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SymbolTableAssistant {

	public static IntType intType = new IntType();
	public static BoolType boolType = new BoolType();
	public static StringType stringType = new StringType();
	public static VoidType voidType = new VoidType();
	public static NullType nullType = new NullType();

	public static Scope stringBuiltinFuncScope = new Scope(null);
	public static Scope arrayBuiltinFuncScope = new Scope(null);

	public static Type typeNode2VarType(Scope scope, TypeNode node){
		if(node instanceof ClassTypeNode){
			String identifier = node.getIdentifier();
			Type type = scope.findType(identifier);
			if(type == null)
				throw new SemanticException(node.getPosition(), "undefined class : " + identifier);
			return type;
		}
		else if(node instanceof ArrayTypeNode){
			int dim = 0;
			while(node instanceof ArrayTypeNode){
				dim++;
				node = ((ArrayTypeNode) node).getType();
			}
			Type type = typeNode2VarType(scope, node);
			String arrayIdentifier = type.toArrayString(dim);
			ArrayType arrayType = (ArrayType)scope.findType(arrayIdentifier);
			if(arrayType == null){
				arrayType = new ArrayType(type, dim);
				scope.addType(arrayIdentifier, arrayType);
			}
			return arrayType;
		}
		else{
			if(node instanceof IntTypeNode) return intType;
			else if(node instanceof BoolTypeNode) return boolType;
			else if(node instanceof StringTypeNode) return stringType;
			else if(node instanceof VoidTypeNode) return voidType;
			else throw new SemanticException(node.getPosition(), "unknown type : " + node.getIdentifier());
		}
	}

	public static Type arrayTypeDimDecrease(Scope scope, Type preArrayType, int dimDecrease, ExprNode node){
		if(dimDecrease == 0) return preArrayType;
		if(!(preArrayType instanceof ArrayType))
			throw new SemanticException(node.getPosition(), "not array type : " + preArrayType.getIdentifier());
		ArrayType arrayType = (ArrayType) preArrayType;

		if(arrayType.getDim() < dimDecrease){
			throw new SemanticException(node.getPosition(), "dimension exceeded : " + arrayType.getIdentifier());
		}
		else if(arrayType.getDim() == dimDecrease){
			return arrayType.getType();
		}
		else {
			ArrayType tmp = new ArrayType(arrayType.getType(), arrayType.getDim() - dimDecrease);
			ArrayType retArrayType = (ArrayType)scope.findType(tmp.getIdentifier());
			if(retArrayType == null){
				scope.addType(tmp.getIdentifier(), tmp);
				return tmp;
			}
			else return retArrayType;
		}
	}

	public static Type arrayTypeDimIncrease(Scope scope, Type preType, int dimIncrease, ExprNode node){
		if(dimIncrease == 0) return preType;
		if(preType instanceof ArrayType){
			ArrayType tmp = new ArrayType(((ArrayType)preType).getType(), ((ArrayType)preType).getDim() + dimIncrease);
			ArrayType retArrayType = (ArrayType)scope.findType(tmp.getIdentifier());
			if(retArrayType == null){
				scope.addType(tmp.getIdentifier(), tmp);
				return tmp;
			}
			else return retArrayType;
		}
		else{
			ArrayType tmp = new ArrayType(preType, dimIncrease);
			ArrayType retArrayType = (ArrayType)scope.findType(tmp.getIdentifier());
			if(retArrayType == null){
				scope.addType(tmp.getIdentifier(), tmp);
				return tmp;
			}
			else return retArrayType;
		}
	}

	public static void addBuiltinFunction(Scope scope){
		scope.mutuallyAddSymbol("print", new FuncSymbol("print", voidType, new ArrayList<>(Collections.singletonList(new VarSymbol("str", stringType)))));
		scope.mutuallyAddSymbol("println", new FuncSymbol("println", voidType, new ArrayList<>(Collections.singletonList(new VarSymbol("str", stringType)))));
		scope.mutuallyAddSymbol("printInt", new FuncSymbol("printInt", voidType, new ArrayList<>(Collections.singletonList(new VarSymbol("n", intType)))));
		scope.mutuallyAddSymbol("printlnInt", new FuncSymbol("printlnInt", voidType, new ArrayList<>(Collections.singletonList(new VarSymbol("n", intType)))));
		scope.mutuallyAddSymbol("getString", new FuncSymbol("getString", stringType, new ArrayList<>()));
		scope.mutuallyAddSymbol("getInt", new FuncSymbol("getInt", intType, new ArrayList<>()));
		scope.mutuallyAddSymbol("toString", new FuncSymbol("toString", stringType, new ArrayList<>(Collections.singletonList(new VarSymbol("i", intType)))));

		// string
		stringBuiltinFuncScope.mutuallyAddSymbol("length", new FuncSymbol("length", intType, new ArrayList<>()));
		stringBuiltinFuncScope.mutuallyAddSymbol("substring", new FuncSymbol("substring", stringType, new ArrayList<>(Arrays.asList(new VarSymbol("left", intType), new VarSymbol("right", intType)))));
		stringBuiltinFuncScope.mutuallyAddSymbol("parseInt", new FuncSymbol("parseInt", intType, new ArrayList<>()));
		stringBuiltinFuncScope.mutuallyAddSymbol("ord", new FuncSymbol("ord", intType, new ArrayList<>(Collections.singletonList(new VarSymbol("pos", intType)))));

		// array
		arrayBuiltinFuncScope.mutuallyAddSymbol("size", new FuncSymbol("size", intType, new ArrayList<>()));
	}

	public static Symbol findStringBuiltinFuncSymbol(String identifier){
		return stringBuiltinFuncScope.findSymbol(identifier);
	}

	public static Symbol findArrayBuiltinFuncSymbol(String identifier){
		return arrayBuiltinFuncScope.findSymbol(identifier);
	}

}
