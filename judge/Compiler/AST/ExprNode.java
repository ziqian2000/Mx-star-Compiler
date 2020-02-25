package Compiler.AST;

import Compiler.Utils.Position;

public abstract class ExprNode extends BaseNode {

	public ExprNode(Position position){
		super(position);
	}
}
