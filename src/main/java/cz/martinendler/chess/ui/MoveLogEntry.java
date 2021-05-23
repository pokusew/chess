package cz.martinendler.chess.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A move log entry
 */
public class MoveLogEntry extends HBox {

	private final @NotNull Box whiteMoveBox;
	private final @Nullable Box blackMoveBox;

	public MoveLogEntry(
		int fullMoveCounter,
		int whiteMoveIndex,
		int blackMoveIndex,
		@Nullable String whiteMove,
		@Nullable String blackMove,
		@NotNull SimpleIntegerProperty activeMoveIndex
	) {
		super();

		getStyleClass().add("move-log-entry");

		if (fullMoveCounter % 2 == 0) {
			getStyleClass().add("move-log-entry--even");
		}

		HBox counterBox = new HBox(new Text(fullMoveCounter + "."));
		counterBox.getStyleClass().add("counter");
		getChildren().add(counterBox);

		whiteMoveBox = new Box(
			whiteMoveIndex,
			activeMoveIndex,
			"white-move",
			whiteMove
		);
		getChildren().add(whiteMoveBox);

		if (blackMove != null) {
			blackMoveBox = new Box(
				blackMoveIndex,
				activeMoveIndex,
				"black-move",
				blackMove
			);
			getChildren().add(blackMoveBox);
		} else {
			blackMoveBox = null;
		}

	}

	public void deregister() {
		whiteMoveBox.deregister();
		if (blackMoveBox != null) {
			blackMoveBox.deregister();
		}
	}

	private static class Box extends HBox {

		private static final Logger log = LoggerFactory.getLogger(Box.class);

		private final int moveIndex;
		private final @NotNull SimpleIntegerProperty activeMoveIndex;
		private final @NotNull ChangeListener<Number> activeMoveIndexListener;

		private Box(
			int moveIndex,
			@NotNull SimpleIntegerProperty activeMoveIndex,
			@NotNull String className,
			@Nullable String text
		) {

			super();

			this.moveIndex = moveIndex;
			this.activeMoveIndex = activeMoveIndex;

			HBox innerBox = new HBox(new Text(text));
			innerBox.getStyleClass().add("inner-box");
			innerBox.setOnMouseClicked(event -> {
				this.activeMoveIndex.set(this.moveIndex);
			});

			activeMoveIndexListener = (observable, oldValue, newValue) -> {

				if (oldValue.intValue() == this.moveIndex) {
					innerBox.getStyleClass().remove("inner-box--active");
				}

				if (newValue.intValue() == this.moveIndex) {
					innerBox.getStyleClass().add("inner-box--active");
				}

			};

			// initial value
			if (this.moveIndex == this.activeMoveIndex.get()) {
				innerBox.getStyleClass().add("inner-box--active");
			}

			register();

			getStyleClass().addAll("outer-box", className);
			getChildren().add(innerBox);

		}

		private void register() {
			activeMoveIndex.addListener(activeMoveIndexListener);
		}

		private void deregister() {
			activeMoveIndex.removeListener(activeMoveIndexListener);
		}

	}

}
