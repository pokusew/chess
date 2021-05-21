package cz.martinendler.chess.pgn.entity;

import cz.martinendler.chess.pgn.PgnUtils;
import cz.martinendler.chess.pgn.PgnValidationException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A representation of a PGN Game that is inside a {@link PgnDatabase}
 */
public class PgnGame extends PgnEntity {

	/**
	 * In PGN export format, tokens in the movetext are placed left justified
	 * on successive text lines each of which has less than 80 printing characters.
	 */
	public static final int MOVE_TEXT_LINE_CHAR_LIMIT = 80;

	/**
	 * Each PGN game must have at least these 7 tags in this order
	 *
	 * @see PgnGame#SEVEN_TAG_ROSTER_SET
	 */
	public final static @NotNull List<@NotNull String> SEVEN_TAG_ROSTER = List.of(
		"Event",
		"Site",
		"Date",
		"Round",
		"White",
		"Black",
		"Result"
	);

	/**
	 * Each PGN game must have at least these 7 tags
	 *
	 * @see PgnGame#SEVEN_TAG_ROSTER
	 */
	public final static @NotNull Set<@NotNull String> SEVEN_TAG_ROSTER_SET = Set.copyOf(SEVEN_TAG_ROSTER);

	/**
	 * PGN game tag pairs
	 */
	public final @NotNull Map<String, String> tags;

	/**
	 * An array of SAN-encoded moves
	 */
	public final @NotNull List<String> moves;

	/**
	 * PGN game termination marker
	 */
	public @NotNull PgnGameTermination termination;

	public PgnGame() {
		this.tags = new HashMap<>();
		this.moves = new ArrayList<>();
		this.termination = PgnGameTermination.UNKNOWN;
	}

	/**
	 * Validates that:
	 * 1. Seven Tag Roster is present
	 * 2. Result tag matches Game Termination Marker
	 * 3. If SetUp tag is 1, then FEN tag is present
	 * Note: Nothing else is validated (no even moves)!
	 *
	 * @throws PgnValidationException when something is not valid
	 */
	@Override
	public void validate() throws PgnValidationException {

		// check that all 7 required tags are present
		// PGN 8.1.1: Seven Tag Roster
		// http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c8.1.1

		for (String tagName : SEVEN_TAG_ROSTER) {
			if (!tags.containsKey(tagName)) {
				throw new PgnValidationException(
					"The required tag " + tagName + " is not present."
				);
			}
		}

		// The game termination marker appearing in the movetext of a game must match
		// the value of the game's Result tag pair.

		if (!termination.getNotation().equals(tags.get("Result"))) {
			throw new PgnValidationException(
				"The game termination marker must match the value of the Result tag."
			);
		}

		// check that custom starting position is correctly set
		// PGN 9.7: Alternative starting positions
		// http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c9.7.2

		if ("1".equals(tags.get("SetUp")) && tags.get("FEN") == null) {
			throw new PgnValidationException(
				"The SetUp tag is 1 but FEN tag is NOT set."
			);
		}

		// currently, nothing else is validated

	}

	/**
	 * Converts all tags to a tag pair section string in the PGN Export format
	 * <p>
	 * Maintains the mandatory order for Seven Tag Roster.
	 * Adds empty strings for non-existing tags from Seven Tag Roster.
	 * Order for the remaining tags is alphabetical.
	 *
	 * @return a tag pair section string in the PGN Export format
	 */
	public String tagsToString() {

		final StringBuilder sb = new StringBuilder();

		for (String tagName : SEVEN_TAG_ROSTER) {
			sb.append(PgnUtils.tagToString(tagName, tags.get(tagName)));
			sb.append('\n');
		}

		List<String> otherTagNames = tags.keySet().stream()
			.filter(Predicate.not(SEVEN_TAG_ROSTER_SET::contains))
			// in the future we might want implement here custom ordering
			// (and merge it with SEVEN_TAG_ROSTER code)
			.sorted() // natural oder (alphabetical)
			.collect(Collectors.toUnmodifiableList());

		for (String tagName : otherTagNames) {
			sb.append(PgnUtils.tagToString(tagName, tags.get(tagName)));
			sb.append('\n');
		}

		return sb.toString();

	}

	/**
	 * Appends the given string {@code str} to the movetext
	 * <p>
	 * In PGN export format, tokens in the movetext are placed left justified
	 * on successive text lines each of which has less
	 * than {@link PgnGame#MOVE_TEXT_LINE_CHAR_LIMIT} printing characters.
	 *
	 * @param movetext       the movetext string builder
	 * @param numCharsInLine the number of characters in the current line
	 * @param str            the non-null string to append
	 * @return the updated number of characters in the current line
	 */
	private static int appendToMoveText(@NotNull StringBuilder movetext, int numCharsInLine, @NotNull String str) {

		// In PGN export format, tokens in the movetext are placed left justified
		// on successive text lines each of which has less than 80 printing characters.

		final boolean spaceNeeded = numCharsInLine != 0;

		int numCharsInLineAfter = numCharsInLine + str.length() + (spaceNeeded ? 1 : 0);

		if (numCharsInLineAfter >= MOVE_TEXT_LINE_CHAR_LIMIT) {
			// append a newline (separator) between the previous and the current fullMoveText
			movetext.append("\n");
			// reset the value of numCharsInLine
			numCharsInLineAfter = str.length();
		} else if (spaceNeeded) {
			// append a space (separator) between the previous and the current fullMoveText
			movetext.append(" ");
		}

		// append the actual given string
		movetext.append(str);

		return numCharsInLineAfter;

	}

	/**
	 * Converts all moves (array of string of SAN-encoded moves) to a movetext string
	 * in the PGN Export format
	 *
	 * @return a movetext string in the PGN Export format
	 */
	public String movesToString() {

		final StringBuilder movetext = new StringBuilder();

		int numCharsInLine = 0;

		// iterate trough moves by 2 (two moves form one full-move)
		// first full-move has number 1
		for (
			int whiteIdx = 0, blackIdx = 1, fullMoveCounter = 1;
			whiteIdx < moves.size();
			whiteIdx += 2, blackIdx += 2, fullMoveCounter += 1
		) {

			final String whiteMove = moves.get(whiteIdx);
			final String blackMove = blackIdx < moves.size() ? moves.get(blackIdx) : null;

			final String fullMoveText = fullMoveCounter + "."
				+ whiteMove
				+ (blackMove != null ? (" " + blackMove) : "");

			numCharsInLine = appendToMoveText(movetext, numCharsInLine, fullMoveText);

		}

		// add game termination marker
		// (here we can ignore the return value)
		appendToMoveText(movetext, numCharsInLine, termination.getNotation());

		// trailing end of line
		movetext.append("\n");

		return movetext.toString();

	}

	/**
	 * Converts the game to a PGN game string in the PGN Export format
	 *
	 * @return a PGN game string in the PGN Export format
	 */
	@Override
	public String toString() {
		return (
			tagsToString()
				+ "\n" // empty line
				+ movesToString()
		);
	}

}
