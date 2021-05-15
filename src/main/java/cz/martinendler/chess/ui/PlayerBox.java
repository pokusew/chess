package cz.martinendler.chess.ui;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 */
public class PlayerBox extends HBox {

	private static final Logger log = LoggerFactory.getLogger(PlayerBox.class);

	public PlayerBox() {
		super();

		getStyleClass().add("player-box");

		Text playerNameNode = new Text("BLACK player");

		playerNameNode.getStyleClass().add("player-box-text");

		getChildren().add(playerNameNode);

	}

}
