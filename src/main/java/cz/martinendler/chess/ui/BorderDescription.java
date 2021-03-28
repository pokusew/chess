package cz.martinendler.chess.ui;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BorderDescription extends BorderSegment {

	public BorderDescription(boolean light, String text) {
		super(light);

		Text textNode = new Text(text);
		textNode.setFill(Color.WHITE);
		// TODO: system font does not support FontWeight.BOLD (at least on macOS)?
		textNode.setFont(Font.font("Arial", FontWeight.BOLD, Font.getDefault().getSize()));
		textNode.setFontSmoothingType(FontSmoothingType.LCD);

		getChildren().add(textNode);

	}

}
