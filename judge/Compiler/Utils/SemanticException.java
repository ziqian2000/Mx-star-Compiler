package Compiler.Utils;

import Compiler.Utils.Position;

public class SemanticException extends RuntimeException{

	private Position position;
	private String message;

	public SemanticException(Position position, String message){
		this.position = position;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "[" + position.toString() + "][SemanticException]" + message;
	}
}
