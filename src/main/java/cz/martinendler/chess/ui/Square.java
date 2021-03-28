package cz.martinendler.chess.ui;

import cz.martinendler.chess.engine.PlayerType;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Square extends StackPane {

	public static final Logger log = LoggerFactory.getLogger(Square.class);

	private final static Background COLOR_WHITE = new Background(
		new BackgroundFill(Color.WHITESMOKE, null, null)
	);

	private final static Background COLOR_BLACK = new Background(
		new BackgroundFill(Color.BLACK, null, null)
	);

	private final static Background COLOR_TARGET = new Background(
		new BackgroundFill(Color.LIGHTYELLOW, null, null)
	);

	public final int row;
	public final int column;
	public final PlayerType color;

	private final Background normalBg;

	// protected Piece piece;

	public Square(int row, int column, PlayerType color) {
		super();

		// TODO: better approach
		setWidth(40);
		setHeight(40);

		this.row = row;
		this.column = column;
		this.color = color;

		normalBg = color == PlayerType.WHITE ? COLOR_WHITE : COLOR_BLACK;

		setBackground(normalBg);

		setOnMouseClicked((MouseEvent event) -> {
			log.info("r={} c={} clicked", row, column);
		});

		setOnMouseDragEntered((MouseDragEvent event) -> {
			log.info("r={} c={} onMouseDragEntered", row, column);
			setBackground(COLOR_TARGET);
		});

		setOnMouseDragExited((MouseDragEvent event) -> {
			log.info("r={} c={} onMouseDragExited", row, column);
			setBackground(normalBg);
		});

		setOnMouseDragReleased((MouseDragEvent event) -> {

			log.info("r={} c={} setOnMouseDragReleased", row, column);

			if (event.getGestureSource() instanceof Piece) {
				Piece src = (Piece) event.getGestureSource();
				// TODO: rewrite
				if (getChildren().contains(src)) {
					return;
				}
				// if (src == piece) {
				// 	return;
				// }
				log.info("r={} c={} setOnMouseDragReleased {}-->", row, column, src.id);
				src.stopDragging();
				// srcSquare = src.getCurrentSquare()
				// setPiece(src);
				getChildren().setAll(src);
			}

		});

	}

	// public boolean hasPiece() {
	// 	return piece != null;
	// }
	//
	// public Piece getPiece() {
	// 	return piece;
	// }

	// TODO: try setPiece(null)
	// public void setPiece(Piece newPiece) {
	//
	// 	if (hasPiece()) {
	// 		// TODO: un-connect
	// 		// piece.
	// 	}
	//
	// 	piece = newPiece;
	// 	getChildren().setAll(piece);
	//
	// }

	// @Override
	// public boolean isResizable() {
	// 	return true;
	// }
	//
	// @Override
	// public void resize(double width, double height) {
	// 	setWidth(width);
	// 	setHeight(height);
	// }

}
