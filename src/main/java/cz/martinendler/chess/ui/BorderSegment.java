package cz.martinendler.chess.ui;

import javafx.scene.layout.VBox;

/**
 * A border segment (with a text description) of the chessboard {@link Board}
 */
public class BorderSegment extends VBox {

	public BorderSegment(boolean light) {
		super();

		getStyleClass().add("border-segment");

		if (light) {
			getStyleClass().add("border-segment--light");
		} else {
			getStyleClass().add("border-segment--dark");
		}

	}

}
