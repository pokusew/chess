module chess {

	requires javafx.controls;
	requires javafx.fxml;

	requires org.slf4j;
	requires org.antlr.antlr4.runtime;
	requires org.jetbrains.annotations;

	// TODO: everything must be opened because of JUint (better solution?)

	// opens cz.martinendler.chess to javafx.fxml;

	opens cz.martinendler.chess;

	opens cz.martinendler.chess.engine;
	opens cz.martinendler.chess.engine.board;
	opens cz.martinendler.chess.engine.move;
	opens cz.martinendler.chess.engine.pieces;

	opens cz.martinendler.chess.ui;
	opens cz.martinendler.chess.ui.controllers;
	opens cz.martinendler.chess.ui.routing;

	opens cz.martinendler.chess.pgn;
	opens cz.martinendler.chess.pgn.antlr4;
	opens cz.martinendler.chess.pgn.entity;

	opens cz.martinendler.chess.utils;

	// TODO: everything must be exported because of JUint (better solution?)

	exports cz.martinendler.chess;

	exports cz.martinendler.chess.engine;
	exports cz.martinendler.chess.engine.board;
	exports cz.martinendler.chess.engine.move;
	exports cz.martinendler.chess.engine.pieces;

	exports cz.martinendler.chess.ui;
	exports cz.martinendler.chess.ui.controllers;
	exports cz.martinendler.chess.ui.routing;

	exports cz.martinendler.chess.pgn;
	exports cz.martinendler.chess.pgn.antlr4;
	exports cz.martinendler.chess.pgn.entity;

	exports cz.martinendler.chess.utils;

}
