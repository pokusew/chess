module chess {

	requires javafx.controls;
	requires javafx.fxml;

	requires org.slf4j;

	// opens cz.martinendler.chess to javafx.fxml;
	opens cz.martinendler.chess;
	opens cz.martinendler.chess.ui.controllers;
	opens cz.martinendler.chess.engine;

	exports cz.martinendler.chess;

}