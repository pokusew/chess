package cz.martinendler.chess.ui;

import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A square on the chessboard {@link Board}
 */
public class Square extends StackPane {

	private static final Logger log = LoggerFactory.getLogger(Square.class);

	public final int row;
	public final int column;
	public final boolean light;

	// protected Piece piece;

	public Square(int row, int column, boolean light) {
		super();

		// TODO: better approach
		setWidth(40);
		setHeight(40);

		this.row = row;
		this.column = column;
		this.light = light;

		getStyleClass().add("square");

		if (light) {
			getStyleClass().add("square--light");
		} else {
			getStyleClass().add("square--dark");
		}

		setOnMouseClicked((MouseEvent event) -> {
			log.info("r={} c={} clicked", row, column);
		});

		setOnMouseDragEntered((MouseDragEvent event) -> {
			log.info("r={} c={} onMouseDragEntered", row, column);
			getStyleClass().add("square--target");
		});

		setOnMouseDragExited((MouseDragEvent event) -> {
			log.info("r={} c={} onMouseDragExited", row, column);
			getStyleClass().remove("square--target");
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
