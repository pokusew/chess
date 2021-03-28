package cz.martinendler.chess.ui;

import cz.martinendler.chess.engine.PlayerType;
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

	public final int row;
	public final int column;
	public final PlayerType color;

	protected Piece piece;

	public Square(int row, int column, PlayerType color) {
		super();

		// TODO: better approach
		setWidth(40);
		setHeight(40);

		this.row = row;
		this.column = column;
		this.color = color;

		if (this.color == PlayerType.WHITE) {
			setBackground(COLOR_WHITE);
		} else {
			setBackground(COLOR_BLACK);
		}

		setOnMouseClicked((MouseEvent event) -> {
			log.info("r={} c={} clicked", row, column);
		});

	}

	public boolean hasPiece() {
		return piece != null;
	}

	public Piece getPiece() {
		return piece;
	}

	// TODO: try setPiece(null)
	public void setPiece(Piece newPiece) {

		if (hasPiece()) {
			// TODO: un-connect
			// piece.
		}

		piece = newPiece;
		getChildren().setAll(piece);

	}

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
