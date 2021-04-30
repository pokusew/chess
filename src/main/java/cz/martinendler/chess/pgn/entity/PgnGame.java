package cz.martinendler.chess.pgn.entity;

import cz.martinendler.chess.pgn.PgnUtils;
import cz.martinendler.chess.pgn.PgnValidationException;

import java.util.*;

public class PgnGame extends PgnEntity {

	// TODO: is order guaranteed, is this ordered set?
	public final static Set<String> SEVEN_TAG_ROSTER = Set.of(
		"Event",
		"Site",
		"Date",
		"Round",
		"White",
		"Black",
		"Result"
	);

	public final Map<String, String> tags;
	public final List<String> moves;
	public PgnGameTermination termination;

	public PgnGame() {
		this.tags = new HashMap<>();
		this.moves = new ArrayList<>();
		this.termination = PgnGameTermination.UNKNOWN;
	}

	@Override
	public void validate() throws PgnValidationException {

		// check that all 7 required tags are present
		// 8.1.1: Seven Tag Roster
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

		// TODO: validate moves?

	}

	public String tagsToString() {
		final StringBuilder sb = new StringBuilder();
		// TODO: maintain the mandatory order for SEVEN_TAG_ROSTER
		for (String tagName : tags.keySet()) {
			sb.append(PgnUtils.tagToString(tagName, tags.get(tagName)));
			sb.append('\n');
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return tagsToString() + '\n' + "TODO MOVES\n";
	}

}
