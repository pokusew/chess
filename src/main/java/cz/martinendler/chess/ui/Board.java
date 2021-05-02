package cz.martinendler.chess.ui;

import cz.martinendler.chess.engine.Side;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A chessboard
 * <p>
 * It handles positioning of its children nodes (instances of `Square` and `BorderSegment`).
 * It is <strong>fully responsive</strong>. It automatically adapts the layout
 * according to the available width from its parent.
 */
public class Board extends Region {

	private static final Logger log = LoggerFactory.getLogger(Board.class);

	private final static Background COLOR_BROWN = new Background(
		new BackgroundFill(Color.web("7E4D38"), null, null)
	);

	public final static int NUM = 8;
	public final static int NUM_ROWS = NUM;
	public final static int NUM_COLS = NUM;

	// TODO: support vertical flip
	// TODO: support move highlighting

	protected final Square[][] squares;
	protected final BorderSegment[][] borderCorners;
	protected final BorderDescription[][] rowDescriptions;
	protected final BorderDescription[][] colDescriptions;

	public Board() {
		super();

		squares = new Square[NUM_ROWS][NUM_COLS];
		borderCorners = new BorderSegment[2][2];
		rowDescriptions = new BorderDescription[2][NUM_ROWS];
		colDescriptions = new BorderDescription[2][NUM_COLS];

		initChildren();

		// setBackground(COLOR_BROWN);

	}

	protected void initChildren() {

		boolean white = false;

		for (int r = 0; r < NUM_ROWS; r++) {
			white = !white;
			for (int c = 0; c < NUM_COLS; c++) {
				white = !white;
				squares[r][c] = new Square(r, c, white ? Side.WHITE : Side.BLACK);
				getChildren().add(squares[r][c]);
			}
		}

		boolean light = true;

		for (int r = 0; r < NUM_ROWS; r++) {
			String desc = Integer.toString(r + 1);
			rowDescriptions[0][r] = new BorderDescription(light, desc);
			light = !light;
			rowDescriptions[1][r] = new BorderDescription(light, desc);
			getChildren().add(rowDescriptions[0][r]);
			getChildren().add(rowDescriptions[1][r]);
		}

		light = true;

		for (int c = 0; c < NUM_COLS; c++) {
			// TODO: fix this awfulness
			String desc = new String(new char[]{(char) ('a' + c)});
			colDescriptions[0][c] = new BorderDescription(light, desc);
			light = !light;
			colDescriptions[1][c] = new BorderDescription(light, desc);
			getChildren().add(colDescriptions[0][c]);
			getChildren().add(colDescriptions[1][c]);
		}

		light = false;

		for (int r = 0; r < 2; r++) {
			light = !light;
			for (int c = 0; c < 2; c++) {
				light = !light;
				borderCorners[r][c] = new BorderSegment(light);
				getChildren().add(borderCorners[r][c]);
			}
		}

	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		double boardSize = Double.min(getWidth(), getHeight());
		// boardSize = 2 * borderSize + NUM * squareSize
		double unitSize = boardSize / 70;
		double borderSize = Math.floor(3 * unitSize);
		double squareSize = Math.floor(64 * unitSize / NUM);

		log.info(
			"layoutChildren: w = {}, h = {}, boardSize = {}, unitSize = {}, borderSize = {}, squareSize = {}",
			getWidth(), getHeight(), boardSize, unitSize, borderSize, squareSize
		);

		// layout (x,y) = (0,0) is at the top-left corner
		// BUT board (r,c) = (0,0) is the bottom-left corner

		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {

				Square square = squares[r][c];

				square.resizeRelocate(
					borderSize + c * squareSize, borderSize + (7 - r) * squareSize,
					squareSize, squareSize
				);

			}
		}

		for (int c = 0; c < NUM_COLS; c++) {
			BorderSegment desc0 = colDescriptions[0][c];
			desc0.resizeRelocate(
				borderSize + c * squareSize, borderSize + NUM * squareSize,
				squareSize, borderSize
			);
			BorderSegment desc1 = colDescriptions[1][c];
			desc1.resizeRelocate(
				borderSize + c * squareSize, 0,
				squareSize, borderSize
			);
		}

		for (int r = 0; r < NUM_ROWS; r++) {
			BorderSegment desc0 = rowDescriptions[0][r];
			desc0.resizeRelocate(
				0, borderSize + (7 - r) * squareSize,
				borderSize, squareSize
			);
			BorderSegment desc1 = rowDescriptions[1][r];
			desc1.resizeRelocate(
				borderSize + NUM * squareSize, borderSize + (7 - r) * squareSize,
				borderSize, squareSize
			);
		}

		for (int r = 0; r < 2; r++) {
			for (int c = 0; c < 2; c++) {
				BorderSegment corner = borderCorners[r][c];
				corner.resizeRelocate(
					c * (borderSize + NUM * squareSize), (1 - r) * (borderSize + NUM * squareSize),
					borderSize, borderSize
				);
			}
		}

		// previous version:
		// TODO: remove
		// List<Node> children = getManagedChildren();
		//
		// for (int i = 0; i < children.size(); i++) {
		//
		// 	final Node child = children.get(i);
		//
		// 	int row = i / 8;
		// 	int column = i % 8;
		//
		// 	child.resizeRelocate(
		// 		row * squareSize, column * squareSize,
		// 		squareSize, squareSize
		// 	);
		//
		// }

	}

	public Square getSquareAt(int r, int c) {
		// TODO: assert
		return squares[r][c];
	}

}
