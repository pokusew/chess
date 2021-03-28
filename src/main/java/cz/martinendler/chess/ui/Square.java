package cz.martinendler.chess.ui;

import cz.martinendler.chess.App;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
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

		setOnDragDetected((MouseEvent event) -> {

			Dragboard db = startDragAndDrop(TransferMode.MOVE);

			db.setDragView(
				new Image(url, getWidth(), getHeight(), false, true, false),
				// event coordinate system:
				//
				//    (0,0) -----> + event.getX()
				//    |
				//    |
				//    V
				//    +
				//    event.getY()
				//
				// offset coordinate system:
				//
				//                       offsetY--
				//    ++offsetX (drag view image center = (0,0)) offsetX--
				//                       offsetY++
				//
				(getWidth() / 2) - event.getX(), -(getHeight() / 2) + event.getY()
			);

			log.info(MessageFormat.format(
				"setOnDragDetected {0} {1} {2} {3}",
				event.getX(), event.getY(),
				getWidth(), getHeight()
			));

			ClipboardContent content = new ClipboardContent();
			content.putString("source text");
			db.setContent(content);

		});

		setOnMouseDragged((MouseEvent event) -> {
			// event.setDragDetect(true);
		});

		setOnDragOver((DragEvent event) -> {

			if (event.getGestureSource() != this && event.getDragboard().hasString()) {
				event.acceptTransferModes(TransferMode.MOVE);
			}

			event.consume();

		});

		setOnDragDropped((DragEvent event) -> {
			Dragboard db = event.getDragboard();
			if (db.hasString()) {
				log.info("Dropped: " + db.getString());
				event.setDropCompleted(true);
			} else {
				event.setDropCompleted(false);
			}
			event.consume();
		});


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
