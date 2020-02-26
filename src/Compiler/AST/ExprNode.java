package Compiler.AST;

import Compiler.SymbolTable.Symbol.Symbol;
import Compiler.SymbolTable.Type.Type;
import Compiler.Utils.Position;

public abstract class ExprNode extends BaseNode {

	private Type type;
	private Symbol symbol;

	public ExprNode(Position position){
		super(position);
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
	}
}
