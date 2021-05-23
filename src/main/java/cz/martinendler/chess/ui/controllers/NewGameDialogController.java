package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.ui.GameOptions;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewGameDialogController extends GameDialogController {

	private static final Logger log = LoggerFactory.getLogger(NewGameDialogController.class);

	@FXML
	protected TextField fenField;

	@Override
	public void setOptions(GameOptions options) {

		super.setOptions(options);

		fenField.setText(options.getStartingFen());

	}

	@Override
	protected void setOptionsFromFieldValues() {

		super.setOptionsFromFieldValues();

		if (options == null) {
			return;
		}

		options.setStartingFen(fenField.getText());

	}

}
