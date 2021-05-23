package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.ui.GameOptions;
import cz.martinendler.chess.utils.DialogUtils;
import cz.martinendler.chess.utils.FormUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
	@FXML
	protected TextField whiteTimeLimitField;
	@FXML
	protected TextField blackTimeLimitField;

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
		whiteTimeLimitField.setTextFormatter(new TextFormatter<Integer>(FormUtils.LONG_FILTER));
		blackTimeLimitField.setTextFormatter(new TextFormatter<Integer>(FormUtils.LONG_FILTER));

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

		StringBuilder sb = new StringBuilder();

		Long whiteTimeLimit = FormUtils.LONG_CONVERTER.fromString(whiteTimeLimitField.getText());
		Long blackTimeLimit = FormUtils.LONG_CONVERTER.fromString(blackTimeLimitField.getText());

		if (!FormUtils.isValidTimeLimit(whiteTimeLimit)) {
			sb.append("- white time must be a number in range [0, 99]\n");
		}

		if (!FormUtils.isValidTimeLimit(blackTimeLimit)) {
			sb.append("- black time must be a number in range [0, 99]\n");
		}

		return DialogUtils.showInputErrors(sb, dialogStage);

	}

	public void setOptions(@Nullable GameOptions options) {

		this.options = options;

		if (options != null) {
			typeField.setValue(options.getType());
			humanSideField.setValue(options.getHumanSide());
			whiteTimeLimitField.setText(FormUtils.LONG_CONVERTER.toString(options.getWhiteTimeLimitInMinutes()));
			blackTimeLimitField.setText(FormUtils.LONG_CONVERTER.toString(options.getBlackTimeLimitInMinutes()));
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
		options.setWhiteTimeLimitFromMinutes(FormUtils.LONG_CONVERTER.fromString(whiteTimeLimitField.getText()));
		options.setBlackTimeLimitFromMinutes(FormUtils.LONG_CONVERTER.fromString(blackTimeLimitField.getText()));

	}

}
