package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.ui.MoveLogEntry;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the right view of the game view
 */
public class RightViewController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(RightViewController.class);

	@FXML
	private Text messageBubbleText;

	@FXML
	private ScrollPane moveLog;

	@FXML
	private VBox moveLogContent;

	public RightViewController() {
		super();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		log.info("initialize");

		// moveLog.setOnMouseClicked((MouseEvent event) -> {
		// 	log.info("onMouseClicked: target " + event.getTarget().toString());
		// });

	}

	/**
	 * Sets message text that is shown in the bubble
	 *
	 * @param value message text
	 */
	public void setMessageBubbleText(@Nullable String value) {
		messageBubbleText.setText(value);
	}

	/**
	 * Updates the move log with the {@link cz.martinendler.chess.engine.Game} move log
	 *
	 * @param gameMoveLog the game move log
	 */
	public void updateMoveLog(List<cz.martinendler.chess.engine.move.MoveLogEntry> gameMoveLog) {

		// TODO: get rid of this horrible thing
		moveLogContent.getChildren().remove(0, moveLogContent.getChildren().size());

		for (int i = 0, fullMoveCounter = 1; i < gameMoveLog.size(); i += 2, fullMoveCounter++) {

			cz.martinendler.chess.engine.move.MoveLogEntry whiteEntry = gameMoveLog.get(i);
			cz.martinendler.chess.engine.move.MoveLogEntry blackEntry = (i + 1 < gameMoveLog.size())
				? gameMoveLog.get(i + 1)
				: null;

			moveLogContent.getChildren().add(new MoveLogEntry(
				fullMoveCounter, whiteEntry.getSan(), blackEntry != null ? blackEntry.getSan() : null
			));

		}

	}

}
