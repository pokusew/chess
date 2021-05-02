package cz.martinendler.chess.engine.move;

/**
 * The type Move conversion exception.
 */
public class MoveConversionException extends RuntimeException {

	/**
	 * Instantiates a new Move conversion exception.
	 */
	public MoveConversionException() {
		super();
	}

	/**
	 * Instantiates a new Move conversion exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public MoveConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new Move conversion exception.
	 *
	 * @param message the message
	 */
	public MoveConversionException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Move conversion exception.
	 *
	 * @param cause the cause
	 */
	public MoveConversionException(Throwable cause) {
		super(cause);
	}

}
