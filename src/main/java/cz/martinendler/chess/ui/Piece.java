package cz.martinendler.chess.ui;

import cz.martinendler.chess.App;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A chess piece that can be placed on a {@link Square}
 */
public class Piece extends Rectangle {

	private static final Logger log = LoggerFactory.getLogger(Piece.class);

	public final int id;

	private boolean dragging = false;
	private double prevSceneX = 0;
	private double prevSceneY = 0;

	public Piece(int id) {
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

			// event.setDragDetect(true); // works well by default

			if (dragging) {

				double changeX = event.getSceneX() - prevSceneX;
				double changeY = event.getSceneY() - prevSceneY;

				setTranslateX(getTranslateX() + changeX);
				setTranslateY(getTranslateY() + changeY);

				prevSceneX = event.getSceneX();
				prevSceneY = event.getSceneY();

			}

		});

		setOnDragDetected((MouseEvent event) -> {

			log.info("ID={} setOnDragDetected", id);

			startFullDrag();

			// here are different grab behaviors:
			// TODO: choose what feels the most user-friendly

			// 1) cursor is at the grab point
			prevSceneX = event.getSceneX();
			prevSceneY = event.getSceneY();

			// 2) align the node so that the cursor is always at its top-left corner
			//   (no matter where the grab point was)
			// prevSceneX = event.getSceneX() - event.getX();
			// prevSceneY = event.getSceneY() - event.getY();

			// 3) align the node so that the cursor is always at its middle
			//    (no matter where the grab point was)
			// prevSceneX = event.getSceneX() - event.getX() + getWidth() / 2;
			// prevSceneY = event.getSceneY() - event.getY() + getHeight() / 2;

			getParent().setViewOrder(-1);
			dragging = true;

		});

		setOnMousePressed((MouseEvent event) -> {

			log.info("ID={} onMousePressed", id);

			setMouseTransparent(true);
			getParent().setMouseTransparent(true);

		});

		setOnMouseReleased((MouseEvent event) -> {

			log.info("ID={} onMouseReleased", id);

			stopDragging();

		});

	}

	// TODO: better
	public void stopDragging() {

		log.info("ID={} stopDragging", id);

		setMouseTransparent(false);
		getParent().setMouseTransparent(false);

		if (dragging) {
			dragging = false;
			getParent().setViewOrder(0);
			setTranslateX(0);
			setTranslateY(0);
		}

	}

	public Square getCurrentSquare() {

		Parent parent = getParent();

		if (parent instanceof Square) {
			return (Square) parent;
		}

		return null;

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
