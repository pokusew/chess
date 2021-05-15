package cz.martinendler.chess.ui;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 */
public class TimeBox extends HBox {

	private static final Logger log = LoggerFactory.getLogger(TimeBox.class);

	public TimeBox() {
		super();

		getStyleClass().add("time-box");

		setPrefWidth(100.0);
		setPrefHeight(40.0);

		Text timeNode = new Text("00:00");

		getChildren().add(timeNode);

	}

}
