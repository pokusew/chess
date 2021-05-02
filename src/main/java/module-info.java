module chess {

	requires javafx.controls;
	requires javafx.fxml;

	requires org.slf4j;
	requires org.antlr.antlr4.runtime;
	requires org.jetbrains.annotations;

	// opens cz.martinendler.chess to javafx.fxml;
	opens cz.martinendler.chess;
	opens cz.martinendler.chess.ui;
	opens cz.martinendler.chess.ui.controllers;

	opens cz.martinendler.chess.pgn.entity;
	opens cz.martinendler.chess.pgn.antlr4;
	opens cz.martinendler.chess.pgn;

	exports cz.martinendler.chess;

}
