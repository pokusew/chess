package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.ui.MoveLogEntry;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

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

		moveLog.setOnMouseClicked((MouseEvent event) -> {
			log.info("onMouseClicked: target " + event.getTarget().toString());
		});

		moveLogContent.getChildren().add(new MoveLogEntry(1, "e4", "e6"));

	}

	public void setMessageBubbleText(String value) {
		messageBubbleText.setText(value);
	}

}
