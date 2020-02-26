package Compiler.AST;

import Compiler.SemanticAnalysis.ASTVisitor;
import Compiler.SymbolTable.Symbol.FuncSymbol;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Position;

import java.util.List;

public class FuncCallExprNode extends ExprNode {

	private ExprNode function;
	private List<ExprNode> paraList;

	public FuncCallExprNode(Position position, ExprNode function, List<ExprNode> paraList){
		super(position);
		this.function = function;
		this.paraList = paraList;
	}

	public ExprNode getFunction() {
		return function;
	}

	public List<ExprNode> getParaList() {
		return paraList;
	}

	public void accept(ASTVisitor astVisitor){
		astVisitor.visit(this);
	}
}
