package Compiler.Utils;

public class SyntaxException extends RuntimeException{

	private Position position;
	private String message;

	public SyntaxException(Position position, String message){
		this.position = position;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "[" + position.toString() + "][SyntaxException] " + message;
	}
}
