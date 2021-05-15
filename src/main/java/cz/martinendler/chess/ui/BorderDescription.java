package cz.martinendler.chess.ui;

import javafx.scene.text.Text;

/**
 * A border segment of the chessboard {@link Board}
 */
public class BorderDescription extends BorderSegment {

	public BorderDescription(boolean light, String text) {
		super(light);

		getStyleClass().add("border-description");

		Text textNode = new Text(text);

		getChildren().add(textNode);

	}

}
