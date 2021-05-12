package cz.martinendler.chess.engine.board;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

/**
 * A square on a chessboard
 * <p>
 * Uses Little-Endian Rank-File Mapping (id = 8 * rank + file)
 *
 * @see <a href="https://www.chessprogramming.org/Square_Mapping_Considerations">Square Mapping Considerations on CPW</a>
 */
public enum Square {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via Square.ordinal())

	/**
	 * A 1 square (0)
	 */
	A1,
	/**
	 * B 1 square (1)
	 */
	B1,
	/**
	 * C 1 square (2)
	 */
	C1,
	/**
	 * D 1 square (3)
	 */
	D1,
	/**
	 * E 1 square (4)
	 */
	E1,
	/**
	 * F 1 square (5)
	 */
	F1,
	/**
	 * G 1 square (6)
	 */
	G1,
	/**
	 * H 1 square (7)
	 */
	H1,
	/**
	 * A 2 square (8)
	 */
	A2,
	/**
	 * B 2 square (9)
	 */
	B2,
	/**
	 * C 2 square (10)
	 */
	C2,
	/**
	 * D 2 square (11)
	 */
	D2,
	/**
	 * E 2 square (12)
	 */
	E2,
	/**
	 * F 2 square (13)
	 */
	F2,
	/**
	 * G 2 square (14)
	 */
	G2,
	/**
	 * H 2 square (15)
	 */
	H2,
	/**
	 * A 3 square (16)
	 */
	A3,
	/**
	 * B 3 square (17)
	 */
	B3,
	/**
	 * C 3 square (18)
	 */
	C3,
	/**
	 * D 3 square (19)
	 */
	D3,
	/**
	 * E 3 square (20)
	 */
	E3,
	/**
	 * F 3 square (21)
	 */
	F3,
	/**
	 * G 3 square (22)
	 */
	G3,
	/**
	 * H 3 square (23)
	 */
	H3,
	/**
	 * A 4 square (24)
	 */
	A4,
	/**
	 * B 4 square (25)
	 */
	B4,
	/**
	 * C 4 square (26)
	 */
	C4,
	/**
	 * D 4 square (27)
	 */
	D4,
	/**
	 * E 4 square (28)
	 */
	E4,
	/**
	 * F 4 square (29)
	 */
	F4,
	/**
	 * G 4 square (30)
	 */
	G4,
	/**
	 * H 4 square (31)
	 */
	H4,
	/**
	 * A 5 square (32)
	 */
	A5,
	/**
	 * B 5 square (33)
	 */
	B5,
	/**
	 * C 5 square (34)
	 */
	C5,
	/**
	 * D 5 square (35)
	 */
	D5,
	/**
	 * E 5 square (36)
	 */
	E5,
	/**
	 * F 5 square (37)
	 */
	F5,
	/**
	 * G 5 square (38)
	 */
	G5,
	/**
	 * H 5 square (39)
	 */
	H5,
	/**
	 * A 6 square (40)
	 */
	A6,
	/**
	 * B 6 square (41)
	 */
	B6,
	/**
	 * C 6 square (42)
	 */
	C6,
	/**
	 * D 6 square (43)
	 */
	D6,
	/**
	 * E 6 square (44)
	 */
	E6,
	/**
	 * F 6 square (45)
	 */
	F6,
	/**
	 * G 6 square (46)
	 */
	G6,
	/**
	 * H 6 square (47)
	 */
	H6,
	/**
	 * A 7 square (48)
	 */
	A7,
	/**
	 * B 7 square (49)
	 */
	B7,
	/**
	 * C 7 square (50)
	 */
	C7,
	/**
	 * D 7 square (51)
	 */
	D7,
	/**
	 * E 7 square (52)
	 */
	E7,
	/**
	 * F 7 square (53)
	 */
	F7,
	/**
	 * G 7 square (54)
	 */
	G7,
	/**
	 * H 7 square (55)
	 */
	H7,
	/**
	 * A 8 square (56)
	 */
	A8,
	/**
	 * B 8 square (57)
	 */
	B8,
	/**
	 * C 8 square (58)
	 */
	C8,
	/**
	 * D 8 square (59)
	 */
	D8,
	/**
	 * E 8 square (60)
	 */
	E8,
	/**
	 * F 8 square (61)
	 */
	F8,
	/**
	 * G 8 square (62)
	 */
	G8,
	/**
	 * H 8 square (63)
	 */
	H8;

	private static final Square[] allSquares = Square.values();
	private static final Rank[] rankValues = Rank.values();
	private static final File[] fileValues = File.values();
	private static final long[] bitboard = new long[allSquares.length];

	private static final EnumMap<Square, Square[]> sideSquare = new EnumMap<>(Square.class);

	static {

		// initialize bitboard and sideSquare static final variables

		for (Square sq : allSquares) {

			bitboard[sq.ordinal()] = 1L << sq.ordinal();

			// a left-most square has only one side neighbour
			if (File.FILE_A.equals(sq.getFile())) {
				sideSquare.put(sq, new Square[]{
					encode(sq.getRank(), File.FILE_B),
				});
				continue;
			}

			// a right-most square has only one side neighbour
			if (File.FILE_H.equals(sq.getFile())) {
				sideSquare.put(sq, new Square[]{
					encode(sq.getRank(), File.FILE_G),
				});
				continue;
			}

			sideSquare.put(sq, new Square[]{
				// left side neighbour
				encode(sq.getRank(), fileValues[sq.getFile().ordinal() - 1]),
				// right side neighbour
				encode(sq.getRank(), fileValues[sq.getFile().ordinal() + 1]),
			});

		}

	}

	/**
	 * Gets square by its rank and file
	 *
	 * @param rank the rank
	 * @param file the file
	 * @return the square
	 */
	public static @NotNull Square encode(@NotNull Rank rank, @NotNull File file) {
		return allSquares[rank.ordinal() * 8 + file.ordinal()];
	}

	/**
	 * Gets square by its ordinal number / index / id
	 *
	 * @param index the ordinal number of the required square [0, 63]
	 * @return the square
	 * @throws IllegalArgumentException when the given index is out of allowed range [0, 63]
	 */
	public static @NotNull Square fromIndex(int index) {
		if (index < 0 || index >= allSquares.length) {
			throw new IllegalArgumentException(
				"Index value " + index + " is out of allowed range [0, " + allSquares.length + "]"
			);
		}
		return allSquares[index];
	}

	/**
	 * Gets side neighbour squares
	 *
	 * @return an array of left-right-side neighbour squares (1 or 2)
	 */
	public @NotNull Square[] getSideSquares() {
		return sideSquare.get(this);
	}

	/**
	 * Gets the rank in which this square is
	 *
	 * @return the rank of this square
	 */
	public Rank getRank() {
		return rankValues[this.ordinal() / 8];
	}

	/**
	 * Gets the file in which this square is
	 *
	 * @return the file of this square
	 */
	public File getFile() {
		return fileValues[this.ordinal() % 8];
	}

	/**
	 * Gets the corresponding bitboard for this square
	 *
	 * @return the bitboard
	 */
	public long getBitboard() {
		return bitboard[this.ordinal()];
	}

	/**
	 * Checks whether this square is light
	 *
	 * @return {@code true} when this square is white, {@code false} when this square is dark
	 */
	public boolean isLightSquare() {
		return (getBitboard() & Bitboard.lightSquaresBB) != 0L;
	}

}
