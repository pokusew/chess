package cz.martinendler.chess.ui;

import cz.martinendler.chess.engine.Side;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 */
public class SideInfoBox extends HBox {

	private static final Logger log = LoggerFactory.getLogger(SideInfoBox.class);

	private final PlayerBox playerBox;
	private final TimeBox timeBox;

	public SideInfoBox(Side side) {
		super();

		getStyleClass().add("side-info-box");

		if (side.isWhite()) {
			getStyleClass().add("side-info-box--white");
		} else {
			getStyleClass().add("side-info-box--black");
		}

		playerBox = new PlayerBox();
		timeBox = new TimeBox();

		getChildren().addAll(playerBox, timeBox);

	}

	public PlayerBox getPlayerBox() {
		return playerBox;
	}

	public TimeBox getTimeBox() {
		return timeBox;
	}

}
