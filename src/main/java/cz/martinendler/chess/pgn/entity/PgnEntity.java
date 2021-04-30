package cz.martinendler.chess.pgn.entity;

import cz.martinendler.chess.pgn.PgnValidationException;

abstract public class PgnEntity implements PgnValidatable {

	@Override
	public boolean isValid() {

		try {
			validate();
			return true;
		} catch (PgnValidationException e) {
			return false;
		}

	}

}
