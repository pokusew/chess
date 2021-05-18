package cz.martinendler.chess.engine.move;

import cz.martinendler.chess.engine.Castling;
import cz.martinendler.chess.engine.Notation;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.board.*;
import cz.martinendler.chess.engine.pieces.Piece;
import cz.martinendler.chess.engine.pieces.PieceType;
import cz.martinendler.chess.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Utils for working with SAN
 * TODO: needs refactoring
 */
public class SanUtils {

	/**
	 * Normalizes SAN move string (discards annotations)
	 *
	 * @param san the SAN move string
	 * @return normalized SAN move string
	 */
	private static String normalizeSan(String san) {
		return san.replace("+", "") // check indication
			.replace("#", "") // checkmate indication
			// other SAN move suffix annotations (subjective information)
			.replace("!", "")
			.replace("?", "")
			.replace("ep", "");
	}

	/**
	 * Adds check/mate annotation to the SAN move
	 *
	 * @param board the current state of the board
	 * @param san   string builder of the SAN move
	 */
	private static void addCheckFlag(@NotNull Board board, @NotNull StringBuilder san) {

		if (board.isKingAttacked()) {
			if (board.isCheckMate()) {
				san.append("#");
			} else {
				san.append("+");
			}
		}

	}

	// TODO: Javadoc
	private static long findLegalSquares(
		@NotNull Board board,
		@NotNull Square to,
		@Nullable Piece promotion,
		long pieces
	) {

		long result = 0L;

		if (pieces != 0L) {

			List<Square> sourceSqs = Bitboard.bbToSquareList(pieces);

			for (Square sourceSq : sourceSqs) {

				Move move = new Move(sourceSq, to, promotion);

				if (board.isMoveLegal(move, true)) {
					result |= sourceSq.getBitboard();
					break;
				}

			}

		}

		return result;

	}

	/**
	 * Encodes the given move log entry to SAN notation
	 *
	 * @param moveLogEntry the move log entry
	 * @return the encoded move in SAN notation
	 * @throws MoveConversionException if the given move is not legal for the current board state
	 */
	protected static String encodeToSan(
		final @NotNull MoveLogEntry moveLogEntry
	) throws MoveConversionException {
		return encode(moveLogEntry, Notation.SAN);
	}

	/**
	 * Encodes the given move log entry to FAN notation
	 *
	 * @param moveLogEntry the move log entry
	 * @return the encoded move in FAN notation
	 * @throws MoveConversionException if the given move is not legal for the current board state
	 */
	public static String encodeToFan(
		final @NotNull MoveLogEntry moveLogEntry
	) throws MoveConversionException {
		return encode(moveLogEntry, Notation.FAN);
	}

	/**
	 * Encodes the given move log entry to SAN/FAN notation
	 *
	 * @param moveLogEntry the move log entry
	 * @param notation     the notation
	 * @return the encoded move in SAN/FAN notation
	 * @throws MoveConversionException if the given move is not legal for the current board state
	 */
	protected static String encode(
		final @NotNull MoveLogEntry moveLogEntry,
		final @NotNull Notation notation
	) throws MoveConversionException {

		StringBuilder san = new StringBuilder();

		Piece piece = moveLogEntry.getMovingPiece();

		if (piece.isOfType(PieceType.KING)) {

			Castling castling = moveLogEntry.getMove().getCastling();

			if (castling != null) {

				san.append(castling.getNotation());

				// TODO
				// addCheckFlag(board, san);

				return san.toString();

			}

		}

		// normal pawn move (not pawn attack)
		boolean pawnMove = piece.isOfType(PieceType.PAWN)
			&& moveLogEntry.getMove().getFrom().getFile() == moveLogEntry.getMove().getTo().getFile();

		boolean ambiguityResolved = false;

		san.append(piece.getNotation(notation));

		if (!pawnMove) {
			// resolving ambiguous move
			long amb = moveLogEntry.getBoard().squareAttackedByPieceType(
				moveLogEntry.getMove().getTo(),
				moveLogEntry.getBoard().getSideToMove(),
				piece.getPieceType()
			);
			amb &= ~moveLogEntry.getMove().getFrom().getBitboard();
			if (amb != 0L) {
				List<Square> fromList = Bitboard.bbToSquareList(amb);
				for (Square from : fromList) {
					if (!moveLogEntry.getBoard().isMoveLegal(
						new Move(from, moveLogEntry.getMove().getTo()), false)
					) {
						amb ^= from.getBitboard();
					}
				}
			}
			if (amb != 0L) {
				if ((Bitboard.getFileBB(moveLogEntry.getMove().getFrom()) & amb) == 0L) {
					san.append(moveLogEntry.getMove().getFrom().getFile().getNotation().toLowerCase());
				} else if ((Bitboard.getRankBB(moveLogEntry.getMove().getFrom()) & amb) == 0L) {
					san.append(moveLogEntry.getMove().getFrom().getRank().getNotation().toLowerCase());
				} else {
					san.append(moveLogEntry.getMove().getFrom().toString().toLowerCase());
				}
				ambiguityResolved = true;
			}
		}


		Piece captured = moveLogEntry.getCapturedPiece();
		boolean isCapture = captured != null;
		if (isCapture) {
			if (!ambiguityResolved
				&& piece.getPieceType().equals(PieceType.PAWN)
			) {
				san.append(moveLogEntry.getMove().getFrom().getFile().getNotation().toLowerCase());
			}
			san.append("x");
		}
		san.append(moveLogEntry.getMove().getTo().toString().toLowerCase());
		if (moveLogEntry.getMove().getPromotion() != null) {
			san.append("=");
			san.append(moveLogEntry.getMove().getPromotion().getNotation(notation));
		}

		// TODO
		// addCheckFlag(board, san);

		return san.toString();

	}

	/**
	 * Decodes the given move in SAN to a {@link Move]
	 *
	 * @param board the board
	 * @param san   the san
	 * @param side  the side
	 * @return the decoded move
	 * @throws MoveConversionException if the move conversion fails
	 *                                 (e.g. the notation is ambiguous for the current board state)
	 */
	public static @NotNull Move decodeSan(
		@NotNull Board board,
		@NotNull String san,
		@NotNull Side side
	) throws MoveConversionException {

		san = normalizeSan(san);

		String strPromotion = StringUtils.afterSequence(san, "=", 1);
		san = StringUtils.beforeSequence(san, "=");

		char lastChar = san.charAt(san.length() - 1);

		// TODO: What if the equal sign for pawn promotions is missing?

		if (Character.isLetter(lastChar) && Character.toUpperCase(lastChar) != 'O') {
			san = san.substring(0, san.length() - 1);
			strPromotion = lastChar + "";
		}

		Castling castling = Castling.fromNotation(san);

		if (castling != null) {
			return castling.getKingMove(side);
		}

		if (san.length() == 3 &&
			Character.isUpperCase(san.charAt(2))) {
			strPromotion = san.substring(2, 3);
			san = san.substring(0, 2);
		}

		Square from = null;

		Square to = Square.fromNotation(StringUtils.lastSequence(san, 2));

		if (to == null) {
			throw new MoveConversionException(
				"Could not parse destination square[" + san + "]: " + san.toUpperCase()
			);
		}

		Piece promotion = null;

		if (!strPromotion.isEmpty()) {
			promotion = Piece.fromSanNotation(side, strPromotion);

			if (promotion == null) {
				throw new MoveConversionException(
					"Could not parse promotion piece from " + strPromotion
				);
			}

		}

		if (san.length() == 2) { //is pawn move
			long mask = Bitboard.getBBTable(to) - 1L;
			long xfrom = (side.equals(Side.WHITE) ? mask : ~mask)
				& Bitboard.getFileBB(to)
				& board.getBitboard(Piece.make(side, PieceType.PAWN));
			int f = side.equals(Side.BLACK)
				? Bitboard.bitScanForward(xfrom)
				: Bitboard.bitScanReverse(xfrom);
			if (f >= 0 && f <= 63) {
				from = Square.fromIndex(f);
			}
		} else {

			String strFrom = (san.contains("x")
				? StringUtils.beforeSequence(san, "x")
				: san.substring(0, san.length() - 2));

			if (strFrom == null || strFrom.length() == 0 || strFrom.length() > 3) {
				throw new MoveConversionException(
					"Could not parse 'from' square " + san + ": Too many/few characters."
				);
			}

			PieceType fromPiece = PieceType.PAWN;

			if (Character.isUpperCase(strFrom.charAt(0))) {
				fromPiece = PieceType.fromSanNotation(strFrom.charAt(0) + "");
				if (fromPiece == null) {
					throw new MoveConversionException(
						"Could not parse fromPiece from: " + strFrom.charAt(0) + ""
					);
				}
			}

			if (strFrom.length() == 3) {
				from = Square.valueOf(strFrom.substring(1, 3).toUpperCase());
			} else {
				String location = "";
				if (strFrom.length() == 2) {
					if (Character.isUpperCase(strFrom.charAt(0))) {
						location = strFrom.substring(1, 2);
					} else {
						location = strFrom.substring(0, 2);
						from = Square.valueOf(location.toUpperCase());
					}
				} else {
					if (Character.isLowerCase(strFrom.charAt(0))) {
						location = strFrom;
					}
				}
				if (location.length() < 2) {
					// resolving ambiguous from
					long xfrom = board.squareAttackedByPieceType(to, board.getSideToMove(), fromPiece);
					if (location.length() > 0) {
						if (Character.isDigit(location.charAt(0))) {
							int irank = Integer.parseInt(location);
							if (!(irank >= 1 && irank <= 8)) {
								throw new MoveConversionException("Could not parse rank: " + location);
							}
							Rank rank = Rank.fromIndex(irank - 1);
							xfrom &= Bitboard.getRankBB(rank);
						} else {
							try {
								File file = File.valueOf("FILE_" + location.toUpperCase());
								xfrom &= Bitboard.getFileBB(file);
							} catch (Exception e) {
								throw new MoveConversionException("Could not parse file: " + location);
							}
						}
					}
					if (xfrom != 0L) {
						if (!Bitboard.hasOnly1Bit(xfrom)) {
							xfrom = findLegalSquares(board, to, promotion, xfrom);
						}
						int f = Bitboard.bitScanForward(xfrom);
						if (f >= 0 && f <= 63) {
							from = Square.fromIndex(f);
						}
					}
				}
			}

		}

		if (from == null) {
			throw new MoveConversionException(
				"Could not parse 'from' square " + san + " to setup: " + board.getFen()
			);
		}

		return new Move(from, to, promotion);

	}

}
