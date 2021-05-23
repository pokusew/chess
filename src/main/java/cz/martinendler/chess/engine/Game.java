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

	/**
	 * Players info
	 */
	private final @NotNull EnumMap<@NotNull Side, @NotNull Player> players;

	/**
	 * Boards history
	 * Always holds: {@code boards.size() + 1 == moveLog.size()}
	 * {@code boards.get(0)} --> starting board
	 * {@code boards.get(1)} --> board AFTER move 0
	 * {@code boards.get(2)} --> board AFTER move 1
	 * {@code boards.get(N)} --> board AFTER move N-1
	 */
	private final @NotNull List<@NotNull Board> boards;
	/**
	 * Moves history
	 */
	private final @NotNull List<@NotNull MoveLogEntry> moveLog;

	/**
	 * Latest board
	 */
	private Board board;
	/**
	 * Legal moves for the {@link #board}
	 */
	private List<Move> legalMoves;
	/**
	 * Legal moves for the {@link #board}, indexed by from (i.e. origin) square
	 */
	private final long[] legalMovesOfSquare;

	/**
	 * Random number generator for {@link #doRandomMove()}
	 */
	private final Random random = new Random();

	/**
	 * Creates a new game from a given starting chess position
	 *
	 * @param fen the starting chess position in FEN format
	 */
	public Game(String fen) {

		players = new EnumMap<>(Side.class);
		players.put(Side.WHITE, new Player("WHITE player"));
		players.put(Side.BLACK, new Player("BLACK player"));

		boards = new LinkedList<>();
		moveLog = new LinkedList<>();

		board = new Board();
		board.loadFromFen(fen);
		boards.add(new Board(board));

		legalMovesOfSquare = new long[Square.values().length];

		// sync legal moves cache
		updateState();

	}

	public Game(PgnGame game) throws GameLoadingException, MoveConversionException {

		players = new EnumMap<>(Side.class);
		players.put(Side.WHITE, new Player(game.tags.get("White")));
		players.put(Side.BLACK, new Player(game.tags.get("Black")));

		boards = new LinkedList<>();
		moveLog = new LinkedList<>();

		board = new Board();
		board.loadFromFen(game.resolveSetUpFEN());
		boards.add(new Board(board));

		legalMovesOfSquare = new long[Square.values().length];

		// tries to replay all moves in the PGN game
		for (String sanMove : game.moves) {

			Move move = SanUtils.decodeSan(board, sanMove, board.getSideToMove());

			MoveLogEntry moveLogEntry = board.doMove(move, true);

			if (moveLogEntry == null) {
				throw new GameLoadingException("Game could not be loaded due to invalid moves");
			}

			moveLog.add(moveLogEntry);
			boards.add(new Board(board));

		}

		if (getResult() != game.termination) {
			log.info("getResult() {} != game.termination {}", getResult(), game.termination);
		}

		// sync legal moves cache
		updateState();

	}

	/**
	 * Computes {@link #legalMoves} and {@link #legalMovesOfSquare} for the current {@link #board}
	 */
	private void updateState() {

		legalMoves = board.generateLegalMoves();

		Arrays.fill(legalMovesOfSquare, 0L);

		for (Move move : legalMoves) {
			legalMovesOfSquare[move.getFrom().ordinal()] |= move.getTo().getBitboard();
		}

	}

	/**
	 * Checks if the given move leads to a pawn promotion
	 *
	 * @param from the origin square
	 * @param to   the target square
	 * @return {@code true} iff the given move leads to a pawn promotion
	 */
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
		boards.add(new Board(board));

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
		boards.add(new Board(board));

		updateState();

		return true;

	}

	/**
	 * Tries to undo the last move
	 *
	 * @return {@code true} if the move was undone and the board updated
	 */
	public boolean undoLastMove() {

		if (moveLog.size() == 0) {
			return false;
		}

		MoveLogEntry lastMoveLogEntry = moveLog.remove(moveLog.size() - 1);
		boards.remove(boards.size() - 1);

		board = lastMoveLogEntry.getBoard();

		updateState();

		return true;

	}

	/**
	 * Undoes last N moves
	 * <p>
	 * NOTE: Might successfully undo N - 1 move and still return {@code false}.
	 *
	 * @param n the number of moves to try to undo
	 * @return {@code false} if all N moves were undone successfully, {@code false} otherwise
	 */
	public boolean undoLastMoves(int n) {

		for (int i = 0; i < n; i++) {

			if (!undoLastMove()) {
				return false;
			}

		}

		return true;

	}

	/**
	 * Gets the board that was immediately after N-th move
	 *
	 * @param moveIdx the move index
	 * @return the board that was immediately after N-th move
	 * @throws IllegalArgumentException when the given moveIdx is out of the valid range
	 */
	private @NotNull Board getSpecificBoard(int moveIdx) {

		int boardIdx = moveIdx + 1;

		if (boardIdx < 0 || boardIdx >= boards.size()) {
			throw new IllegalArgumentException(
				"The given moveIdx " + moveIdx + " is out of the valid range [-1 ," + (moveLog.size() - 1) + "]"
			);
		}

		return boards.get(boardIdx);

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
	 * Gets the piece on the given square in board state that was immediately after N-th move
	 *
	 * @param moveIdx the move index
	 * @param square  the square
	 * @return the piece or {@code null} if there is no piece on the given square in board state
	 * that was immediately after N-th move
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
	 * (that was immediately after N-th move)
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
	 * <p>
	 * Returns {@code -1} if there is no move in the move log yet.
	 *
	 * @return the last move index in range [-1, number of moves - 1]
	 */
	public int getLastMoveIndex() {
		return moveLog.size() - 1;
	}

	/**
	 * Gets the specific move by index
	 *
	 * @param moveIdx the move index, in range [-1, moveLog.size() - 1]
	 * @return {@code moveIdx == -1 -> null}, otherwise a non-null {@link Move}
	 */
	public @Nullable Move getMove(int moveIdx) {

		if (moveIdx == -1) {
			return null;
		}

		return moveLog.get(moveIdx).getMove();

	}

}
