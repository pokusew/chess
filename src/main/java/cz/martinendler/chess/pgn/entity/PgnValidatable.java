package cz.martinendler.chess.pgn.entity;

import cz.martinendler.chess.pgn.PgnValidationException;

/**
 * Interface for all PGN entities that can be "validated".
 * The precise meaning of the "validation" is in control of classes that implement this interface.
 */
public interface PgnValidatable {

	/**
	 * Validates the entity
	 * <p>
	 * The precise meaning of the "validation" is in control of classes that implement this interface.
	 *
	 * @throws PgnValidationException whe something is not valid (typically includes a reason in a message)
	 */
	void validate() throws PgnValidationException;

	/**
	 * Checks that the validation would not produce any exceptions
	 *
	 * @return {@code true} iff the {@link PgnValidatable#validate()} does NOT throw {@link PgnValidationException}
	 */
	boolean isValid();

}
