package cz.martinendler.chess.pgn;

import cz.martinendler.chess.pgn.entity.PgnDatabase;
import cz.martinendler.chess.pgn.entity.PgnGame;
import cz.martinendler.chess.pgn.entity.PgnGameTermination;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PgnUtilsTest {

	@Test
	void exampleDatabaseIsValid() {

		PgnDatabase db = createExampleDatabase();

		assertTrue(db.isValid());

	}

	@Test
	void parseNonExistingFile() {

		final String fileName = "src/test/resources/non-existing.pgn";

		assertThrows(IOException.class, () -> PgnUtils.parseFile(fileName));

	}


	@Test
	void parseInvalidFile() {

		final String fileName = "src/test/resources/invalid.pgn";

		String expectedMessage = """
			The following errors occurred during PGN parsing:
			line 6:0 missing ']' at '['
			line 24:0 missing {'1-0', '0-1', '1/2-1/2', '*'} at '<EOF>'
			""";

		PgnParseException e = assertThrows(PgnParseException.class, () -> PgnUtils.parseFile(fileName));

		assertEquals(expectedMessage, e.getMessage());

	}


	@Test
	void parseEmptyFile() {

		final String fileName = "src/test/resources/empty.pgn";

		PgnDatabase db = assertDoesNotThrow(() -> PgnUtils.parseFile(fileName));

		assertEquals(0, db.games.size());

	}

	@Test
	void parseExampleFile() {

		final String fileName = "src/test/resources/example.pgn";

		final PgnDatabase expectedDb = createExampleDatabase();

		PgnDatabase db = assertDoesNotThrow(() -> PgnUtils.parseFile(fileName));

		assertDatabasesAreEqual(expectedDb, db);

	}

	@Test
	void databaseToString() {

		PgnDatabase db = createExampleDatabase();

		String expectedString = """
			[Event "Zlatoust"]
			[Site "Zlatoust"]
			[Date "1961.??.??"]
			[Round "?"]
			[White "Karpov, Anatoly"]
			[Black "Ponomariov, Boris"]
			[Result "1-0"]
			[BlackElo ""]
			[ECO "C01"]
			[WhiteElo ""]

			1 e4 e63 d4 d55 exd5 exd57 Nf3 Nf69 Bd3 Bg411 O-O Be713 Re1 O-O15 Bg5 Nbd7
			17 Nbd2 Re819 c3 c521 Qb1 c423 Bf5 Bh525 Bf4 Nf827 Ne5 Ng629 Nxg6 Bxg6
			31 Bxg6 fxg633 Qc2 Bd635 Bxd6 Qxd637 Nf3 Ne439 Ne5 Re641 Nxc4 Qf443 Ne3 Nf6
			45 Nf1 Rae847 Rxe6 Rxe649 Rd1 g551 Qd2 Qh453 Ne3 Ne455 Qe2 Rh657 h3 Rf6
			59 Rf1 g461 Nxg4 Rf563 f3 Nd665 Qf2 Qg567 Re1 h569 Ne5 b571 Nc6 Rf773 Qe3 Qg3
			75 Ne7+ Kh877 Nxd5 Nf579 Qf2 Qg581 f4 Qd883 Ne3 Nh485 g3 Ng687 f5 Nf889 Qe2 Qg5
			91 Kg2 Nd793 h4 Qh695 Qf3 Re797 Qf4 Qa699 a3 Nf6101 Nc2 Qc6+103 Qf3 Qe8
			105 Rxe7 Qxe7107 Ne3 Qd6109 Kh3 a5111 d5 b4113 axb4 axb4115 Qf4 Qd7117 c4 Qa7
			119 d6 Qa1121 Qf3 Qxb2123 d7 Qd4125 Nd5 Qg4+127 Qxg4 hxg4+129 Kg2 Nxd7
			131 Nxb4 Kg8133 Nd5 Kf7135 Kf1 g6137 fxg6+ Kxg6139 Ke2 Kf7141 Ke3 Ke6
			143 Kd4 Kd6145 h5 Nc5147 h6 Ne6+149 Ke4 Ng5+151 Kf5 Nf7153 h7 Kc5155 Kf6 Nh8
			157 Kg7 1-0

			""";

		assertEquals(expectedString, db.toString());

	}

	// HELPERS

	private PgnDatabase createExampleDatabase() {

		PgnDatabase db = new PgnDatabase();

		PgnGame game = new PgnGame();

		db.games.add(game);

		// tags
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

		// moves
		game.moves.add("e4");
		game.moves.add("e6");
		game.moves.add("d4");
		game.moves.add("d5");
		game.moves.add("exd5");
		game.moves.add("exd5");
		game.moves.add("Nf3");
		game.moves.add("Nf6");
		game.moves.add("Bd3");
		game.moves.add("Bg4");
		game.moves.add("O-O");
		game.moves.add("Be7");
		game.moves.add("Re1");
		game.moves.add("O-O");
		game.moves.add("Bg5");
		game.moves.add("Nbd7");
		game.moves.add("Nbd2");
		game.moves.add("Re8");
		game.moves.add("c3");
		game.moves.add("c5");
		game.moves.add("Qb1");
		game.moves.add("c4");
		game.moves.add("Bf5");
		game.moves.add("Bh5");
		game.moves.add("Bf4");
		game.moves.add("Nf8");
		game.moves.add("Ne5");
		game.moves.add("Ng6");
		game.moves.add("Nxg6");
		game.moves.add("Bxg6");
		game.moves.add("Bxg6");
		game.moves.add("fxg6");
		game.moves.add("Qc2");
		game.moves.add("Bd6");
		game.moves.add("Bxd6");
		game.moves.add("Qxd6");
		game.moves.add("Nf3");
		game.moves.add("Ne4");
		game.moves.add("Ne5");
		game.moves.add("Re6");
		game.moves.add("Nxc4");
		game.moves.add("Qf4");
		game.moves.add("Ne3");
		game.moves.add("Nf6");
		game.moves.add("Nf1");
		game.moves.add("Rae8");
		game.moves.add("Rxe6");
		game.moves.add("Rxe6");
		game.moves.add("Rd1");
		game.moves.add("g5");
		game.moves.add("Qd2");
		game.moves.add("Qh4");
		game.moves.add("Ne3");
		game.moves.add("Ne4");
		game.moves.add("Qe2");
		game.moves.add("Rh6");
		game.moves.add("h3");
		game.moves.add("Rf6");
		game.moves.add("Rf1");
		game.moves.add("g4");
		game.moves.add("Nxg4");
		game.moves.add("Rf5");
		game.moves.add("f3");
		game.moves.add("Nd6");
		game.moves.add("Qf2");
		game.moves.add("Qg5");
		game.moves.add("Re1");
		game.moves.add("h5");
		game.moves.add("Ne5");
		game.moves.add("b5");
		game.moves.add("Nc6");
		game.moves.add("Rf7");
		game.moves.add("Qe3");
		game.moves.add("Qg3");
		game.moves.add("Ne7+");
		game.moves.add("Kh8");
		game.moves.add("Nxd5");
		game.moves.add("Nf5");
		game.moves.add("Qf2");
		game.moves.add("Qg5");
		game.moves.add("f4");
		game.moves.add("Qd8");
		game.moves.add("Ne3");
		game.moves.add("Nh4");
		game.moves.add("g3");
		game.moves.add("Ng6");
		game.moves.add("f5");
		game.moves.add("Nf8");
		game.moves.add("Qe2");
		game.moves.add("Qg5");
		game.moves.add("Kg2");
		game.moves.add("Nd7");
		game.moves.add("h4");
		game.moves.add("Qh6");
		game.moves.add("Qf3");
		game.moves.add("Re7");
		game.moves.add("Qf4");
		game.moves.add("Qa6");
		game.moves.add("a3");
		game.moves.add("Nf6");
		game.moves.add("Nc2");
		game.moves.add("Qc6+");
		game.moves.add("Qf3");
		game.moves.add("Qe8");
		game.moves.add("Rxe7");
		game.moves.add("Qxe7");
		game.moves.add("Ne3");
		game.moves.add("Qd6");
		game.moves.add("Kh3");
		game.moves.add("a5");
		game.moves.add("d5");
		game.moves.add("b4");
		game.moves.add("axb4");
		game.moves.add("axb4");
		game.moves.add("Qf4");
		game.moves.add("Qd7");
		game.moves.add("c4");
		game.moves.add("Qa7");
		game.moves.add("d6");
		game.moves.add("Qa1");
		game.moves.add("Qf3");
		game.moves.add("Qxb2");
		game.moves.add("d7");
		game.moves.add("Qd4");
		game.moves.add("Nd5");
		game.moves.add("Qg4+");
		game.moves.add("Qxg4");
		game.moves.add("hxg4+");
		game.moves.add("Kg2");
		game.moves.add("Nxd7");
		game.moves.add("Nxb4");
		game.moves.add("Kg8");
		game.moves.add("Nd5");
		game.moves.add("Kf7");
		game.moves.add("Kf1");
		game.moves.add("g6");
		game.moves.add("fxg6+");
		game.moves.add("Kxg6");
		game.moves.add("Ke2");
		game.moves.add("Kf7");
		game.moves.add("Ke3");
		game.moves.add("Ke6");
		game.moves.add("Kd4");
		game.moves.add("Kd6");
		game.moves.add("h5");
		game.moves.add("Nc5");
		game.moves.add("h6");
		game.moves.add("Ne6+");
		game.moves.add("Ke4");
		game.moves.add("Ng5+");
		game.moves.add("Kf5");
		game.moves.add("Nf7");
		game.moves.add("h7");
		game.moves.add("Kc5");
		game.moves.add("Kf6");
		game.moves.add("Nh8");
		game.moves.add("Kg7");

		// game termination marker
		game.termination = PgnGameTermination.WHITE_WINS;

		return db;

	}

	private void assertGamesAreEqual(PgnGame expected, PgnGame actual) {

		// assert that all tags are same
		assertEquals(
			expected.tags.size(), actual.tags.size(),
			"tags has different size"
		);
		for (String tagName : expected.tags.keySet()) {
			assertEquals(
				expected.tags.get(tagName), actual.tags.get(tagName),
				"actual value of tag " + tagName + " differs from the expected"
			);
		}

		// assert that all moves are same
		assertEquals(
			expected.moves.size(), actual.moves.size(),
			"moves has different size"
		);
		for (int i = 0; i < expected.moves.size(); i++) {
			assertEquals(
				expected.moves.get(i), actual.moves.get(i),
				"actual move with index " + i + " differs from the expected"
			);
		}

		// assert that game termination markers are same
		assertEquals(
			expected.termination, actual.termination,
			"actual termination differs from the expected"
		);

	}

	private void assertDatabasesAreEqual(PgnDatabase expected, PgnDatabase actual) {

		assertEquals(
			expected.games.size(), actual.games.size(),
			"databases has different number of games"
		);

		for (int i = 0; i < expected.games.size(); i++) {
			assertGamesAreEqual(expected.games.get(i), actual.games.get(i));
		}

	}

}
