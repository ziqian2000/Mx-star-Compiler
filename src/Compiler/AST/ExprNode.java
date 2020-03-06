package Compiler.AST;

import Compiler.IR.Operand.Operand;
import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.SymbolTable.Symbol.VarSymbol;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Position;

public abstract class ExprNode extends BaseNode {

	public enum category{
		LVALUE, RVALUE
	}

	private Type type;
	private Symbol symbol;
	private category valueCategory;

	public ExprNode(Position position){
		super(position);
		this.valueCategory = category.RVALUE;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
		if(symbol instanceof VarSymbol)
			setValueCategory(category.LVALUE);
	}

	public category getValueCategory() {
		return valueCategory;
	}

	public void setValueCategory(category valueCategory) {
		this.valueCategory = valueCategory;
	}
}
