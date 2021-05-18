package cz.martinendler.chess.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the side info component
 * Used by {@link GameController}
 */
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

	public @Nullable String getSideName() {
		return sideName.getText();
	}

	public void setSideName(@Nullable String sideName) {
		this.sideName.setText(sideName);
	}

	public @Nullable String getSideTime() {
		return sideTime.getText();
	}

	public void setSideTime(@Nullable String sideTime) {
		this.sideTime.setText(sideTime);
	}

	/**
	 * Sets if this side info is active (the corresponding side/player is on move)
	 *
	 * @param active if the side is the side to move
	 */
	public void setActive(boolean active) {
		if (!active) {
			sideBox.getStyleClass().remove("side-info-box--active");
		} else {
			sideBox.getStyleClass().add("side-info-box--active");
		}
	}

}
