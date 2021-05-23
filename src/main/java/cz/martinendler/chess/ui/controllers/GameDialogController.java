package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.ui.GameOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class GameDialogController {

	private static final Logger log = LoggerFactory.getLogger(GameDialogController.class);

	@FXML
	protected ComboBox<GameOptions.GameType> typeField;
	@FXML
	protected ComboBox<Side> humanSideField;

	protected @Nullable Stage dialogStage;
	protected @Nullable GameOptions options;
	protected boolean successful = false;

	@FXML
	protected void initialize() {

		ObservableList<GameOptions.GameType> types = FXCollections.observableList(new ArrayList<>());
		types.addAll(GameOptions.GameType.values());

		ObservableList<Side> sides = FXCollections.observableList(new ArrayList<>());
		sides.addAll(Side.values());

		typeField.setItems(types);
		humanSideField.setItems(sides);

	}

	public void setDialogStage(@Nullable Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public @Nullable Stage getDialogStage() {
		return dialogStage;
	}

	public boolean isSuccessful() {
		return successful;
	}

	@FXML
	protected void handleSubmit() {

		if (!validateInput()) {
			return;
		}

		setOptionsFromFieldValues();

		successful = true;
		if (dialogStage != null) {
			dialogStage.close();
		}

	}

	@FXML
	private void handleCancel() {
		if (dialogStage != null) {
			dialogStage.close();
		}
	}

	protected boolean validateInput() {
		return true;
	}

	public void setOptions(@Nullable GameOptions options) {

		this.options = options;

		if (options != null) {
			typeField.setValue(options.getType());
			humanSideField.setValue(options.getHumanSide());
		}

	}

	public @Nullable GameOptions getOptions() {
		return options;
	}

	protected void setOptionsFromFieldValues() {

		if (options == null) {
			return;
		}

		options.setType(typeField.getValue());
		options.setHumanSide(humanSideField.getValue());

	}

}
