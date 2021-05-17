package cz.martinendler.chess.pgn;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple error listener that collects all parsing errors
 * <p>
 * A new instance should be created for each parsing.
 */
public class ErrorCollectorListener extends BaseErrorListener {

	private final List<String> errors = new ArrayList<>();

	@Override
	public void syntaxError(
		Recognizer<?, ?> recognizer,
		Object offendingSymbol,
		int line,
		int charPositionInLine,
		String msg,
		RecognitionException e
	) {
		errors.add("line " + line + ":" + charPositionInLine + " " + msg);
	}

	public int getErrorsCount() {
		return errors.size();
	}

	public boolean hasAnyErrors() {
		return getErrorsCount() > 0;
	}

	/**
	 * Gets all errors (one per line)
	 *
	 * @return all errors (one per line), an empty string ("") if there are no errors
	 */
	public @NotNull String getErrors() {
		return String.join("\n", errors) + "\n";
	}

}
