package cz.martinendler.chess.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

	public final int row;
	public final int column;
	public final boolean light;

	private @Nullable Board parentBoard;

	private final @NotNull ChangeListener<Square> moveOriginListener;

	private final @NotNull Circle moveHint;
	private final @NotNull Region captureHint;
	private final @NotNull Region dragReleaseHint;

	// protected Piece piece;

	public Square(int row, int column, boolean light) {
		super();

		// TODO: cleaner/correct solution (without these initial sizes it does not work)
		setWidth(40);
		setHeight(40);

		this.row = row;
		this.column = column;
		this.light = light;

		this.parentBoard = null;

		this.moveOriginListener = (observable, oldValue, newValue) -> {

			if (oldValue == this) {
				log.info("r={} c={} is no longer move origin", row, column);
				getStyleClass().remove("square--origin");
			}

			if (newValue == this) {
				log.info("r={} c={} is move origin", row, column);
				getStyleClass().addAll("square--origin");
			}

		};

		getStyleClass().add("square");

		if (light) {
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
			log.info("r={} c={} clicked", row, column);
		});

		setOnMouseDragEntered((MouseDragEvent event) -> {
			log.info("r={} c={} onMouseDragEntered", row, column);
			getStyleClass().add("square--target");
		});

		setOnMouseDragExited((MouseDragEvent event) -> {
			log.info("r={} c={} onMouseDragExited", row, column);
			getStyleClass().remove("square--target");
		});

		setOnMouseDragReleased((MouseDragEvent event) -> {

			log.info(
				"r={} c={} onMouseDragReleased:  source = {}, target = {}",
				row, column, event.getSource(), event.getTarget()
			);

			if (event.getGestureSource() instanceof Piece) {

				Piece movingPiece = (Piece) event.getGestureSource();

				if (movingPiece.getParentSquare() == this) {
					// no position change
					return;
				}

				log.info("r={} c={} onMouseDragReleased: piece {} released here", row, column, movingPiece.id);
				movingPiece.stopDragging();

				// TODO: this is temp solution
				getChildren().removeIf((Node node) -> node instanceof Piece);
				getChildren().add(movingPiece);

			}

		});

		parentProperty().addListener((ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) -> {

			log.info("r={} c={} parent changed from {} to {}", row, column, oldValue, newValue);

			registerToBoard(newValue);

		});

	}

	private void unregisterFromBoard() {

		if (parentBoard == null) {
			return;
		}

		// unregister
		parentBoard.moveOriginProperty().removeListener(moveOriginListener);
		// unset reference
		parentBoard = null;

	}

	private void registerToBoard(Parent newParent) {

		unregisterFromBoard();

		if (!(newParent instanceof Board)) {
			// we cloud use lookup(".board") if Board was not the direct predecessor
			log.info("r={} c={} parent is not Board", row, column);
			return;
		}

		parentBoard = (Board) newParent;
		parentBoard.moveOriginProperty().addListener(moveOriginListener);

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

	public @Nullable Piece removePiece() {

		if (getChildren().size() <= 3) {
			return null;
		}

		int lastIndex = getChildren().size() - 1;

		Node lastChild = getChildren().get(lastIndex);

		if (!(lastChild instanceof Piece)) {
			return null;
		}

		getChildren().remove(lastChild);
		return (Piece) lastChild;

	}

}
