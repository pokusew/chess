package cz.martinendler.chess.utils;

public class StringUtils {

	/**
	 * Pads the given string with padding (from the left) so it has the given length
	 *
	 * @param str     string to pad
	 * @param padding padding string to use (e.g. "0", " ")
	 * @param length  required length
	 * @return left-padded string of the given length
	 * @see <a href="https://stackoverflow.com/a/391978">String.format details on SO</a>
	 * @see <a href="https://stackoverflow.com/a/53459347">String.repeat on SO</a>
	 */
	public static String leftPad(String str, String padding, int length) {
		return padding.repeat((length - str.length()) / padding.length()) + str;
	}

}
