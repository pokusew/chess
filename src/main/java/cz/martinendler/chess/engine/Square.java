package cz.martinendler.chess.engine;

/**
 * A square on a chessboard within a chess {@link Game}
 */
public class Square {

	// row
	// 0-7
	public final int rank;

	// column
	// 0-7
	public final int file;

	// id = 8 * rank + file
	// 0-63
	public final int id;

	public Square(int rank, int file) {
		this.rank = rank;
		this.file = file;
		this.id = 8 * rank + file;
	}

}
