package Compiler.AST;

import Compiler.utils.Position;

public abstract class ExprNode extends BaseNode {

	public ExprNode(Position position){
		super(position);
	}
}
