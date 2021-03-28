package cz.martinendler.chess.ui;

import cz.martinendler.chess.App;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PieceImage extends ImageView {

	public static final Logger log = LoggerFactory.getLogger(PieceImage.class);

	public PieceImage() {
		super();

		String url = App.class.getResource("images/white_knight.png").toString();
		log.info("url = " + url);
		Image image = new Image(url);

		setImage(image);

		setFitHeight(50);
		setFitWidth(50);


		// //
		// //
		// // setFill(new ImagePattern(image));
		//
		// ImageView piece = new ImageView(image);
		// piece.setFitHeight(10);
		// piece.setFitWidth(10);
		// piece.setPreserveRatio(true);
		// piece.setPickOnBounds(true);
		//
		// getChildren().add(new ImageView(image));
		//
		// setBackground(new Background(new BackgroundFill(white ? Color.WHITESMOKE : Color.BLACK, null, null)));

	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public void resize(double width, double height) {
		log.info("resize width = " + width);
		setFitHeight(width);
		setFitWidth(height);
	}

}
