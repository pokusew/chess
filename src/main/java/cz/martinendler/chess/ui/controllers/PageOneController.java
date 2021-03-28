package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
import cz.martinendler.chess.ui.Board;
import cz.martinendler.chess.ui.Piece;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PageOneController extends AppAwareController implements Initializable, Reloadable {

	private static final Logger log = LoggerFactory.getLogger(PageOneController.class);

	@FXML
	private Board board;

	public PageOneController(App app) {
		super(app);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		log.info("initialize");

		Piece knight0 = new Piece(0);
		Piece knight1 = new Piece(1);
		Piece knight2 = new Piece(2);

		board.getSquareAt(3, 2).getChildren().setAll(knight0);
		board.getSquareAt(5, 2).getChildren().setAll(knight1);
		board.getSquareAt(3, 7).getChildren().setAll(knight2);

	}

	public void reload() {
		log.info("reload");
	}

	public void unload() {
		log.info("unload");
	}

}
