package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

import java.util.List;

public class ClassDeclNode extends DeclNode {

	String identifier;
	List<FuncDeclNode> funcDeclList;
	List<VarDeclNode> varDeclList;

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

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
