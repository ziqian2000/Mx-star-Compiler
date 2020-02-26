package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class VarDeclListNode extends DeclNode {

	TypeNode type;
	List<VarDeclNode> varDeclNodeList;

	public VarDeclListNode(Position position){
		super(position);
		varDeclNodeList = new ArrayList<>();
	}

	public void addVarDecl(VarDeclNode varDeclNode){
		varDeclNodeList.add(varDeclNode);
	}

	public void setType(TypeNode type){
		this.type = type;
		for(VarDeclNode varDeclNode : varDeclNodeList){
			varDeclNode.setType(type);
		}
	}

	public TypeNode getType() {
		return type;
	}

	public List<VarDeclNode> getVarDeclNodeList() {
		return varDeclNodeList;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
