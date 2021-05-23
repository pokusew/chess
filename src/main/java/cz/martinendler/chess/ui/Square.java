package cz.martinendler.chess.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A square on the chessboard {@link Board}
 */
public class Square extends StackPane {

	private static final Logger log = LoggerFactory.getLogger(Square.class);

	private final cz.martinendler.chess.engine.board.Square square;

	private final @NotNull Circle moveHint;
	private final @NotNull Region captureHint;
	private final @NotNull Region dragReleaseHint;

	private final @NotNull ChangeListener<Square> moveOriginListener;
	private final @NotNull ChangeListener<Number> legalMovesListener;
	private final @NotNull ChangeListener<Number> highlightsListener;

	private @Nullable Board parentBoard;

	private boolean isMoveOrigin;
	private boolean isHighlighted;

	public Square(cz.martinendler.chess.engine.board.Square square) {
		super();

		// TODO: cleaner/correct solution (without these initial sizes it does not work)
		setWidth(40);
		setHeight(40);

		this.square = square;
		this.parentBoard = null;

		this.moveOriginListener = (observable, oldValue, newValue) -> {

			if (oldValue == this) {
				log.info("{} is no longer move origin", this);
				isMoveOrigin = false;
				getStyleClass().remove("square--origin");
			}

			if (newValue == this) {
				log.info("{} is move origin", this);
				isMoveOrigin = true;
				getStyleClass().addAll("square--origin");
			}

		};

		this.legalMovesListener = (observable, oldValue, newValue) -> {

			if ((oldValue.longValue() & square.getBitboard()) != 0L) {
				log.info("{} removing hint", this);
				getStyleClass().remove("square--move-hint");
				getStyleClass().remove("square--capture-hint");
			}

			if ((newValue.longValue() & square.getBitboard()) != 0L) {
				log.info("{} adding hint", this);
				if (getPiece() != null) {
					getStyleClass().add("square--capture-hint");
				} else {
					getStyleClass().add("square--move-hint");
				}
			}

		};

		this.highlightsListener = (observable, oldValue, newValue) -> {

			if ((oldValue.longValue() & square.getBitboard()) != 0L) {
				log.info("{} removing highlight", this);
				isHighlighted = false;
				getStyleClass().remove("square--highlight");
			}

			if ((newValue.longValue() & square.getBitboard()) != 0L) {
				log.info("{} adding highlight", this);
				isHighlighted = true;
				getStyleClass().add("square--highlight");
			}

		};

		getStyleClass().add("square");

		if (square.isLightSquare()) {
			getStyleClass().add("square--light");
		} else {
			getStyleClass().add("square--dark");
		}

		// move hint
		moveHint = new Circle();
		moveHint.getStyleClass().add("move-hint");
		moveHint.setMouseTransparent(true);
		getChildren().add(moveHint);

		// capture hint
		captureHint = new Region();
		captureHint.getStyleClass().add("capture-hint");
		captureHint.setMouseTransparent(true);
		getChildren().add(captureHint);

		// capture hint
		dragReleaseHint = new Region();
		dragReleaseHint.getStyleClass().add("drag-release-hint");
		dragReleaseHint.setMouseTransparent(true);
		getChildren().add(dragReleaseHint);

		setOnMouseClicked((MouseEvent event) -> {
			log.info("{} clicked", this);
			event.consume();
			updateMove();
		});

		setOnMouseDragEntered((MouseDragEvent event) -> {
			// log.info("{} onMouseDragEntered", this);
			getStyleClass().add("square--target");
		});

		setOnMouseDragExited((MouseDragEvent event) -> {
			// log.info("{} onMouseDragExited", this);
			getStyleClass().remove("square--target");
		});

		setOnMouseDragReleased((MouseDragEvent event) -> {

			if (event.getGestureSource() instanceof Piece) {

				Piece movingPiece = (Piece) event.getGestureSource();

				if (movingPiece.getParentSquare() == this) {
					// no position change
					log.info("{} onMouseDragReleased: no position change", this);
					return;
				}

				log.info("{} onMouseDragReleased: piece {} released here", this, movingPiece.getPiece());
				movingPiece.stopDragging();

				// // TODO: this is temp solution
				// getChildren().removeIf((Node node) -> node instanceof Piece);
				// getChildren().add(movingPiece);

				updateMove();

			} else {

				log.info(
					"{} onMouseDragReleased: unknown gesture source {}",
					this, event.getGestureSource()
				);

			}

		});

		parentProperty().addListener((ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) -> {

			log.info("{} parent changed from {} to {}", this, oldValue, newValue);

			registerToBoard(newValue);

		});

	}

	private void unregisterFromBoard() {

		if (parentBoard == null) {
			return;
		}

		// unregister
		parentBoard.moveOriginProperty().removeListener(moveOriginListener);
		parentBoard.legalMovesProperty().removeListener(legalMovesListener);
		parentBoard.highlightsProperty().removeListener(highlightsListener);
		// unset reference
		parentBoard = null;

	}

	private void registerToBoard(Parent newParent) {

		unregisterFromBoard();

		if (!(newParent instanceof Board)) {
			// we cloud use lookup(".board") if Board was not the direct predecessor
			log.info("{} parent is not Board", this);
			return;
		}

		parentBoard = (Board) newParent;
		parentBoard.moveOriginProperty().addListener(moveOriginListener);
		parentBoard.legalMovesProperty().addListener(legalMovesListener);
		parentBoard.highlightsProperty().addListener(highlightsListener);

	}

	public @Nullable Board getParentBoard() {
		return parentBoard;
	}

	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		moveHint.setRadius(width / 6); // cca 1/3
		captureHint.resize(width, height);
		dragReleaseHint.resize(width, height);
	}

	public @Nullable Piece getPiece() {

		if (getChildren().size() <= 3) {
			return null;
		}

		int lastIndex = getChildren().size() - 1;

		Node lastChild = getChildren().get(lastIndex);

		if (!(lastChild instanceof Piece)) {
			return null;
		}

		return (Piece) lastChild;

	}

	/**
	 * Sets the new piece for this square
	 *
	 * @param newPiece if {@code null} is given
	 * @return old piece if there was any, {@code null} otherwise
	 */
	public @Nullable Piece setPiece(@Nullable Piece newPiece) {

		Piece oldPiece = getPiece();

		if (oldPiece != null) {
			int lastIndex = getChildren().size() - 1;
			getChildren().remove(lastIndex);
		}

		if (newPiece != null) {
			getChildren().add(newPiece);
		}

		return oldPiece;

	}

	public void setMoveOrigin(@Nullable Square square) {

		Board board = getParentBoard();

		if (board == null) {
			return;
		}

		board.setMoveOrigin(square);

	}

	public boolean isMoveOrigin() {
		return isMoveOrigin;
	}

	public boolean isHighlighted() {
		return isHighlighted;
	}

	public void updateMove() {

		Board board = getParentBoard();

		if (board == null) {
			return;
		}

		board.updateMove(this);

	}

	public cz.martinendler.chess.engine.board.Square getSquare() {
		return square;
	}

	@Override
	public String toString() {
		return square.getNotation();
	}

}
