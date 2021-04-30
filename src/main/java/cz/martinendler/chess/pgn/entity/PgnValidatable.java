package cz.martinendler.chess.pgn.entity;

import cz.martinendler.chess.pgn.PgnValidationException;

public interface PgnValidatable {

	void validate() throws PgnValidationException;

	public boolean isValid();

}
