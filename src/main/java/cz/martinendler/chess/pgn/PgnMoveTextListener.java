package cz.martinendler.chess.pgn;

import cz.martinendler.chess.pgn.antlr4.PGNBaseListener;
import cz.martinendler.chess.pgn.antlr4.PGNParser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple PGN listener that converts the movetext AST into a SAN moves array
 */
public class PgnMoveTextListener extends PGNBaseListener {

	private final List<String> moves;
	private int variationDepth;

	public PgnMoveTextListener() {
		moves = new ArrayList<>();
		variationDepth = 0;
	}

	/**
	 * Checks if the listener is currently inside a RAV (Recursive Annotation Variation)
	 *
	 * @return {@code true} iff the listener is currently inside a RAV (Recursive Annotation Variation)
	 */
	public boolean isInVariation() {
		return variationDepth != 0;
	}

	@Override
	public void enterRecursive_variation(PGNParser.Recursive_variationContext ctx) {
		variationDepth++;
	}

	@Override
	public void exitRecursive_variation(PGNParser.Recursive_variationContext ctx) {
		variationDepth--;
	}

	@Override
	public void enterSan_move(PGNParser.San_moveContext ctx) {

		// currently, moves inside any RAVs (Recursive Annotation Variation) are just ignored
		if (isInVariation()) {
			return;
		}

		moves.add(ctx.getText());
	}

	public List<String> getMoves() {
		return moves;
	}

}
