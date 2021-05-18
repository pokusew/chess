package cz.martinendler.chess.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SideInfoBoxController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(SideInfoBoxController.class);

	@FXML
	private HBox sideBox;

	@FXML
	private Text sideName;

	@FXML
	private Text sideTime;

	public SideInfoBoxController() {
		super();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info("initialize");
	}

	public Text getSideName() {
		return sideName;
	}

	public Text getSideTime() {
		return sideTime;
	}

	public void setActive(boolean active) {
		if (!active) {
			sideBox.getStyleClass().remove("side-info-box--active");
		} else {
			sideBox.getStyleClass().add("side-info-box--active");
		}
	}

}
