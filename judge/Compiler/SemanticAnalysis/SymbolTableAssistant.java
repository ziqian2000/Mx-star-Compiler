package Compiler.SemanticAnalysis;

import Compiler.AST.*;
import Compiler.SymbolTable.Scope;
import Compiler.SymbolTable.Symbol.ClassSymbol;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.SymbolTable.Type.*;
import Compiler.Utils.SemanticException;

public class SymbolTableAssistant {

	public static IntType intType = new IntType();
	public static BoolType boolType = new BoolType();
	public static StringType stringType = new StringType();
	public static VoidType voidType = new VoidType();

	public static Type TypeNode2VarType(Scope scope, TypeNode node){
		if(node instanceof ClassTypeNode){
			String identifier = node.getIdentifier();
			Type type = scope.findType(identifier);
			if(type == null)
				throw new SemanticException(node.getPosition(), "undefined class :" + identifier);
			return type;
		}
		else if(node instanceof ArrayTypeNode){
			int dim = 0;
			while(node instanceof ArrayTypeNode){
				dim++;
				node = ((ArrayTypeNode) node).getType();
			}
			Type type = TypeNode2VarType(scope, node);
			String arrayIdentifier = type.toArrayString(dim);
			ArrayType arrayType = (ArrayType)scope.findType(arrayIdentifier);
			if(arrayIdentifier == null){
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

	public static Type ArrayTypeDimDecrease(Scope scope, Type preArrayType, int dimDecrease, ExprNode node){
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

}
