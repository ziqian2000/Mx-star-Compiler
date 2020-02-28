package Compiler.Parser;

import Compiler.Utils.Position;
import Compiler.Utils.SyntaxException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class MxstarErrorListener extends BaseErrorListener {

	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
		throw new SyntaxException(new Position(line, charPositionInLine), msg);
	}

}
