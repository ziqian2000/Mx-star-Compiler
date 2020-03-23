package Compiler.IRVisitor;

import Compiler.AST.ClassDeclNode;
import Compiler.AST.DeclNode;
import Compiler.AST.ProgramNode;
import Compiler.AST.VarDeclNode;
import Compiler.SemanticAnalysis.ASTBaseVisitor;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.Utils.Config;

public class MemberOffsetCalculator extends ASTBaseVisitor{

	public void visit(ProgramNode node){
		for(DeclNode decl : node.getDeclList())
			if(decl instanceof ClassDeclNode)
				decl.accept(this);
	}

	public void visit(ClassDeclNode node){
		int curOffset = 0;
		for(VarDeclNode varDeclNode : node.getVarDeclList()){
			VarSymbol varSymbol = (VarSymbol) varDeclNode.getSymbol();
			varSymbol.setOffset(curOffset);
			curOffset += IRAssistant.isReferenceType(varSymbol.getVarType())
					? Config.POINTER_SIZE
					: Config.BASIC_TYPE_SIZE;
		}
		node.getClassSymbol().getSelfType().setSize(curOffset);
	}

}
