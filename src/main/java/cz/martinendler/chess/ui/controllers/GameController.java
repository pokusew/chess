package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
import cz.martinendler.chess.ui.Board;
import cz.martinendler.chess.ui.Piece;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController extends AppAwareController implements Initializable, Reloadable {

	private static final Logger log = LoggerFactory.getLogger(GameController.class);

	@FXML
	private Board board;

	@FXML
	private HBox whiteInfo;

	@FXML
	private SideInfoBoxController whiteInfoController;

	@FXML
	private HBox blackInfo;

	@FXML
	private SideInfoBoxController blackInfoController;

	@FXML
	private ScrollPane moveLog;

	public GameController(App app) {
		super(app);
		log.info("constructor");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		log.info("initialize");

		Piece knight0 = new Piece(0);
		Piece knight1 = new Piece(1);
		Piece knight2 = new Piece(2);

		moveLog.setOnMouseClicked((MouseEvent event) -> {
			log.info("mouse");
		});

		// board.getSquareAt(3, 2).getChildren().setAll(knight0);
		// board.getSquareAt(5, 2).getChildren().setAll(knight1);
		// board.getSquareAt(3, 7).getChildren().setAll(knight2);

	}

	public void reload() {
		log.info("reload");
	}

	public void unload() {
		log.info("unload");
	}

}
