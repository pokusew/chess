package cz.martinendler.chess.ui;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Nullable;

/**
 * A move log entry
 */
public class MoveLogEntry extends HBox {

	public MoveLogEntry(int fullMoveCounter, @Nullable String whiteMove, @Nullable String blackMove) {
		super();

		getStyleClass().add("move-log-entry");

		if (fullMoveCounter % 2 == 0) {
			getStyleClass().add("move-log-entry--even");
		}

		HBox counterBox = new HBox(new Text(fullMoveCounter + "."));
		HBox whiteMoveBox = new HBox(new Text(whiteMove));
		HBox blackMoveBox = new HBox(new Text(blackMove));

		counterBox.getStyleClass().add("counter");
		whiteMoveBox.getStyleClass().add("white-move");
		blackMoveBox.getStyleClass().add("black-move");

		getChildren().addAll(counterBox, whiteMoveBox, blackMoveBox);

	}

}
