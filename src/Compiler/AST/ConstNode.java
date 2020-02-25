package Compiler.AST;

import Compiler.Utils.Position;

public abstract class ConstNode extends ExprNode {

	public ConstNode(Position position){
		super(position);
	}

}
