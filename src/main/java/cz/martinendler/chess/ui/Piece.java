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

import java.util.EnumMap;

/**
 * A chess piece that can be placed on a {@link Square}
 */
public class Piece extends ResponsiveRectangle {

	private static final Logger log = LoggerFactory.getLogger(Piece.class);

	private final cz.martinendler.chess.engine.pieces.Piece piece;

	private boolean dragging = false;
	private double prevSceneX = 0;
	private double prevSceneY = 0;

	private static final EnumMap<cz.martinendler.chess.engine.pieces.Piece, ImagePattern> pieceToFill
		= new EnumMap<>(cz.martinendler.chess.engine.pieces.Piece.class);

	static {

		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.WHITE_PAWN,
			new ImagePattern(new Image(App.class.getResource("images/white_pawn.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.WHITE_KNIGHT,
			new ImagePattern(new Image(App.class.getResource("images/white_knight.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.WHITE_BISHOP,
			new ImagePattern(new Image(App.class.getResource("images/white_bishop.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.WHITE_ROOK,
			new ImagePattern(new Image(App.class.getResource("images/white_rook.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.WHITE_QUEEN,
			new ImagePattern(new Image(App.class.getResource("images/white_queen.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.WHITE_KING,
			new ImagePattern(new Image(App.class.getResource("images/white_king.png").toString()))
		);

		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.BLACK_PAWN,
			new ImagePattern(new Image(App.class.getResource("images/black_pawn.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.BLACK_KNIGHT,
			new ImagePattern(new Image(App.class.getResource("images/black_knight.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.BLACK_BISHOP,
			new ImagePattern(new Image(App.class.getResource("images/black_bishop.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.BLACK_ROOK,
			new ImagePattern(new Image(App.class.getResource("images/black_rook.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.BLACK_QUEEN,
			new ImagePattern(new Image(App.class.getResource("images/black_queen.png").toString()))
		);
		pieceToFill.put(
			cz.martinendler.chess.engine.pieces.Piece.BLACK_KING,
			new ImagePattern(new Image(App.class.getResource("images/black_king.png").toString()))
		);

	}

	public Piece(cz.martinendler.chess.engine.pieces.Piece piece) {
		super();

		this.piece = piece;
		setWidth(40);
		setHeight(40);

		getStyleClass().add("piece");

		setFill(pieceToFill.get(piece));

		// https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/input/MouseEvent.html
		// https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/input/MouseDragEvent.html

		// setOnMouseClicked((MouseEvent event) -> {
		// 	log.info("{} onMouseClicked: source = {}, target = {}", piece, event.getSource(), event.getTarget());
		// 	updateMove();
		// 	// prevent propagation in bubble phase
		// 	event.consume();
		// });

		setOnMouseDragged((MouseEvent event) -> {

			// log.info("{} onMouseDragged", piece);

			// event.setDragDetect(true); // works well by default

			if (dragging) {

				double changeX = event.getSceneX() - prevSceneX;
				double changeY = event.getSceneY() - prevSceneY;

				setTranslateX(getTranslateX() + changeX);
				setTranslateY(getTranslateY() + changeY);

				prevSceneX = event.getSceneX();
				prevSceneY = event.getSceneY();

			}

			// prevent propagation in bubble phase
			event.consume();

		});

		setOnDragDetected((MouseEvent event) -> {

			log.info("{} onDragDetected", piece);

			startFullDrag();

			// here are different grab behaviors:
			// TODO: choose what feels the most user-friendly

			// 1) cursor is at the grab point
			// prevSceneX = event.getSceneX();
			// prevSceneY = event.getSceneY();

			// 2) align the node so that the cursor is always at its top-left corner
			//   (no matter where the grab point was)
			// prevSceneX = event.getSceneX() - event.getX();
			// prevSceneY = event.getSceneY() - event.getY();

			// 3) align the node so that the cursor is always at its middle
			//    (no matter where the grab point was)
			prevSceneX = event.getSceneX() - event.getX() + getWidth() / 2;
			prevSceneY = event.getSceneY() - event.getY() + getHeight() / 2;

			startDragging();

			// prevent propagation in bubble phase
			event.consume();

		});

		setOnMousePressed((MouseEvent event) -> {

			log.info("{} onMousePressed", piece);

			// prevent propagation in bubble phase
			event.consume();

		});

		setOnMouseReleased((MouseEvent event) -> {

			log.info("{} onMouseReleased", piece);

			stopDragging();

			// prevent propagation in bubble phase
			event.consume();

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
			log.info("{} stopDragging", piece);
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

		square.setMoveOrigin(square);

	}

	public cz.martinendler.chess.engine.pieces.Piece getPiece() {
		return piece;
	}

}
