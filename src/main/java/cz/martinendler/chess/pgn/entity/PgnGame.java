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

	public final static @NotNull List<@NotNull String> SEVEN_TAG_ROSTER = List.of(
		"Event",
		"Site",
		"Date",
		"Round",
		"White",
		"Black",
		"Result"
	);

	public final static @NotNull Set<@NotNull String> SEVEN_TAG_ROSTER_SET = Set.copyOf(SEVEN_TAG_ROSTER);

	public final @NotNull Map<String, String> tags;

	/**
	 * An array of SAN-encoded moves
	 */
	public final @NotNull List<String> moves;

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
	 * Converts all moves (array of string of SAN-encoded moves) to a movetext string
	 * in the PGN Export format
	 *
	 * @return a movetext string in the PGN Export format
	 */
	public String movesToString() {

		final StringBuilder sb = new StringBuilder();

		// In PGN export format, tokens in the movetext are placed left justified
		// on successive text lines  each of which has less than 80 printing characters.
		int numCharsInLine = 0;

		for (int i = 0; i < moves.size(); i += 2) {

			String fullMoveNumber = Integer.toString(i + 1); // first full-move has number 1
			String whiteMove = moves.get(i);
			String blackMove = i + 1 < moves.size() ? moves.get(i + 1) : null;

			String fullMoveText = fullMoveNumber + " " + whiteMove + (blackMove != null ? (" " + blackMove) : "");

			numCharsInLine += fullMoveText.length();

			if (numCharsInLine >= 80) {
				sb.append("\n");
				numCharsInLine = fullMoveText.length();
			}

			sb.append(fullMoveText);

		}

		// add game termination marker
		if (numCharsInLine >= 80) {
			sb.append("\n");
			sb.append(termination.getNotation());
		} else {
			sb.append(" ");
			sb.append(termination.getNotation());

		}

		// trailing end of line
		sb.append("\n");

		return sb.toString();

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
