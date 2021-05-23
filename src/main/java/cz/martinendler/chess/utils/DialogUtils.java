package cz.martinendler.chess.utils;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Utils for JavaFX dialogs
 */
public class DialogUtils {

	public static boolean showInputErrors(StringBuilder sb, Stage stage) {

		String errorMessages = sb.toString();

		if (errorMessages.isEmpty()) {
			return true;
		}

		// show error messages
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(stage);
		alert.setTitle("Invalid input");
		alert.setHeaderText("Please correct invalid fields");
		alert.setContentText(errorMessages);

		alert.showAndWait();

		return false;

	}

}
