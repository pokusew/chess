package cz.martinendler.chess.pgn.entity;

import java.util.HashMap;
import java.util.Map;

public class PgnGame {

	public final Map<String, String> tags;

	public PgnGame() {
		this.tags = new HashMap<>();
	}

}
