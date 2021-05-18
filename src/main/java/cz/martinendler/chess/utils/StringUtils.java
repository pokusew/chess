package cz.martinendler.chess.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utils for working with strings
 */
public class StringUtils {

	/**
	 * Pads the given string with padding (from the left) so it has the given length
	 * <p>
	 * Note: For best results ensure that the given string {@code str} has not greater
	 * length than {@code length} and that the padding has only one character
	 *
	 * @param str     string to pad ({@code null} is considered as {@code ""})
	 * @param padding padding string to use (e.g. "0", " "), must not be empty or null
	 * @param length  required length
	 * @return left-padded string that has at least the given length
	 * (that might not be true in case that padding has more that one character)
	 * @see <a href="https://stackoverflow.com/a/391978">String.format details on SO</a>
	 * @see <a href="https://stackoverflow.com/a/53459347">String.repeat on SO</a>
	 */
	public static @NotNull String leftPad(@Nullable String str, @NotNull String padding, int length) {

		str = str != null ? str : ""; // fallback to empty string if null is given

		if (padding.isEmpty()) {
			throw new IllegalArgumentException("Padding argument must not be an empty string.");
		}

		return padding.repeat((length - str.length()) / padding.length()) + str;

	}

	/**
	 * Extracts the last char sequence of a string
	 *
	 * @param str  the str
	 * @param size the size
	 * @return string
	 */
	public static String lastSequence(final String str, int size) {
		return str.substring(str.length() - size);
	}

	/**
	 * Extracts the sequence after the given subsequence
	 *
	 * @param str  the str
	 * @param seq  the seq
	 * @param size the size
	 * @return string
	 */
	public static String afterSequence(final String str, final String seq, int size) {
		int idx = str.indexOf(seq) + seq.length();
		if (idx == 0) {
			return "";
		}
		return str.substring(idx, idx + size);
	}

	/**
	 * Extracts the sequence after the given subsequence
	 *
	 * @param str the str
	 * @param seq the seq
	 * @return string
	 */
	public static String afterSequence(final String str, final String seq) {
		int idx = str.indexOf(seq) + seq.length();
		if (idx == 0) {
			return "";
		}
		return str.substring(idx);
	}

	/**
	 * Extracts the sequence before the given subsequence
	 *
	 * @param str the str
	 * @param seq the seq
	 * @return string
	 */
	public static String beforeSequence(final String str, final String seq) {
		int idx = str.indexOf(seq);
		if (idx == -1) {
			return str;
		}
		return str.substring(0, idx);
	}

}
