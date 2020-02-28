package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Symbol.ClassSymbol;
import Compiler.Utils.Position;

import java.util.List;

public class ClassDeclNode extends DeclNode {

	private String identifier;
	private List<FuncDeclNode> funcDeclList;
	private List<VarDeclNode> varDeclList;
	private ClassSymbol classSymbol;

	public ClassDeclNode(Position position, String identifier, List<FuncDeclNode> funcDeclList, List<VarDeclNode> varDeclList){
		super(position);
		this.identifier = identifier;
		this.funcDeclList = funcDeclList;
		this.varDeclList = varDeclList;
	}

	public String getIdentifier(){
		return identifier;
	}

	public List<FuncDeclNode> getFuncDeclList() {
		return funcDeclList;
	}

	public List<VarDeclNode> getVarDeclList() {
		return varDeclList;
	}

	public ClassSymbol getClassSymbol() {
		return classSymbol;
	}

	public void setClassSymbol(ClassSymbol classSymbol) {
		this.classSymbol = classSymbol;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
