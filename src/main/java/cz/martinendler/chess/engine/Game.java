package cz.martinendler.chess.engine;

import cz.martinendler.chess.engine.board.Board;
import cz.martinendler.chess.engine.board.Square;
import cz.martinendler.chess.engine.move.Move;
import cz.martinendler.chess.engine.move.MoveConversionException;
import cz.martinendler.chess.engine.move.MoveLogEntry;
import cz.martinendler.chess.engine.move.SanUtils;
import cz.martinendler.chess.engine.pieces.Piece;
import cz.martinendler.chess.engine.pieces.PieceType;
import cz.martinendler.chess.pgn.entity.PgnGame;
import cz.martinendler.chess.pgn.entity.PgnGameTermination;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A chess game
 * <p>
 * It wraps {@link Board} and add support for moves history
 * and loading/saving {@link Board} state, moves history and other metadata in PGN format.
 * For PGN, under the hood, it uses classes from {@link cz.martinendler.chess.pgn} package.
 */
public class Game {

	private static final Logger log = LoggerFactory.getLogger(Game.class);

	private Board board;
	private List<Move> legalMoves;
	private final long[] legalMovesOfSquare;
	private final @NotNull List<@NotNull MoveLogEntry> moveLog;
	private final @NotNull EnumMap<@NotNull Side, @NotNull Player> players;

	private final Random random = new Random();

	public Game(String fen) {

		board = new Board();
		board.loadFromFen(fen);

		legalMovesOfSquare = new long[Square.values().length];

		moveLog = new LinkedList<>();

		players = new EnumMap<>(Side.class);
		players.put(Side.WHITE, new Player("WHITE player"));
		players.put(Side.BLACK, new Player("BLACK player"));

		updateState();

	}

	public Game(PgnGame game) throws GameLoadingException, MoveConversionException {

		board = new Board();
		board.loadFromFen(game.resolveSetUpFEN());

		legalMovesOfSquare = new long[Square.values().length];

		moveLog = new LinkedList<>();

		for (String sanMove : game.moves) {

			Move move = SanUtils.decodeSan(board, sanMove, board.getSideToMove());

			MoveLogEntry moveLogEntry = board.doMove(move, true);

			if (moveLogEntry == null) {
				throw new GameLoadingException("Game could not be loaded due to invalid moves");
			}

			moveLog.add(moveLogEntry);

		}

		if (getResult() != game.termination) {
			log.info("getResult() {} != game.termination {}", getResult(), game.termination);
		}

		players = new EnumMap<>(Side.class);
		players.put(Side.WHITE, new Player(game.tags.get("White")));
		players.put(Side.BLACK, new Player(game.tags.get("Black")));

		updateState();

	}

	private void updateState() {

		legalMoves = board.generateLegalMoves();

		Arrays.fill(legalMovesOfSquare, 0L);

		for (Move move : legalMoves) {
			legalMovesOfSquare[move.getFrom().ordinal()] |= move.getTo().getBitboard();
		}

	}

	public boolean isPromotionMove(@NotNull Square from, @NotNull Square to) {

		Optional<Move> move = legalMoves.stream()
			.filter(m -> m.getFrom() == from && m.getTo() == to)
			.findFirst();

		if (move.isEmpty()) {
			log.info("isPromotionMove: move.isEmpty() from={} to={}", from, to);
			return false;
		}

		return move.get().hasPromotion();

	}

	public boolean doMove(@NotNull Square from, @NotNull Square to, @Nullable PieceType promotionType) {

		Piece promotion = null;

		if (promotionType != null) {
			promotion = Piece.make(getSideToMove(), promotionType);
		}

		Move move = new Move(from, to, promotion);

		if (!legalMoves.contains(move)) {
			log.info("doMove: !legalMoves.contains({})", move);
			return false;
		}

		MoveLogEntry moveLogEntry = board.doMove(move, true);

		if (moveLogEntry == null) {
			log.error("doMove: moveLogEntry == null, move={}", move);
			return false;
		}

		moveLog.add(moveLogEntry);

		updateState();

		return true;

	}

	public boolean doRandomMove() {

		if (legalMoves.size() == 0) {
			return false;
		}

		Move randomMove = legalMoves.get(random.nextInt(legalMoves.size()));

		if (randomMove == null) {
			return false;
		}

		MoveLogEntry moveLogEntry = board.doMove(randomMove, true);

		if (moveLogEntry == null) {
			log.error("doRandomMove: moveLogEntry == null, move={}", randomMove);
			return false;
		}

		moveLog.add(moveLogEntry);

		updateState();

		return true;

	}

	public boolean undoLastMove() {

		if (moveLog.size() == 0) {
			return false;
		}

		MoveLogEntry lastMoveLogEntry = moveLog.remove(moveLog.size() - 1);

		board = lastMoveLogEntry.getBoard();

		updateState();

		return true;

	}

	/**
	 * Gets the board that was immediately before N-th move
	 *
	 * @param moveIdx the move index
	 * @return the board that was immediately before N-th move
	 * @throws IllegalArgumentException when the given moveIdx is out of the valid range
	 */
	private @NotNull Board getSpecificBoard(int moveIdx) {

		if (moveIdx < 0 || moveIdx >= moveLog.size()) {
			throw new IllegalArgumentException(
				"The given moveIdx " + moveIdx + " is out of the valid range [0 ," + (moveLog.size() - 1) + "]"
			);
		}

		return moveLog.get(moveIdx).getBoard();

	}

	/**
	 * Gets the piece on the given square in the current board state
	 *
	 * @param square the square
	 * @return the piece or {@code null} if there is no piece on the given square in the current board state
	 */
	public @Nullable Piece getPiece(@NotNull Square square) {
		return board.getPiece(square);
	}

	/**
	 * Gets the piece on the given square in board state that was immediately before N-th move
	 *
	 * @param moveIdx the move index
	 * @param square  the square
	 * @return the piece or {@code null} if there is no piece on the given square in board state
	 * that was immediately before N-th move
	 * @throws IllegalArgumentException when the given moveIdx is out of the valid range
	 */
	public @Nullable Piece getPiece(int moveIdx, @NotNull Square square) {
		return getSpecificBoard(moveIdx).getPiece(square);
	}

	/**
	 * Gets legal moves originating from the given square
	 *
	 * @param square the square
	 * @return legal moves as bitboard of target squares
	 */
	public long getLegalMoves(@NotNull Square square) {
		return legalMovesOfSquare[square.ordinal()];
	}

	/**
	 * Checks if the side-to-move's king is attacked (is in check)
	 *
	 * @return boolean {@code true} if the side-to-move's king is attacked, {@code false} otherwise
	 */
	public boolean isCheck() {
		return board.isKingAttacked();
	}

	/**
	 * Checks if there is a checkmate on the board
	 *
	 * @return {@code true} iff there is a checkmate on the board
	 */
	public boolean isCheckMate() {
		return board.isCheckMate();
	}

	/**
	 * Checks if there is a stalemate on the board
	 * (i.e. side-to-move is not in check but has not legal move)
	 *
	 * @return {@code true} iff there is a stalemate on the board
	 */
	public boolean isStaleMate() {
		return board.isStaleMate();
	}

	/**
	 * Gets the current game result as a {@link PgnGameTermination}
	 *
	 * @return the current game result, {@link PgnGameTermination#UNKNOWN} if this is an ongoing game without result
	 */
	public PgnGameTermination getResult() {

		if (isCheckMate()) {
			return board.getSideToMove().isWhite()
				? PgnGameTermination.BLACK_WINS
				: PgnGameTermination.WHITE_WINS;
		}

		if (isStaleMate()) {
			return PgnGameTermination.DRAWN_GAME;
		}

		return PgnGameTermination.UNKNOWN;

	}

	/**
	 * Gets side to move
	 *
	 * @return the side to move
	 */
	public @NotNull Side getSideToMove() {
		return board.getSideToMove();
	}

	/**
	 * Generates the FEN representation of the underlying board
	 *
	 * @see Board#getFen(boolean includeCounters)
	 * @see Board#getFen()
	 */
	public String getFen() {
		return board.getFen();
	}

	/**
	 * Generates the FEN representation of the underlying board
	 * (that was immediately before N-th move)
	 *
	 * @param moveIdx the move index
	 * @throws IllegalArgumentException when the given moveIdx is out of the valid range
	 * @see Board#getFen(boolean includeCounters)
	 * @see Board#getFen()
	 */
	public String getFen(int moveIdx) {
		return getSpecificBoard(moveIdx).getFen();
	}

	/**
	 * Gets the move log
	 * TODO: better encapsulation (immutable view)
	 *
	 * @return the move log
	 */
	public @NotNull List<MoveLogEntry> getMoveLog() {
		return moveLog;
	}

	/**
	 * Gets player for the given side
	 *
	 * @param side the side
	 * @return the player for the given side
	 */
	public @NotNull Player getPlayer(@NotNull Side side) {
		return players.get(side);
	}

	/**
	 * Converts this game to a PGN game entity
	 *
	 * @return a PGN game entity
	 */
	public @NotNull PgnGame toPgnGame() {

		PgnGame pgnGame = new PgnGame();

		if (moveLog.size() > 0) {
			pgnGame.setSetUpFEN(moveLog.get(0).getBoard().getFen());
		}

		pgnGame.tags.put("White", getPlayer(Side.WHITE).getName());
		pgnGame.tags.put("Black", getPlayer(Side.BLACK).getName());
		pgnGame.tags.put("Result", getResult().getNotation());
		pgnGame.termination = getResult();

		moveLog.forEach(moveLogEntry -> pgnGame.moves.add(moveLogEntry.getSan()));

		return pgnGame;

	}

	/**
	 * Gets last move index
	 *
	 * Returns {@code -1} if there is no move in the move log yet.
	 *
	 * @return the last move index in range [-1, number of moves - 1]
	 */
	public int getLastMoveIndex() {
		return moveLog.size() - 1;
	}

}
