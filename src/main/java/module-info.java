module chess {

	requires javafx.controls;
	requires javafx.fxml;

	requires org.slf4j;
	requires org.antlr.antlr4.runtime;

	// opens cz.martinendler.chess to javafx.fxml;
	opens cz.martinendler.chess;
	opens cz.martinendler.chess.ui;
	opens cz.martinendler.chess.ui.controllers;

	exports cz.martinendler.chess;

}
