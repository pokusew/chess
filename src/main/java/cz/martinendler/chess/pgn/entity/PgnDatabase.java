package cz.martinendler.chess.pgn.entity;

import java.util.ArrayList;
import java.util.List;

public class PgnDatabase {

	public final List<PgnGame> games;

	public PgnDatabase() {
		games = new ArrayList<>();
	}

}
