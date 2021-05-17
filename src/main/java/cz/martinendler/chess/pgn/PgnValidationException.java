package cz.martinendler.chess.pgn;

import org.jetbrains.annotations.NotNull;

/**
 * A PGN validation exception
 */
public class PgnValidationException extends Exception {

	/**
	 * Creates a PGN validation exception
	 *
	 * @param message a reason why the validation failed (what is invalid)
	 */
	public PgnValidationException(@NotNull String message) {
		super(message);
	}

	/**
	 * Gets the reason why the validation failed (what is invalid)
	 *
	 * @return a non-null meaningful message
	 */
	@Override
	public @NotNull String getMessage() {
		return super.getMessage();
	}

}
