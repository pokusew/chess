package cz.martinendler.chess.pgn.entity;

import cz.martinendler.chess.pgn.PgnValidationException;

import java.util.ArrayList;
import java.util.List;

public class PgnDatabase extends PgnEntity {

	public final List<PgnGame> games;

	public PgnDatabase() {
		games = new ArrayList<>();
	}

	@Override
	public void validate() throws PgnValidationException {
		for (PgnGame game : games) {
			game.validate();
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (PgnGame game : games) {
			sb.append(game.toString());
			sb.append('\n');
		}
		return sb.toString();
	}
}
