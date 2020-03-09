package Compiler.Utils;

public class FuckingException extends RuntimeException{

	private String message;

	public FuckingException(String message){
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "[FuckingException]" + message;
	}
}
