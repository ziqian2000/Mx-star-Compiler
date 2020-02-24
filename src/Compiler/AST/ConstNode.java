package Compiler.AST;

import Compiler.utils.Position;

public abstract class ConstNode extends ExprNode {

	public ConstNode(Position position){
		super(position);
	}

}
