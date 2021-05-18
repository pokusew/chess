package cz.martinendler.chess.engine;

/**
 * A game loading exception
 */
public class GameLoadingException extends RuntimeException {

	/**
	 * Instantiates a game loading exception
	 */
	public GameLoadingException() {
		super();
	}

	/**
	 * Instantiates a game loading exception
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public GameLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a game loading exception
	 *
	 * @param message the message
	 */
	public GameLoadingException(String message) {
		super(message);
	}

	/**
	 * Instantiates a game loading exception
	 *
	 * @param cause the cause
	 */
	public GameLoadingException(Throwable cause) {
		super(cause);
	}

}
