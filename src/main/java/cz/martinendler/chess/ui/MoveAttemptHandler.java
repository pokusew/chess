package cz.martinendler.chess.ui;

import org.jetbrains.annotations.NotNull;

/**
 * Move attempt handler
 *
 * @see Board#getMoveAttemptHandler()
 * @see Board#setMoveAttemptHandler(MoveAttemptHandler moveAttemptHandler)
 */
public interface MoveAttemptHandler {

	/**
	 * Invoked by the {@link Board} when there is a legal move attempt
	 *
	 * @param origin      the move origin square
	 * @param destination the move destination square
	 */
	void onLegalMoveAttempt(@NotNull Square origin, @NotNull Square destination);

}
