package cz.martinendler.chess.ui.controllers;

import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SideInfoBoxController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(SideInfoBoxController.class);

	// @FXML
	// private Board board;

	public SideInfoBoxController() {
		super();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		log.info("initialize");

	}

}
