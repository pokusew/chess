package cz.martinendler.chess.engine;

import cz.martinendler.chess.ui.Piece;

import java.util.ArrayList;
import java.util.List;

public class Game {

	protected final List<List<Piece>> pieces;
	protected final List<Move> moves;

	protected int nextMoveId;

	public Game() {

		pieces = new ArrayList<>(2);
		pieces.set(PlayerType.WHITE.id, new ArrayList<>(16));
		pieces.set(PlayerType.BLACK.id, new ArrayList<>(16));

		moves = new ArrayList<>();

		nextMoveId = 0;

	}

	public int getMovesCount() {
		assert moves.size() == nextMoveId;
		return nextMoveId;
	}

	public PlayerType getPlayerOnMove() {
		// in chess the first move is always played by the WHITE player
		// then they take turns
		// thus we can safely compute which player is on move from the nextMoveId
		return PlayerType.fromId(nextMoveId % PlayerType.getTotalCount());
	}

	protected void incrementModeId() {
		nextMoveId++;
	}

	public boolean isLegalMove(Move move) {
		// TODO
		return false;
	}

	public boolean doMove(Move move) {

		if (!isLegalMove(move)) {
			return false;
		}

		// TODO

		return true;

	}

	public List<Piece> getPiecesOf(PlayerType player) {
		return pieces.get(player.id);
	}

	// TODO
	public boolean isCheck() {
		return false;
	}

	// TODO
	public boolean isCheckmate() {
		return false;
	}

	// TODO
	public boolean isStalemate() {
		return false;
	}

}
