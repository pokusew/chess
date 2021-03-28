package cz.martinendler.chess.ui;

import cz.martinendler.chess.App;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class Square extends StackPane {

	public static final Logger log = LoggerFactory.getLogger(Square.class);

	public Square(boolean white) {
		setWidth(40);
		setHeight(40);

		String url = App.class.getResource("images/white_knight.png").toString();
		log.info("url = " + url);
		Image image = new Image(url);

		// getChildren().add(new PieceImage());

		setBackground(new Background(new BackgroundFill(white ? Color.WHITESMOKE : Color.BLACK, null, null)));

		setBackground(new Background(
			List.of(
				new BackgroundFill(white ? Color.WHITESMOKE : Color.BLACK, null, null)
			),
			List.of(
				new BackgroundImage(
					image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					null, new BackgroundSize(-1, -1, true, true, true, true)
				)
			)
		));


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
