package cz.martinendler.chess.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * A border segment (with a text description) of the chessboard {@link Board}
 */
public class BorderSegment extends VBox {

	protected final static Background COLOR_BROWN_LIGHT = new Background(
		new BackgroundFill(Color.web("7E4D38"), null, null)
	);

	protected final static Background COLOR_BROWN_DARK = new Background(
		new BackgroundFill(Color.web("803600"), null, null)
	);

	public BorderSegment(boolean light) {
		super();

		setAlignment(Pos.CENTER);

		if (light) {
			setBackground(COLOR_BROWN_LIGHT);
		} else {
			setBackground(COLOR_BROWN_DARK);
		}

	}

}
