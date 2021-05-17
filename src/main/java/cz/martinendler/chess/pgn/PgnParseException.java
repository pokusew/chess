package cz.martinendler.chess.pgn;

import org.jetbrains.annotations.NotNull;

/**
 * A PGN parsing error
 * ALWAYS has a meaningful message tat can be shown in a UI
 */
public class PgnParseException extends Exception {

	public PgnParseException(@NotNull String message) {
		super(message);
	}

	public PgnParseException(@NotNull String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Gets a non-null meaningful message that can be shown in a UI
	 * @return a non-null meaningful message that can be shown in a UI
	 */
	@Override
	public @NotNull String getMessage() {
		return super.getMessage();
	}

}
