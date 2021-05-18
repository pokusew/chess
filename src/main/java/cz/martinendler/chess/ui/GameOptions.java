package cz.martinendler.chess.ui;

import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.board.Board;
import cz.martinendler.chess.ui.controllers.GameDialogController;
import org.jetbrains.annotations.NotNull;

/**
 * Results of GameDialog
 *
 * @see GameDialogController
 */
public class GameOptions {

	private @NotNull GameType type;
	private @NotNull String startingFen;
	private @NotNull Side humanSide;

	public enum GameType {
		HUMAN_HUMAN,
		HUMAN_COMPUTER;
	}

	public GameOptions() {
		this.type = GameType.HUMAN_HUMAN;
		this.startingFen = Board.STANDARD_STARTING_POSITION_FEN;
		this.humanSide = Side.WHITE;
	}

	public GameOptions(@NotNull GameType type, @NotNull String startingFen, @NotNull Side humanSide) {
		this.type = type;
		this.startingFen = startingFen;
		this.humanSide = humanSide;
	}

	public @NotNull GameType getType() {
		return type;
	}

	public @NotNull String getStartingFen() {
		return startingFen;
	}

	public @NotNull Side getHumanSide() {
		return humanSide;
	}

	public void setType(@NotNull GameType type) {
		this.type = type;
	}

	public void setStartingFen(@NotNull String startingFen) {
		this.startingFen = startingFen;
	}

	public void setHumanSide(@NotNull Side humanSide) {
		this.humanSide = humanSide;
	}

}
