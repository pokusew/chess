package cz.martinendler.chess.engine.move;

import cz.martinendler.chess.engine.board.Board;
import cz.martinendler.chess.pgn.PgnUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SanUtilsTest {

	@Test
	public void testDecodeSan1() throws MoveConversionException {

		String[] san = {
			"e4",
			"e6",
			"f4",
		};
		String[] fen = {
			Board.STANDARD_STARTING_POSITION_FEN,
			"rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
			"rnbqkbnr/pppp1ppp/4p3/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2",
			"rnbqkbnr/pppp1ppp/4p3/8/4PP2/8/PPPP2PP/RNBQKBNR b KQkq f3 0 2"
		};

		for (int i = 0; i < san.length; i++) {

			Board board = new Board();
			board.loadFromFen(fen[i]);

			final String sanMove = san[i];

			Move move = assertDoesNotThrow(
				() -> SanUtils.decodeSan(board, sanMove, board.getSideToMove())
			);

			assertNotNull(board.doMove(move, true));

			assertEquals(fen[i + 1], board.getFen());

		}

	}

	@Test
	public void testDecodeSan2() throws MoveConversionException {

		String[] san = {
			"e4",
			"Nc6",
			"d4",
			"Nf6",
			"d5",
			"Ne5",
			"Nf3",
			"d6",
			"Nxe5",
			"dxe5",
			"Bb5+",
			"Bd7",
			"Bxd7+",
			"Qxd7",
			"Nc3",
			"e6",
			"O-O",
			"exd5",
		};

		Board board = new Board();
		board.loadFromFen(Board.STANDARD_STARTING_POSITION_FEN);

		for (int i = 0; i < san.length; i++) {

			final String sanMove = san[i];

			Move move = assertDoesNotThrow(
				() -> SanUtils.decodeSan(board, sanMove, board.getSideToMove()),
				"move " + i + " " + sanMove
			);

			assertNotNull(board.doMove(move, true), "move " + i + " " + sanMove);

		}

		assertEquals(
			"r3kb1r/pppq1ppp/5n2/3pp3/4P3/2N5/PPP2PPP/R1BQ1RK1 w kq - 0 10", board.getFen()
		);

	}

	@Test
	public void testDecodeSan3() throws MoveConversionException {

		String moveText = "1.d4 d5 2.c4 c6 3.Nf3 Nf6 4.e3 Bf5 5.Nc3 e6 6.Nh4 Bg6 7.Nxg6 " +
			"hxg6 8.g3 Nbd7 9.Bg2 dxc4 10.Qe2 Nb6 11.O-O Be7 12.Rd1 Qc7 13.a4 " +
			"a5 14.e4 e5 15.Be3 Nfd7 16.h4 O-O 17.dxe5 Nxe5 18.f4 Nd3 19.e5 " +
			"Bc5 20.Be4 Bxe3+ 21.Qxe3 Nxb2 22.Rdb1 N2xa4 23.Nxa4 Nxa4 24.Rxa4 " +
			"b5 25.Ra2 b4 26.Qe2 c3 27.h5 Qb6+ 28.Kg2 b3 29.Ra3 c2 30.Rh1 " +
			"g5 31.Qc4 Rfb8 32.Bg6 Ra7 33.e6 Qb5 34.exf7+ Kf8 35.Rxb3 Qxb3 " +
			"36.Re1 Rxf7 37.Qc5+ Kg8 38.Bxf7+ Qxf7 39.Qxc2 gxf4 40.Qxc6 fxg3 " +
			"41.Qe6 a4 42.Qe5 Qf2+ 43.Kh3 Qh2+ 44.Kg4 Rb4+ 45.Re4 Qe2+ 46.Kg5 " +
			"Qxe4 47.Qxg7+ Kxg7 48.h6+ Kh8 *";

		Board board = new Board();
		board.loadFromFen(Board.STANDARD_STARTING_POSITION_FEN);

		List<String> moves = assertDoesNotThrow(() -> PgnUtils.parseMoveText(moveText));

		assertEquals(48 * 2, moves.size());

		for (int i = 0; i < moves.size(); i++) {

			final String sanMove = moves.get(i);

			Move move = assertDoesNotThrow(
				() -> SanUtils.decodeSan(board, sanMove, board.getSideToMove()),
				"move " + i + " " + sanMove
			);

			assertNotNull(board.doMove(move, true), "move " + i + " " + sanMove);

		}

		assertEquals("7k/8/7P/6K1/pr2q3/6p1/8/8 w - - 1 49", board.getFen());

	}

}
