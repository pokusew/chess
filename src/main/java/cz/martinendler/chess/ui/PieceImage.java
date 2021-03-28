package cz.martinendler.chess.ui;

import cz.martinendler.chess.App;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;


public class PieceImage extends Rectangle {

	public static final Logger log = LoggerFactory.getLogger(PieceImage.class);

	public PieceImage() {
		super();
		setWidth(40);
		setHeight(40);

		String url = App.class.getResource("images/white_knight.png").toString();
		log.info("url = " + url);
		Image image = new Image(url);


		setFill(new ImagePattern(image));

		setOnDragDetected((MouseEvent event) -> {

			Dragboard db = startDragAndDrop(TransferMode.MOVE);

			WritableImage im = new WritableImage((int) getWidth(), (int) getHeight());
			SnapshotParameters sp = new SnapshotParameters();
			sp.setFill(Color.TRANSPARENT);
			snapshot(sp, im);


			db.setDragView(
				// TODO: When Image is used (even when smooth is set to true)
				//       image is visibly coarse (toothy edges, rough edges).
				//       BUT when snapshot is used, it seems to be okay.
				//       tested on macOS 10.14.6:
				//         java.version = 15.0.2, javafx.version = 15.0.1)
				//         openjdk 15.0.2 2021-01-19
				//         OpenJDK Runtime Environment AdoptOpenJDK (build 15.0.2+7)
				//         OpenJDK 64-Bit Server VM AdoptOpenJDK (build 15.0.2+7, mixed mode, sharing)
				im,
				// new Image(url, getWidth(), getHeight(), false, true, false),
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



	@Override
	public double minWidth(double height) {
		return 0.0;
	}

	@Override
	public double minHeight(double width) {
		return 0.0;
	}

	@Override
	public double maxWidth(double height) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double maxHeight(double width) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public void resize(double width, double height) {
		log.info(MessageFormat.format("width = {0}, height = {1}", width, height));
		setWidth(width);
		setHeight(height);
	}

}
