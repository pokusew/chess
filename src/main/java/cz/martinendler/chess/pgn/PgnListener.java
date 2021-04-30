package cz.martinendler.chess.pgn;

import cz.martinendler.chess.pgn.antlr4.PGNBaseListener;
import cz.martinendler.chess.pgn.antlr4.PGNParser;
import cz.martinendler.chess.pgn.entity.PgnDatabase;
import cz.martinendler.chess.pgn.entity.PgnGame;

/**
 * A small demo listener that retrieves the player's name and
 * the result from the game termination production rule. Of course
 * the result could more easily be retrieved from the result-tag,
 * [Result "..."], but this is just a quick example of how to
 * retrieve information from the parser context objects.
 */
public class PgnListener extends PGNBaseListener {

	private PgnDatabase database;
	private PgnGame game;

	public PgnListener() {

	}

	public PgnDatabase getDatabase() {
		return database;
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
	public void enterMovetext_section(PGNParser.Movetext_sectionContext ctx) {

		String result = ctx.game_termination().getText();

		// System.out.printf("`%s` with white against `%s` with black resulted in: %s\n",
		// 	white, black, result);

	}

}
