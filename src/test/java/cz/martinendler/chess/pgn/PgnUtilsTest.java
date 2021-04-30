package cz.martinendler.chess.pgn;

import cz.martinendler.chess.pgn.entity.PgnDatabase;
import cz.martinendler.chess.pgn.entity.PgnGame;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PgnUtilsTest {

	private PgnDatabase getExampleDatabase() {

		PgnDatabase db = new PgnDatabase();

		PgnGame game = new PgnGame();

		db.games.add(game);

		game.tags.put("Event", "Zlatoust");
		game.tags.put("Site", "Zlatoust");
		game.tags.put("Date", "1961.??.??");
		game.tags.put("Round", "?");
		game.tags.put("White", "Karpov, Anatoly");
		game.tags.put("Black", "Ponomariov, Boris");
		game.tags.put("Result", "1-0");
		game.tags.put("WhiteElo", "");
		game.tags.put("BlackElo", "");
		game.tags.put("ECO", "C01");

		return db;

	}

	@Test
	void parseNonExistingFile() {

		final String fileName = "src/test/resources/non-existing.pgn";

		assertThrows(IOException.class, () -> PgnUtils.parseFile(fileName));

	}

	@Test
	void parseExampleFileExample() {

		final String fileName = "src/test/resources/example.pgn";

		final PgnDatabase expectedDb = getExampleDatabase();

		PgnDatabase db = assertDoesNotThrow(() -> PgnUtils.parseFile(fileName));

		assertEquals(db.games.size(), expectedDb.games.size());

		final PgnGame game = db.games.get(0);
		final PgnGame expectedGame = expectedDb.games.get(0);

		// assert that all tags are same
		assertEquals(
			db.games.get(0).tags.size(), expectedDb.games.get(0).tags.size(),
			"tags has different size"
		);
		for (String tagName : expectedGame.tags.keySet()) {
			assertEquals(
				game.tags.get(tagName), expectedGame.tags.get(tagName),
				"real value of tag " + tagName + " differs from the expected"
			);
		}

	}

	@Test
	void databaseToString() {

		// TODO

		System.out.println(getExampleDatabase());

	}

}
