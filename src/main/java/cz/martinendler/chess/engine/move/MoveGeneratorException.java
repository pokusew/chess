package cz.martinendler.chess.engine.move;

/**
 * A move generation error
 */
public class MoveGeneratorException extends RuntimeException {

	/**
	 * Instantiates a new Move generator exception
	 */
	public MoveGeneratorException() {
		super();
	}

	/**
	 * Instantiates a new Move generator exception
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public MoveGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new Move generator exception
	 *
	 * @param message the message
	 */
	public MoveGeneratorException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Move generator exception
	 *
	 * @param cause the cause
	 */
	public MoveGeneratorException(Throwable cause) {
		super(cause);
	}

}
