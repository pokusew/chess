package cz.martinendler.chess.engine.board;

import cz.martinendler.chess.engine.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

import static cz.martinendler.chess.engine.board.DiagonalA1H8.*;
import static cz.martinendler.chess.engine.board.DiagonalH1A8.*;
import static cz.martinendler.chess.engine.board.Square.*;

/**
 * Bitboard helpers
 *
 * @see <a href="https://www.chessprogramming.org/Bitboards">Bitboards on CPW</a>
 * @see <a href="https://gekomad.github.io/Cinnamon/BitboardCalculator/">Bitboard Calculator</a>
 */
public class Bitboard {

	/**
	 * The light squares on a chessboard as a bitboard
	 */
	public static final long lightSquaresBB = 0x55AA55AA55AA55AAL;
	/**
	 * The dark squares on a chessboard as a bitboard
	 */
	public static final long darkSquaresBB = 0xAA55AA55AA55AA55L;

	/**
	 * Bitboards for all ranks
	 */
	static final long[] rankBB = {
		0x00000000000000FFL, 0x000000000000FF00L, 0x0000000000FF0000L, 0x00000000FF000000L,
		0x000000FF00000000L, 0x0000FF0000000000L, 0x00FF000000000000L, 0xFF00000000000000L
	};
	/**
	 * Bitboards for all files
	 */
	static final long[] fileBB = {
		0x0101010101010101L, 0x0202020202020202L, 0x0404040404040404L, 0x0808080808080808L,
		0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
	};

	/**
	 * Mapping between a square Square and its corresponding diagonal DiagonalA1H8
	 */
	static final DiagonalA1H8[] squareToDiagonalA1H8 = {
		H8_A1, B1_H7, C1_H6, D1_H5, E1_H4, F1_H3, G1_H2, H1_H1,
		G8_A2, H8_A1, B1_H7, C1_H6, D1_H5, E1_H4, F1_H3, G1_H2,
		F8_A3, G8_A2, H8_A1, B1_H7, C1_H6, D1_H5, E1_H4, F1_H3,
		E8_A4, F8_A3, G8_A2, H8_A1, B1_H7, C1_H6, D1_H5, E1_H4,
		D8_A5, E8_A4, F8_A3, G8_A2, H8_A1, B1_H7, C1_H6, D1_H5,
		C8_A6, D8_A5, E8_A4, F8_A3, G8_A2, H8_A1, B1_H7, C1_H6,
		B8_A7, C8_A6, D8_A5, E8_A4, F8_A3, G8_A2, H8_A1, B1_H7,
		A8_A8, B8_A7, C8_A6, D8_A5, E8_A4, F8_A3, G8_A2, H8_A1
	};
	/**
	 * Mapping between a square Square and its corresponding anti-diagonal DiagonalH1A8
	 */
	static final DiagonalH1A8[] squareToDiagonalH1A8 = {
		A1_A1, B1_A2, C1_A3, D1_A4, E1_A5, F1_A6, G1_A7, H1_A8,
		B1_A2, C1_A3, D1_A4, E1_A5, F1_A6, G1_A7, H1_A8, B8_H2,
		C1_A3, D1_A4, E1_A5, F1_A6, G1_A7, H1_A8, B8_H2, C8_H3,
		D1_A4, E1_A5, F1_A6, G1_A7, H1_A8, B8_H2, C8_H3, D8_H4,
		E1_A5, F1_A6, G1_A7, H1_A8, B8_H2, C8_H3, D8_H4, E8_H5,
		F1_A6, G1_A7, H1_A8, B8_H2, C8_H3, D8_H4, E8_H5, F8_H6,
		G1_A7, H1_A8, B8_H2, C8_H3, D8_H4, E8_H5, F8_H6, G8_H7,
		H1_A8, B8_H2, C8_H3, D8_H4, E8_H5, F8_H6, G8_H7, H8_H8
	};

	/**
	 * Squares bitboard for each  DiagonalA1H8 diagonal
	 */
	static final long[] diagonalA1H8BB = {
		/* A8_A8  (0) */ sq2BB(A8),
		/* B8_A7  (1) */ sq2BB(B8) | sq2BB(A7),
		/* C8_A6  (2) */ sq2BB(C8) | sq2BB(B7) | sq2BB(A6),
		/* D8_A5  (3) */ sq2BB(D8) | sq2BB(C7) | sq2BB(B6) | sq2BB(A5),
		/* E8_A4  (4) */ sq2BB(E8) | sq2BB(D7) | sq2BB(C6) | sq2BB(B5) | sq2BB(A4),
		/* F8_A3  (5) */ sq2BB(F8) | sq2BB(E7) | sq2BB(D6) | sq2BB(C5) | sq2BB(B4) | sq2BB(A3),
		/* G8_A2  (6) */ sq2BB(G8) | sq2BB(F7) | sq2BB(E6) | sq2BB(D5) | sq2BB(C4) | sq2BB(B3) | sq2BB(A2),
		/* H8_A1  (7) */ sq2BB(H8) | sq2BB(G7) | sq2BB(F6) | sq2BB(E5) | sq2BB(D4) | sq2BB(C3) | sq2BB(B2) | sq2BB(A1),
		/* B1_H7  (8) */ sq2BB(B1) | sq2BB(C2) | sq2BB(D3) | sq2BB(E4) | sq2BB(F5) | sq2BB(G6) | sq2BB(H7),
		/* C1_H6  (9) */ sq2BB(C1) | sq2BB(D2) | sq2BB(E3) | sq2BB(F4) | sq2BB(G5) | sq2BB(H6),
		/* D1_H5 (10) */ sq2BB(D1) | sq2BB(E2) | sq2BB(F3) | sq2BB(G4) | sq2BB(H5),
		/* E1_H4 (11) */ sq2BB(E1) | sq2BB(F2) | sq2BB(G3) | sq2BB(H4),
		/* F1_H3 (12) */ sq2BB(F1) | sq2BB(G2) | sq2BB(H3),
		/* G1_H2 (13) */ sq2BB(G1) | sq2BB(H2),
		/* H1_H1 (14) */ sq2BB(H1)
	};
	/**
	 * Squares bitboard for each {@link DiagonalH1A8} diagonal
	 */
	static final long[] diagonalH1A8BB = {
		/* A1_A1  (0) */ sq2BB(A1),
		/* B1_A2  (1) */ sq2BB(B1) | sq2BB(A2),
		/* C1_A3  (2) */ sq2BB(C1) | sq2BB(B2) | sq2BB(A3),
		/* D1_A4  (3) */ sq2BB(D1) | sq2BB(C2) | sq2BB(B3) | sq2BB(A4),
		/* E1_A5  (4) */ sq2BB(E1) | sq2BB(D2) | sq2BB(C3) | sq2BB(B4) | sq2BB(A5),
		/* F1_A6  (5) */ sq2BB(F1) | sq2BB(E2) | sq2BB(D3) | sq2BB(C4) | sq2BB(B5) | sq2BB(A6),
		/* G1_A7  (6) */ sq2BB(G1) | sq2BB(F2) | sq2BB(E3) | sq2BB(D4) | sq2BB(C5) | sq2BB(B6) | sq2BB(A7),
		/* H1_A8  (7) */ sq2BB(H1) | sq2BB(G2) | sq2BB(F3) | sq2BB(E4) | sq2BB(D5) | sq2BB(C6) | sq2BB(B7) | sq2BB(A8),
		/* B8_H2  (8) */ sq2BB(B8) | sq2BB(C7) | sq2BB(D6) | sq2BB(E5) | sq2BB(F4) | sq2BB(G3) | sq2BB(H2),
		/* C8_H3  (9) */ sq2BB(C8) | sq2BB(D7) | sq2BB(E6) | sq2BB(F5) | sq2BB(G4) | sq2BB(H3),
		/* D8_H4 (10) */ sq2BB(D8) | sq2BB(E7) | sq2BB(F6) | sq2BB(G5) | sq2BB(H4),
		/* E8_H5 (11) */ sq2BB(E8) | sq2BB(F7) | sq2BB(G6) | sq2BB(H5),
		/* F8_H6 (12) */ sq2BB(F8) | sq2BB(G7) | sq2BB(H6),
		/* G8_H7 (13) */ sq2BB(G8) | sq2BB(H7),
		/* H8_H8 (14) */ sq2BB(H8)
	};

	/**
	 * Bitboard for all knight attacks
	 * <p>
	 * The N-th bitboard in this array represents squares
	 * that can be attacked by a knight that is on N-th square.
	 */
	static final long[] knightAttacks = {
		0x0000000000020400L, 0x0000000000050800L, 0x00000000000a1100L, 0x0000000000142200L, 0x0000000000284400L, 0x0000000000508800L, 0x0000000000a01000L, 0x0000000000402000L,
		0x0000000002040004L, 0x0000000005080008L, 0x000000000a110011L, 0x0000000014220022L, 0x0000000028440044L, 0x0000000050880088L, 0x00000000a0100010L, 0x0000000040200020L,
		0x0000000204000402L, 0x0000000508000805L, 0x0000000a1100110aL, 0x0000001422002214L, 0x0000002844004428L, 0x0000005088008850L, 0x000000a0100010a0L, 0x0000004020002040L,
		0x0000020400040200L, 0x0000050800080500L, 0x00000a1100110a00L, 0x0000142200221400L, 0x0000284400442800L, 0x0000508800885000L, 0x0000a0100010a000L, 0x0000402000204000L,
		0x0002040004020000L, 0x0005080008050000L, 0x000a1100110a0000L, 0x0014220022140000L, 0x0028440044280000L, 0x0050880088500000L, 0x00a0100010a00000L, 0x0040200020400000L,
		0x0204000402000000L, 0x0508000805000000L, 0x0a1100110a000000L, 0x1422002214000000L, 0x2844004428000000L, 0x5088008850000000L, 0xa0100010a0000000L, 0x4020002040000000L,
		0x0400040200000000L, 0x0800080500000000L, 0x1100110a00000000L, 0x2200221400000000L, 0x4400442800000000L, 0x8800885000000000L, 0x100010a000000000L, 0x2000204000000000L,
		0x0004020000000000L, 0x0008050000000000L, 0x00110a0000000000L, 0x0022140000000000L, 0x0044280000000000L, 0x0088500000000000L, 0x0010a00000000000L, 0x0020400000000000L
	};
	/**
	 * Bitboard for all white pawn attacks
	 * <p>
	 * The N-th bitboard in this array represents squares
	 * that can be attacked by a white pawn that is on N-th square.
	 */
	static final long[] whitePawnAttacks = {
		0x0000000000000200L, 0x0000000000000500L, 0x0000000000000a00L, 0x0000000000001400L, 0x0000000000002800L, 0x0000000000005000L, 0x000000000000a000L, 0x0000000000004000L,
		0x0000000000020000L, 0x0000000000050000L, 0x00000000000a0000L, 0x0000000000140000L, 0x0000000000280000L, 0x0000000000500000L, 0x0000000000a00000L, 0x0000000000400000L,
		0x0000000002000000L, 0x0000000005000000L, 0x000000000a000000L, 0x0000000014000000L, 0x0000000028000000L, 0x0000000050000000L, 0x00000000a0000000L, 0x0000000040000000L,
		0x0000000200000000L, 0x0000000500000000L, 0x0000000a00000000L, 0x0000001400000000L, 0x0000002800000000L, 0x0000005000000000L, 0x000000a000000000L, 0x0000004000000000L,
		0x0000020000000000L, 0x0000050000000000L, 0x00000a0000000000L, 0x0000140000000000L, 0x0000280000000000L, 0x0000500000000000L, 0x0000a00000000000L, 0x0000400000000000L,
		0x0002000000000000L, 0x0005000000000000L, 0x000a000000000000L, 0x0014000000000000L, 0x0028000000000000L, 0x0050000000000000L, 0x00a0000000000000L, 0x0040000000000000L,
		0x0200000000000000L, 0x0500000000000000L, 0x0a00000000000000L, 0x1400000000000000L, 0x2800000000000000L, 0x5000000000000000L, 0xa000000000000000L, 0x4000000000000000L,
		0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L
	};
	/**
	 * Bitboard for all black pawn attacks
	 * <p>
	 * The N-th bitboard in this array represents squares
	 * that can be attacked by a black pawn that is on N-th square.
	 */
	static final long[] blackPawnAttacks = {
		0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
		0x0000000000000002L, 0x0000000000000005L, 0x000000000000000aL, 0x0000000000000014L, 0x0000000000000028L, 0x0000000000000050L, 0x00000000000000a0L, 0x0000000000000040L,
		0x0000000000000200L, 0x0000000000000500L, 0x0000000000000a00L, 0x0000000000001400L, 0x0000000000002800L, 0x0000000000005000L, 0x000000000000a000L, 0x0000000000004000L,
		0x0000000000020000L, 0x0000000000050000L, 0x00000000000a0000L, 0x0000000000140000L, 0x0000000000280000L, 0x0000000000500000L, 0x0000000000a00000L, 0x0000000000400000L,
		0x0000000002000000L, 0x0000000005000000L, 0x000000000a000000L, 0x0000000014000000L, 0x0000000028000000L, 0x0000000050000000L, 0x00000000a0000000L, 0x0000000040000000L,
		0x0000000200000000L, 0x0000000500000000L, 0x0000000a00000000L, 0x0000001400000000L, 0x0000002800000000L, 0x0000005000000000L, 0x000000a000000000L, 0x0000004000000000L,
		0x0000020000000000L, 0x0000050000000000L, 0x00000a0000000000L, 0x0000140000000000L, 0x0000280000000000L, 0x0000500000000000L, 0x0000a00000000000L, 0x0000400000000000L,
		0x0002000000000000L, 0x0005000000000000L, 0x000a000000000000L, 0x0014000000000000L, 0x0028000000000000L, 0x0050000000000000L, 0x00a0000000000000L, 0x0040000000000000L
	};
	/**
	 * Bitboard for all white pawn moves
	 */
	static final long[] whitePawnMoves = {
		0x0000000000000100L, 0x0000000000000200L, 0x0000000000000400L, 0x0000000000000800L, 0x0000000000001000L, 0x0000000000002000L, 0x0000000000004000L, 0x0000000000008000L,
		0x0000000001010000L, 0x0000000002020000L, 0x0000000004040000L, 0x0000000008080000L, 0x0000000010100000L, 0x0000000020200000L, 0x0000000040400000L, 0x0000000080800000L,
		0x0000000001000000L, 0x0000000002000000L, 0x0000000004000000L, 0x0000000008000000L, 0x0000000010000000L, 0x0000000020000000L, 0x0000000040000000L, 0x0000000080000000L,
		0x0000000100000000L, 0x0000000200000000L, 0x0000000400000000L, 0x0000000800000000L, 0x0000001000000000L, 0x0000002000000000L, 0x0000004000000000L, 0x0000008000000000L,
		0x0000010000000000L, 0x0000020000000000L, 0x0000040000000000L, 0x0000080000000000L, 0x0000100000000000L, 0x0000200000000000L, 0x0000400000000000L, 0x0000800000000000L,
		0x0001000000000000L, 0x0002000000000000L, 0x0004000000000000L, 0x0008000000000000L, 0x0010000000000000L, 0x0020000000000000L, 0x0040000000000000L, 0x0080000000000000L,
		0x0100000000000000L, 0x0200000000000000L, 0x0400000000000000L, 0x0800000000000000L, 0x1000000000000000L, 0x2000000000000000L, 0x4000000000000000L, 0x8000000000000000L,
		0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L
	};
	/**
	 * Bitboard for all black pawn moves
	 */
	static final long[] blackPawnMoves = {
		0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
		0x0000000000000001L, 0x0000000000000002L, 0x0000000000000004L, 0x0000000000000008L, 0x0000000000000010L, 0x0000000000000020L, 0x0000000000000040L, 0x0000000000000080L,
		0x0000000000000100L, 0x0000000000000200L, 0x0000000000000400L, 0x0000000000000800L, 0x0000000000001000L, 0x0000000000002000L, 0x0000000000004000L, 0x0000000000008000L,
		0x0000000000010000L, 0x0000000000020000L, 0x0000000000040000L, 0x0000000000080000L, 0x0000000000100000L, 0x0000000000200000L, 0x0000000000400000L, 0x0000000000800000L,
		0x0000000001000000L, 0x0000000002000000L, 0x0000000004000000L, 0x0000000008000000L, 0x0000000010000000L, 0x0000000020000000L, 0x0000000040000000L, 0x0000000080000000L,
		0x0000000100000000L, 0x0000000200000000L, 0x0000000400000000L, 0x0000000800000000L, 0x0000001000000000L, 0x0000002000000000L, 0x0000004000000000L, 0x0000008000000000L,
		0x0000010100000000L, 0x0000020200000000L, 0x0000040400000000L, 0x0000080800000000L, 0x0000101000000000L, 0x0000202000000000L, 0x0000404000000000L, 0x0000808000000000L,
		0x0001000000000000L, 0x0002000000000000L, 0x0004000000000000L, 0x0008000000000000L, 0x0010000000000000L, 0x0020000000000000L, 0x0040000000000000L, 0x0080000000000000L
	};
	/**
	 * Bitboard for all adjacent squares
	 * <p>
	 * For example, {@code Bitboard.adjacentSquares[21]}:
	 * <pre>
	 * .   A B C D E F G H   .
	 * 8 | 0 0 0 0 0 0 0 0 | 8
	 * 7 | 0 0 0 0 0 0 0 0 | 7
	 * 6 | 0 0 0 0 0 0 0 0 | 6
	 * 5 | 0 0 0 0 0 0 0 0 | 5
	 * 4 | 0 0 0 0 1 1 1 0 | 4
	 * 3 | 0 0 0 0 1 0 1 0 | 3
	 * 2 | 0 0 0 0 1 1 1 0 | 2
	 * 1 | 0 0 0 0 0 0 0 0 | 1
	 * .   A B C D E F G H   .
	 * </pre>
	 */
	static final long[] adjacentSquares = {
		0x0000000000000302L, 0x0000000000000705L, 0x0000000000000e0aL, 0x0000000000001c14L, 0x0000000000003828L, 0x0000000000007050L, 0x000000000000e0a0L, 0x000000000000c040L,
		0x0000000000030203L, 0x0000000000070507L, 0x00000000000e0a0eL, 0x00000000001c141cL, 0x0000000000382838L, 0x0000000000705070L, 0x0000000000e0a0e0L, 0x0000000000c040c0L,
		0x0000000003020300L, 0x0000000007050700L, 0x000000000e0a0e00L, 0x000000001c141c00L, 0x0000000038283800L, 0x0000000070507000L, 0x00000000e0a0e000L, 0x00000000c040c000L,
		0x0000000302030000L, 0x0000000705070000L, 0x0000000e0a0e0000L, 0x0000001c141c0000L, 0x0000003828380000L, 0x0000007050700000L, 0x000000e0a0e00000L, 0x000000c040c00000L,
		0x0000030203000000L, 0x0000070507000000L, 0x00000e0a0e000000L, 0x00001c141c000000L, 0x0000382838000000L, 0x0000705070000000L, 0x0000e0a0e0000000L, 0x0000c040c0000000L,
		0x0003020300000000L, 0x0007050700000000L, 0x000e0a0e00000000L, 0x001c141c00000000L, 0x0038283800000000L, 0x0070507000000000L, 0x00e0a0e000000000L, 0x00c040c000000000L,
		0x0302030000000000L, 0x0705070000000000L, 0x0e0a0e0000000000L, 0x1c141c0000000000L, 0x3828380000000000L, 0x7050700000000000L, 0xe0a0e00000000000L, 0xc040c00000000000L,
		0x0203000000000000L, 0x0507000000000000L, 0x0a0e000000000000L, 0x141c000000000000L, 0x2838000000000000L, 0x5070000000000000L, 0xa0e0000000000000L, 0x40c0000000000000L
	};
	/**
	 * Bitboard for rank attacks
	 * <p>
	 * The N-th bitboard in this array represents squares
	 * that can be attacked in the rank that corresponds to a piece
	 * that is on N-th square (but the square itself is set to 0b).
	 * In other words, the bitboard has 7 bits set 1b.
	 */
	static final long[] rankAttacks = {
		sq2RA(A1), sq2RA(B1), sq2RA(C1), sq2RA(D1), sq2RA(E1), sq2RA(F1), sq2RA(G1), sq2RA(H1),
		sq2RA(A2), sq2RA(B2), sq2RA(C2), sq2RA(D2), sq2RA(E2), sq2RA(F2), sq2RA(G2), sq2RA(H2),
		sq2RA(A3), sq2RA(B3), sq2RA(C3), sq2RA(D3), sq2RA(E3), sq2RA(F3), sq2RA(G3), sq2RA(H3),
		sq2RA(A4), sq2RA(B4), sq2RA(C4), sq2RA(D4), sq2RA(E4), sq2RA(F4), sq2RA(G4), sq2RA(H4),
		sq2RA(A5), sq2RA(B5), sq2RA(C5), sq2RA(D5), sq2RA(E5), sq2RA(F5), sq2RA(G5), sq2RA(H5),
		sq2RA(A6), sq2RA(B6), sq2RA(C6), sq2RA(D6), sq2RA(E6), sq2RA(F6), sq2RA(G6), sq2RA(H6),
		sq2RA(A7), sq2RA(B7), sq2RA(C7), sq2RA(D7), sq2RA(E7), sq2RA(F7), sq2RA(G7), sq2RA(H7),
		sq2RA(A8), sq2RA(B8), sq2RA(C8), sq2RA(D8), sq2RA(E8), sq2RA(F8), sq2RA(G8), sq2RA(H8)
	};
	/**
	 * Bitboard for file attacks
	 * <p>
	 * The N-th bitboard in this array represents squares
	 * that can be attacked in the file that corresponds to a piece
	 * that is on N-th square (but the square itself is set to 0b).
	 * In other words, the bitboard has 7 bits set 1b.
	 */
	static final long[] fileAttacks = {
		sq2FA(A1), sq2FA(B1), sq2FA(C1), sq2FA(D1), sq2FA(E1), sq2FA(F1), sq2FA(G1), sq2FA(H1),
		sq2FA(A2), sq2FA(B2), sq2FA(C2), sq2FA(D2), sq2FA(E2), sq2FA(F2), sq2FA(G2), sq2FA(H2),
		sq2FA(A3), sq2FA(B3), sq2FA(C3), sq2FA(D3), sq2FA(E3), sq2FA(F3), sq2FA(G3), sq2FA(H3),
		sq2FA(A4), sq2FA(B4), sq2FA(C4), sq2FA(D4), sq2FA(E4), sq2FA(F4), sq2FA(G4), sq2FA(H4),
		sq2FA(A5), sq2FA(B5), sq2FA(C5), sq2FA(D5), sq2FA(E5), sq2FA(F5), sq2FA(G5), sq2FA(H5),
		sq2FA(A6), sq2FA(B6), sq2FA(C6), sq2FA(D6), sq2FA(E6), sq2FA(F6), sq2FA(G6), sq2FA(H6),
		sq2FA(A7), sq2FA(B7), sq2FA(C7), sq2FA(D7), sq2FA(E7), sq2FA(F7), sq2FA(G7), sq2FA(H7),
		sq2FA(A8), sq2FA(B8), sq2FA(C8), sq2FA(D8), sq2FA(E8), sq2FA(F8), sq2FA(G8), sq2FA(H8)
	};
	/**
	 * Bitboard for diagonal attacks (in direction of any {@link DiagonalA1H8} diagonal)
	 * <p>
	 * Note: The corresponding square itself is set to 0b.
	 */
	static final long[] diagA1H8Attacks = {
		sq2A1(A1), sq2A1(B1), sq2A1(C1), sq2A1(D1), sq2A1(E1), sq2A1(F1), sq2A1(G1), sq2A1(H1),
		sq2A1(A2), sq2A1(B2), sq2A1(C2), sq2A1(D2), sq2A1(E2), sq2A1(F2), sq2A1(G2), sq2A1(H2),
		sq2A1(A3), sq2A1(B3), sq2A1(C3), sq2A1(D3), sq2A1(E3), sq2A1(F3), sq2A1(G3), sq2A1(H3),
		sq2A1(A4), sq2A1(B4), sq2A1(C4), sq2A1(D4), sq2A1(E4), sq2A1(F4), sq2A1(G4), sq2A1(H4),
		sq2A1(A5), sq2A1(B5), sq2A1(C5), sq2A1(D5), sq2A1(E5), sq2A1(F5), sq2A1(G5), sq2A1(H5),
		sq2A1(A6), sq2A1(B6), sq2A1(C6), sq2A1(D6), sq2A1(E6), sq2A1(F6), sq2A1(G6), sq2A1(H6),
		sq2A1(A7), sq2A1(B7), sq2A1(C7), sq2A1(D7), sq2A1(E7), sq2A1(F7), sq2A1(G7), sq2A1(H7),
		sq2A1(A8), sq2A1(B8), sq2A1(C8), sq2A1(D8), sq2A1(E8), sq2A1(F8), sq2A1(G8), sq2A1(H8)
	};
	/**
	 * Bitboard for diagonal attacks (in direction of any {@link DiagonalH1A8} anti-diagonal)
	 * <p>
	 * Note: The corresponding square itself is set to 0b.
	 */
	static final long[] diagH1A8Attacks = {
		sq2H1(A1), sq2H1(B1), sq2H1(C1), sq2H1(D1), sq2H1(E1), sq2H1(F1), sq2H1(G1), sq2H1(H1),
		sq2H1(A2), sq2H1(B2), sq2H1(C2), sq2H1(D2), sq2H1(E2), sq2H1(F2), sq2H1(G2), sq2H1(H2),
		sq2H1(A3), sq2H1(B3), sq2H1(C3), sq2H1(D3), sq2H1(E3), sq2H1(F3), sq2H1(G3), sq2H1(H3),
		sq2H1(A4), sq2H1(B4), sq2H1(C4), sq2H1(D4), sq2H1(E4), sq2H1(F4), sq2H1(G4), sq2H1(H4),
		sq2H1(A5), sq2H1(B5), sq2H1(C5), sq2H1(D5), sq2H1(E5), sq2H1(F5), sq2H1(G5), sq2H1(H5),
		sq2H1(A6), sq2H1(B6), sq2H1(C6), sq2H1(D6), sq2H1(E6), sq2H1(F6), sq2H1(G6), sq2H1(H6),
		sq2H1(A7), sq2H1(B7), sq2H1(C7), sq2H1(D7), sq2H1(E7), sq2H1(F7), sq2H1(G7), sq2H1(H7),
		sq2H1(A8), sq2H1(B8), sq2H1(C8), sq2H1(D8), sq2H1(E8), sq2H1(F8), sq2H1(G8), sq2H1(H8)
	};

	/**
	 * Bitboard with bits between two given squares
	 * <p>
	 * {@code bitsBetween[x][y]} corresponds to a bitboard where all bits between bits x and y
	 * are set to 1b (including the bits x and y themselves).
	 * <p>
	 * For example, {@code Bitboard.bbTable[18][28]}:
	 * <pre>
	 * .   A B C D E F G H   .
	 * 8 | 0 0 0 0 0 0 0 0 | 8
	 * 7 | 0 0 0 0 0 0 0 0 | 7
	 * 6 | 0 0 0 0 0 0 0 0 | 6
	 * 5 | 0 0 0 0 0 0 0 0 | 5
	 * 4 | 1 1 1 1 1 0 0 0 | 4
	 * 3 | 0 0 1 1 1 1 1 1 | 3
	 * 2 | 0 0 0 0 0 0 0 0 | 2
	 * 1 | 0 0 0 0 0 0 0 0 | 1
	 * .   A B C D E F G H   .
	 * </pre>
	 * <p>
	 * TODO: What if x > y (it seems that it returns nonsense),
	 * or x == y (only bit x == y is set to 1b, that makes sense)?
	 */
	static final long[][] bitsBetween = new long[64][64];

	static {
		for (int x = 0; x < 64; x++) {
			for (int y = 0; y < 64; y++) {
				// TODO: ???
				bitsBetween[x][y] = ((1L << y) | ((1L << y) - (1L << x)));
			}
		}
	}

	/**
	 * Converts square to the corresponding bitboard
	 *
	 * @param sq the square
	 * @return the long
	 */
	static long sq2BB(@NotNull Square sq) {
		return sq.getBitboard();
	}

	/**
	 * Converts square to its corresponding rank bitboard
	 * but the bit that corresponds to the square itself is set to 0b.
	 *
	 * @param sq the square
	 * @return the bitboard
	 */
	static long sq2RA(@NotNull Square sq) {
		return (rankBB[sq.getRank().ordinal()] ^ sq2BB(sq));
	}

	/**
	 * Converts square to its corresponding file bitboard
	 * but the bit that corresponds to the square itself is set to 0b.
	 *
	 * @param sq the sq
	 * @return the bitboard
	 */
	static long sq2FA(@NotNull Square sq) {
		return (fileBB[sq.getFile().ordinal()] ^ sq.getBitboard());
	}

	/**
	 * Converts square to its corresponding {@link DiagonalA1H8} diagonal
	 * but the bit that corresponds to the square itself is set to 0b.
	 *
	 * @param sq the square
	 * @return the bitboard
	 */
	static long sq2A1(@NotNull Square sq) {
		return (diagonalA1H8BB[squareToDiagonalA1H8[sq.ordinal()].ordinal()] ^ sq2BB(sq));
	}

	/**
	 * Converts square to its corresponding {@link DiagonalH1A8} anti-diagonal
	 * but the bit that corresponds to the square itself is set to 0b.
	 *
	 * @param sq the square
	 * @return the bitboard
	 */
	static long sq2H1(@NotNull Square sq) {
		return (diagonalH1A8BB[squareToDiagonalH1A8[sq.ordinal()].ordinal()] ^ sq2BB(sq));
	}

	/**
	 * Determines the bit-index of the least significant 1 bit (LS1B)
	 *
	 * @param bitboard the bitboard
	 * @return the bit-index of the least significant 1 bit (LS1B)
	 * @see <a href="https://www.chessprogramming.org/BitScan">BitScan on CPW</a>
	 */
	public static int bitScanForward(long bitboard) {
		return Long.numberOfTrailingZeros(bitboard);
	}

	/**
	 * Determines the bit-index of the most significant 1 bit (MS1B)
	 *
	 * @param bitboard the bitboard
	 * @return the bit-index of the most significant 1 bit (MS1B)
	 * @see <a href="https://www.chessprogramming.org/BitScan">BitScan on CPW</a>
	 */
	public static int bitScanReverse(long bitboard) {
		return 63 - Long.numberOfLeadingZeros(bitboard);
	}

	/**
	 * Bits between
	 *
	 * @param bb  the bb
	 * @param sq1 the sq 1
	 * @param sq2 the sq 2
	 * @return long
	 */
	public static long bitsBetween(long bb, int sq1, int sq2) {
		return bitsBetween[sq1][sq2] & bb;
	}

	/**
	 * Removes the least significant bit (LSB) of the given bitboard
	 *
	 * @param bb the bitboard
	 * @return the bitboard without the LSB
	 * @see <a href="https://www.chessprogramming.org/General_Setwise_Operations#Reset">Least Significant One Reset on CPW</a>
	 */
	public static long removeLSB(long bb) {
		return bb & (bb - 1);
	}

	/**
	 * Returns true iff the given long has only 1bit in its two's complement binary representation
	 * <p>
	 * TODO: maybe remove, if not used
	 *
	 * @param bb the bitboard
	 * @return {@code true} iff the given long has only 1bit in its two's complement binary representation
	 */
	public static boolean hasOnly1Bit(long bb) {
		return bb != 0L && removeLSB(bb) == 0L;
		// original implementation that fails for (1L << 63)
		//   TODO: file bug in the original library?
		// return bb > 0L && extractLSB(bb) == 0L;
		// alternative implementation:
		// return Long.bitCount(bb) == 1;
	}

	/**
	 * Gets bitboard for the given square
	 * <p>
	 * TODO: maybe remove, if not used, otherwise at least rename (more meaningful name)
	 *
	 * @param sq the square
	 * @return the bitboard for the given square
	 */
	public static long getBBTable(@NotNull Square sq) {
		return 1L << sq.ordinal();
	}

	/**
	 * Gets slider attacks based on the attacks and occupancy mask
	 *
	 * @param attacks bitboard with possible attacks
	 * @param mask    all occupied squares
	 * @param index   index of the square where the attacking piece currently is
	 * @return ???
	 * @see <a href="https://www.chessprogramming.org/Sliding_Piece_Attacks">Sliding Piece Attacks on CPW</a>
	 */
	private static long getSliderAttacks(long attacks, long mask, int index) {

		long occ = mask & attacks;

		if (occ == 0L) {
			return attacks;
		}

		// TODO: How exactly does this work?
		// TODO: Cover with tests.
		long m = (1L << index) - 1L;
		long lowerMask = occ & m;
		long upperMask = occ & ~m;
		int minor = lowerMask == 0L ? 0 : bitScanReverse(lowerMask);
		int major = upperMask == 0L ? 63 : bitScanForward(upperMask);

		return bitsBetween(attacks, minor, major);

	}

	/**
	 * Gets the bishop attacks
	 *
	 * @param mask   the mask (all occupied squares)
	 * @param square the square where the bishop currently is
	 * @return bishop attacks
	 */
	public static long getBishopAttacks(long mask, @NotNull Square square) {
		// bishop is a sliding piece that can move along the diagonals and anti-diagonals
		return (
			getSliderAttacks(diagA1H8Attacks[square.ordinal()], mask, square.ordinal())
				| getSliderAttacks(diagH1A8Attacks[square.ordinal()], mask, square.ordinal())
		);
	}

	/**
	 * Gets the rook attacks
	 *
	 * @param mask   the mask (all occupied squares)
	 * @param square the square where the rook currently is
	 * @return rook attacks
	 */
	public static long getRookAttacks(long mask, @NotNull Square square) {
		// rook is a sliding piece that can move along the ranks or files
		return (
			getSliderAttacks(fileAttacks[square.ordinal()], mask, square.ordinal())
				| getSliderAttacks(rankAttacks[square.ordinal()], mask, square.ordinal())
		);
	}

	/**
	 * Gets the queen attacks
	 *
	 * @param mask   the mask (all occupied squares)
	 * @param square the square where the queen currently is
	 * @return queen attacks
	 */
	public static long getQueenAttacks(long mask, @NotNull Square square) {
		// queen is a sliding piece that can move along the ranks, files, diagonals and anti-diagonals
		return getRookAttacks(mask, square) | getBishopAttacks(mask, square);
	}

	/**
	 * Gets a bitboard with attacked squares by the knight in the given square
	 *
	 * @param square   the square where the knight currently is
	 * @param occupied the occupied
	 * @return knight attacks
	 */
	public static long getKnightAttacks(@NotNull Square square, long occupied) {
		return knightAttacks[square.ordinal()] & occupied;
	}

	/**
	 * Gets a bitboard with move squares by the pawn that is currently in the given square
	 *
	 * @param side   the side of the pawn
	 * @param square the square where the pawn currently is
	 * @return the pawn attacks
	 */
	public static long getPawnAttacks(@NotNull Side side, @NotNull Square square) {
		return side.isWhite()
			? whitePawnAttacks[square.ordinal()]
			: blackPawnAttacks[square.ordinal()];
	}

	/**
	 * Gets a bitboard with move squares by the pawn in the given square
	 *
	 * @param side            the side of the pawn
	 * @param square          the square of the pawn
	 * @param occupied        all occupied squares on the board
	 * @param enPassantTarget the en passant target square (the square of the opposite side's pawn
	 *                        that could be captured via en passant by this pawn)
	 * @return the pawn captures
	 */
	public static long getPawnCaptures(
		@NotNull Side side,
		@NotNull Square square,
		long occupied,
		@Nullable Square enPassantTarget
	) {

		long pawnAttacks = side.isWhite()
			? whitePawnAttacks[square.ordinal()]
			: blackPawnAttacks[square.ordinal()];

		if (enPassantTarget != null) {
			long ep = enPassantTarget.getBitboard();
			// compute the en passant square - if the side's pawn moves to this square
			// then the other side's pawn (that is on the enPassantTarget square) is captured via en passant
			// merge it with occupied bitboard so the pawnAttacks & occupied works correctly
			occupied |= side.isWhite() ? ep << 8L : ep >> 8L;
		}

		return pawnAttacks & occupied;

	}

	/**
	 * Gets a bitboard with move squares by the pawn in the given square
	 *
	 * @param side     the side of the pawn
	 * @param square   the square of the pawn
	 * @param occupied all occupied squares on the board
	 * @return the pawn moves
	 */
	public static long getPawnMoves(@NotNull Side side, @NotNull Square square, long occupied) {

		long pawnMoves = side.equals(Side.WHITE)
			? whitePawnMoves[square.ordinal()]
			: blackPawnMoves[square.ordinal()];

		long occ = occupied;

		// handle double moves:
		//   the whitePawnMoves/blackPawnMoves already correctly defines double moves
		//   BUT we have to ensure that pawn is NOT jumping over any occupied squares
		if (square.getRank() == Rank.RANK_2 && side.isWhite()) {
			if ((square.getBitboard() << 8 & occ) != 0L) {
				// prevent double move if the square the pawn would jumping over is NOT empty
				// (we simply mark the double move target square as occupied)
				occ |= square.getBitboard() << 16;
			}
		} else if (square.getRank() == Rank.RANK_7 && side.isBlack()) {
			if ((square.getBitboard() >> 8 & occ) != 0L) {
				// prevent double move if the square the pawn would jumping over is NOT empty
				// (we simply mark the double move target square as occupied)
				occ |= square.getBitboard() >> 16;
			}
		}

		// the pawn cannot move to the squares that are occupied
		return pawnMoves & ~occ;

	}

	/**
	 * Gets a bitboard with attacked squares by the king in the given square
	 *
	 * @param square the square where the king currently is
	 * @param mask   the mask of allowed target squares
	 * @return the king attacks
	 */
	public static long getKingAttacks(@NotNull Square square, long mask) {
		return adjacentSquares[square.ordinal()] & mask;
	}

	/**
	 * Converts the given bitboard to a list of squares
	 *
	 * @param pieces the pieces
	 * @return List of Square
	 */
	public static @NotNull List<Square> bbToSquareList(long pieces) {
		List<Square> squares = new LinkedList<>();
		while (pieces != 0L) {
			int sq = Bitboard.bitScanForward(pieces);
			pieces = Bitboard.removeLSB(pieces);
			squares.add(Square.fromIndex(sq));
		}
		return squares;
	}

	/**
	 * Converts the given list of squares to the bitboard
	 *
	 * @param squares the list of squares
	 * @return the bitboard
	 */
	public static long squareListToBB(@NotNull List<Square> squares) {
		long bb = 0L;
		for (Square sq : squares) {
			bb |= sq.getBitboard();
		}
		return bb;
	}

	/**
	 * Converts the given bitboard to an array of squares
	 *
	 * @param pieces the pieces
	 * @return array of squares
	 */
	public static @NotNull Square[] bbToSquareArray(long pieces) {
		Square[] squares = new Square[Long.bitCount(pieces)];
		int index = 0;
		while (pieces != 0L) {
			int sq = bitScanForward(pieces);
			pieces = removeLSB(pieces);
			squares[index++] = Square.fromIndex(sq);
		}
		return squares;
	}

	/**
	 * Gets array of ranks' bitboards
	 *
	 * @return the array of ranks' bitboards
	 */
	public static long[] getRankBB() {
		return rankBB;
	}

	/**
	 * Gets array of files' bitboards
	 *
	 * @return the array of files' bitboards
	 */
	public static long[] getFileBB() {
		return fileBB;
	}

	/**
	 * Gets the corresponding bitboard for the square's rank
	 *
	 * @param sq the sq
	 * @return the bitboard of the rank of the given square
	 */
	public static long getRankBB(@NotNull Square sq) {
		return rankBB[sq.getRank().ordinal()];
	}

	/**
	 * Gets the corresponding bitboard for the square's file
	 *
	 * @param sq the sq
	 * @return the bitboard of the file of the given square
	 */
	public static long getFileBB(@NotNull Square sq) {
		return fileBB[sq.getFile().ordinal()];
	}

	/**
	 * Gets the corresponding bitboard for the given rank
	 *
	 * @param rank the rank
	 * @return the bitboard of the given rank
	 */
	public static long getRankBB(@NotNull Rank rank) {
		return rankBB[rank.ordinal()];
	}

	/**
	 * Gets the corresponding bitboard for the given file
	 *
	 * @param file the file
	 * @return the bitboard of the given file
	 */
	public static long getFileBB(@NotNull File file) {
		return fileBB[file.ordinal()];
	}

	/**
	 * Coverts the given bitboard to an 8-lines bit string
	 * <p>
	 * TODO: add note obout the order
	 *
	 * @param bb the bitboard
	 * @return an 8-lines bit string
	 */
	public static @NotNull String bbToString(long bb) {

		StringBuilder sb = new StringBuilder();

		for (int x = 0; x < 64; x++) {
			if (((1L << x) & bb) != 0L) {
				sb.append("1");
			} else {
				sb.append("0");
			}
			if ((x + 1) % 8 == 0) {
				sb.append("\n");
			}
		}

		return sb.toString();

	}

	/**
	 * Converts the given bitboard to a pretty-printed ASCII chessboard string
	 * <p>
	 * An example: (bits number 8 and 63 are 1, others are 0) ({@code includeNames=true})
	 * <pre>
	 * .   A B C D E F G H   .
	 * 8 | 0 0 0 0 0 0 0 1 | 8
	 * 7 | 0 0 0 0 0 0 0 0 | 7
	 * 6 | 0 0 0 0 0 0 0 0 | 6
	 * 5 | 0 0 0 0 0 0 0 0 | 5
	 * 4 | 0 0 0 0 0 0 0 0 | 4
	 * 3 | 0 0 0 0 0 0 0 0 | 3
	 * 2 | 1 0 0 0 0 0 0 0 | 2
	 * 1 | 0 0 0 0 0 0 0 0 | 1
	 * .   A B C D E F G H   .
	 * </pre>
	 *
	 * @param bb           the bitboard
	 * @param includeNames {@code true} to also print files' and ranks' names
	 * @return a pretty-printed ASCII chessboard string
	 */
	public static @NotNull String bbToPrettyString(long bb, boolean includeNames) {

		StringBuilder sb = new StringBuilder();

		if (includeNames) {
			sb.append(".   ");
			for (int file = 0; file < 8; file++) {
				sb.append(File.values()[file].getNotation());
				sb.append(" ");
			}
			sb.append("  .\n");
		}

		for (int rank = 7; rank >= 0; rank--) {
			if (includeNames) {
				sb.append(rank + 1);
				sb.append(" | ");
			}
			for (int file = 0; file < 8; file++) {
				int idx = rank * 8 + file;
				long value = (1L << idx) & bb;
				sb.append(value != 0L ? "1" : "0");
				sb.append(" ");
			}
			if (includeNames) {
				sb.append("| ");
				sb.append(rank + 1);
			}
			sb.append("\n");
		}

		if (includeNames) {
			sb.append(".   ");
			for (int file = 0; file < 8; file++) {
				sb.append(File.values()[file].getNotation());
				sb.append(" ");
			}
			sb.append("  .\n");
		}

		return sb.toString();

	}

	public static @NotNull String bbToPrettyString(long bb) {
		return bbToPrettyString(bb, true);
	}

	/**
	 * Check if all required squares (bits set to 1 in the given mask) are empty
	 *
	 * @param occupied bitboard where each bit is set to 0 iff is empty
	 * @param mask     bitboard of the required squares
	 * @return {@code true} iff all required squares are empty
	 */
	public static boolean areSquaresFree(long occupied, long mask) {
		return (occupied & mask) == 0L;
	}

}
