package cz.martinendler.chess.ui;

import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.board.Board;
import cz.martinendler.chess.ui.controllers.GameDialogController;
import org.jetbrains.annotations.NotNull;

/**
 * DTO for game options dialog
 *
 * @see GameDialogController
 */
public class GameOptions {

	private @NotNull GameType type;
	private @NotNull String startingFen;
	private @NotNull Side humanSide;
	private long whiteTimeLimit;
	private long blackTimeLimit;

	public enum GameType {
		HUMAN_HUMAN,
		HUMAN_COMPUTER;
	}

	public GameOptions() {
		this.type = GameType.HUMAN_HUMAN;
		this.startingFen = Board.STANDARD_STARTING_POSITION_FEN;
		this.humanSide = Side.WHITE;
		this.whiteTimeLimit = ChessClock.TEN_MINUTES;
		this.blackTimeLimit = ChessClock.TEN_MINUTES;
	}

	public GameOptions(
		@NotNull GameType type,
		@NotNull String startingFen,
		@NotNull Side humanSide,
		long whiteTimeLimitMinutes,
		long blackTimeLimitMinutes
	) {
		this.type = type;
		this.startingFen = startingFen;
		this.humanSide = humanSide;
		setWhiteTimeLimitFromMinutes(whiteTimeLimitMinutes);
		setBlackTimeLimitFromMinutes(blackTimeLimitMinutes);
	}

	public @NotNull GameType getType() {
		return type;
	}

	public void setType(@NotNull GameType type) {
		this.type = type;
	}

	public @NotNull String getStartingFen() {
		return startingFen;
	}

	public void setStartingFen(@NotNull String startingFen) {
		this.startingFen = startingFen;
	}

	public @NotNull Side getHumanSide() {
		return humanSide;
	}

	public void setHumanSide(@NotNull Side humanSide) {
		this.humanSide = humanSide;
	}

	public long getWhiteTimeLimit() {
		return whiteTimeLimit;
	}

	public long getWhiteTimeLimitInMinutes() {
		return whiteTimeLimit / 60000L;
	}

	public void setWhiteTimeLimitFromMinutes(long whiteTimeLimitFromMinutes) {
		this.whiteTimeLimit = whiteTimeLimitFromMinutes * 60 * 1000;
	}

	public long getBlackTimeLimit() {
		return blackTimeLimit;
	}

	public long getBlackTimeLimitInMinutes() {
		return blackTimeLimit / 60000L;
	}

	public void setBlackTimeLimitFromMinutes(long blackTimeLimitMinutes) {
		this.blackTimeLimit = blackTimeLimitMinutes * 60 * 1000;
	}

}
