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

	private final int id;

	private boolean dragging = false;
	private double prevSceneX = 0;
	private double prevSceneY = 0;
	private double dragDetectedOffsetX = 0;
	private double dragDetectedOffsetY = 0;

	public PieceImage(int id) {
		super();
		this.id = id;
		setWidth(40);
		setHeight(40);

		String url = App.class.getResource("images/white_knight.png").toString();
		log.info("ID={} url = " + url);
		Image image = new Image(url);

		setFill(new ImagePattern(image));

		// https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/input/MouseEvent.html
		// https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/input/MouseDragEvent.html

		setOnMouseDragged((MouseEvent event) -> {

			// log.info("ID={} onMouseDragged", id);

			// event.setDragDetect(true);

			// setViewOrder(-1.0);
			//
			// if (orgSceneX == -1) {
			// 	orgSceneX = event.getSceneX();
			// 	orgSceneY = event.getSceneY();
			// }
			//

			if (dragging) {

				double offsetX = event.getSceneX() - prevSceneX;
				double offsetY = event.getSceneY() - prevSceneY;

				// log.info(MessageFormat.format("offsetX = {0}, offsetY = {1}", offsetX, offsetY));

				setTranslateX(getTranslateX() + offsetX);
				setTranslateY(getTranslateY() + offsetY);

				prevSceneX = event.getSceneX();
				prevSceneY = event.getSceneY();

			}

		});

		setOnDragDetected((MouseEvent event) -> {
			log.info("ID={} setOnDragDetected", id);
			startFullDrag();
			prevSceneX = event.getSceneX();
			prevSceneY = event.getSceneY();
			dragDetectedOffsetX = event.getX();
			dragDetectedOffsetY = event.getY();
			dragging = true;
			getParent().setViewOrder(-1);
		});

		setOnMouseDragOver((MouseDragEvent event) -> {
			// log.info("ID={} onMouseDragOver", id);
		});

		setOnMouseDragReleased((MouseDragEvent event) -> {
			log.info("ID={} setOnMouseDragReleased", id);
			dragging = false;
			getParent().setViewOrder(0);
			setTranslateX(0);
			setTranslateY(0);
		});

		setOnMouseDragEntered((MouseDragEvent event) -> {
			log.info("ID={} onMouseDragEntered", id);
		});

		setOnMouseDragExited((MouseDragEvent event) -> {
			log.info("ID={} onMouseDragExited", id);
		});

		setOnMousePressed((MouseEvent event) -> {
			log.info("ID={} onMousePressed", id);
			// setMouseTransparent(true);
		});

		setOnMouseReleased((MouseEvent event) -> {
			log.info("ID={} onMouseReleased", id);
			// setMouseTransparent(false);
			if (dragging) {
				getParent().setViewOrder(0);
				setTranslateX(0);
				setTranslateY(0);
			}
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
		// log.info(MessageFormat.format("width = {0}, height = {1}", width, height));
		setWidth(width);
		setHeight(height);
	}

}
