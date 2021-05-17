package cz.martinendler.chess.pgn.entity;

import cz.martinendler.chess.pgn.PgnValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of a PGN Database (one PGN file)
 */
public class PgnDatabase extends PgnEntity {

	public final List<PgnGame> games;

	public PgnDatabase() {
		games = new ArrayList<>();
	}

	/**
	 * Validates all games inside this database
	 *
	 * @throws PgnValidationException when there is a invalid game
	 */
	@Override
	public void validate() throws PgnValidationException {
		for (PgnGame game : games) {
			game.validate();
		}
	}

	/**
	 * Converts the database to a PGN database string (PGN file string) in the PGN Export format
	 *
	 * @return a PGN database string (PGN file string) in the PGN Export format
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (PgnGame game : games) {
			sb.append(game.toString());
			sb.append('\n'); // after each game there is an empty line
		}
		return sb.toString();
	}

}
