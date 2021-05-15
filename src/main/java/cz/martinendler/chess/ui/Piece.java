package cz.martinendler.chess.ui;

import cz.martinendler.chess.App;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A chess piece that can be placed on a {@link Square}
 */
public class Piece extends ResponsiveRectangle {

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

		getStyleClass().add("piece");

		String url = App.class.getResource("images/white_knight.png").toString();
		log.info("ID={} url = " + url);
		Image image = new Image(url);

		setFill(new ImagePattern(image));

		// https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/input/MouseEvent.html
		// https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/input/MouseDragEvent.html

		setOnMouseClicked((MouseEvent event) -> {
			log.info("ID={} onMouseClicked: source = {}, target = {}", id, event.getSource(), event.getTarget());
		});

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

			log.info("ID={} onDragDetected", id);

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

			startDragging();

		});

		setOnMouseDragReleased((MouseDragEvent event) -> {

			log.info(
				"ID={} setOnMouseDragReleased:  source = {}, target = {}",
				id, event.getSource(), event.getTarget()
			);

		});

		setOnMousePressed((MouseEvent event) -> {

			log.info("ID={} onMousePressed", id);

		});

		setOnMouseReleased((MouseEvent event) -> {

			log.info("ID={} onMouseReleased", id);

			stopDragging();

		});

	}

	private void startDragging() {

		setSquareAsMoveOrigin();

		getParent().setViewOrder(-1);
		setMouseTransparent(true);
		getParent().setMouseTransparent(true);
		dragging = true;

	}

	public void stopDragging() {

		if (dragging) {
			log.info("ID={} stopDragging", id);
			dragging = false;
			getParent().setViewOrder(0);
			setMouseTransparent(false);
			getParent().setMouseTransparent(false);
			setTranslateX(0);
			setTranslateY(0);
		}

	}

	public @Nullable Square getParentSquare() {

		Parent parent = getParent();

		if (parent instanceof Square) {
			return (Square) parent;
		}

		return null;

	}

	public void setSquareAsMoveOrigin() {

		Square square = getParentSquare();

		if (square == null) {
			return;
		}

		Board board = square.getParentBoard();

		if (board == null) {
			return;
		}

		board.setMoveOrigin(square);

	}

}
