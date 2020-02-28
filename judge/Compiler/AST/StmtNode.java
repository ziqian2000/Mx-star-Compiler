package Compiler.AST;

import Compiler.SymbolTable.Scope;
import Compiler.Utils.Position;

public abstract class StmtNode extends BaseNode {

	private Scope bodyScope;

	public StmtNode(Position position){
		super(position);
	}

	public Scope getBodyScope() {
		return bodyScope;
	}

	public void setBodyScope(Scope bodyScope) {
		this.bodyScope = bodyScope;
	}
}
