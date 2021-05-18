package cz.martinendler.chess.ui.controllers;


import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.ui.GameOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class GameDialogController {

	private static final Logger log = LoggerFactory.getLogger(GameDialogController.class);

	@FXML
	private TextField fenField;
	@FXML
	private ComboBox<GameOptions.GameType> typeField;
	@FXML
	private ComboBox<Side> humanSideField;

	private Stage dialogStage;
	private GameOptions options;
	private boolean successful = false;

	@FXML
	private void initialize() {

		ObservableList<GameOptions.GameType> types = FXCollections.observableList(new ArrayList<>());
		types.addAll(GameOptions.GameType.values());

		ObservableList<Side> sides = FXCollections.observableList(new ArrayList<>());
		sides.addAll(Side.values());

		typeField.setItems(types);
		humanSideField.setItems(sides);

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setOptions(GameOptions options) {

		this.options = options;

		fenField.setText(options.getStartingFen());
		typeField.setValue(options.getType());
		humanSideField.setValue(options.getHumanSide());

	}

	public boolean isSuccessful() {
		return successful;
	}

	@FXML
	private void handleSubmit() {

		if (options == null) {
			return;
		}

		if (!validateInput()) {
			return;
		}

		options.setStartingFen(fenField.getText());
		options.setType(typeField.getValue());
		options.setHumanSide(humanSideField.getValue());

		successful = true;
		dialogStage.close();

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean validateInput() {

		return true;

	}

}
