package cz.martinendler.chess.ui;

import org.jetbrains.annotations.NotNull;

public interface MoveAttemptHandler {

	void onLegalMoveAttempt(@NotNull Square origin, @NotNull Square destination);

}
