package cz.martinendler.chess.utils;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;

/**
 * Utils for JavaFX forms
 */
public class FormUtils {

	public static final StringConverter<Integer> INT_CONVERTER = new StringConverter<>() {
		@Override
		public String toString(Integer number) {

			if (number == null) {
				return null;
			}

			return number.toString();
		}

		@Override
		public Integer fromString(String string) {
			try {
				return Integer.parseInt(string);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	};

	public static final UnaryOperator<TextFormatter.Change> INT_FILTER = change -> {

		if (change.getControlNewText().isEmpty()) {
			return change;
		}

		try {
			Integer.parseInt(change.getControlNewText());
			return change;
		} catch (NumberFormatException e) {
			return null;
		}

	};

	public static final StringConverter<Long> LONG_CONVERTER = new StringConverter<>() {
		@Override
		public String toString(Long number) {

			if (number == null) {
				return null;
			}

			return number.toString();
		}

		@Override
		public Long fromString(String string) {
			try {
				return Long.parseLong(string);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	};

	public static final UnaryOperator<TextFormatter.Change> LONG_FILTER = change -> {

		if (change.getControlNewText().isEmpty()) {
			return change;
		}

		try {
			Long.parseLong(change.getControlNewText());
			return change;
		} catch (NumberFormatException e) {
			return null;
		}

	};

	public static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	public static boolean isGreaterThan(Integer value, int minExcl) {
		return value != null && value > minExcl;
	}

	public static boolean isNotGreaterThan(Integer value, int minExcl) {
		return value == null || value <= minExcl;
	}

	public static boolean isValidTimeLimit(Long timeLimit) {
		return timeLimit != null && 0 <= timeLimit && timeLimit <= 99;
	}

}
