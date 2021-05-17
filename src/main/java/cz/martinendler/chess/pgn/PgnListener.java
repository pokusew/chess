package cz.martinendler.chess.pgn;

import cz.martinendler.chess.pgn.antlr4.PGNBaseListener;
import cz.martinendler.chess.pgn.antlr4.PGNParser;
import cz.martinendler.chess.pgn.entity.PgnDatabase;
import cz.martinendler.chess.pgn.entity.PgnGame;
import cz.martinendler.chess.pgn.entity.PgnGameTermination;

/**
 * A simple PGN listener that converts the AST into a {@link PgnDatabase}
 * <p>
 * It supports tags, movetext and game termination.
 * It ignores: NAG (Numeric Annotation Glyph), RAV (Recursive Annotation Variation)
 */
public class PgnListener extends PGNBaseListener {

	private PgnDatabase database;
	private PgnGame game;
	private int variationDepth;

	public PgnListener() {
		database = null;
		game = null;
		variationDepth = 0;
	}

	public PgnDatabase getDatabase() {
		return database;
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
	public void enterPgn_database(PGNParser.Pgn_databaseContext ctx) {
		database = new PgnDatabase();
	}

	@Override
	public void enterPgn_game(PGNParser.Pgn_gameContext ctx) {
		game = new PgnGame();
		database.games.add(game);
	}

	@Override
	public void enterTag_pair(PGNParser.Tag_pairContext ctx) {

		String tagName = ctx.tag_name().getText();
		String tagValueRaw = ctx.tag_value().getText();

		// STRING tokens starts and ends with " (a quote character)
		String tagValue = tagValueRaw.substring(1, tagValueRaw.length() - 1);

		game.tags.put(tagName, tagValue);

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

		game.moves.add(ctx.getText());
	}

	@Override
	public void enterGame_termination(PGNParser.Game_terminationContext ctx) {
		game.termination = PgnGameTermination.fromNotation(ctx.getText());
	}

}
