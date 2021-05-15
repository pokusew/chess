package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
import cz.martinendler.chess.engine.Game;
import cz.martinendler.chess.engine.board.Square;
import cz.martinendler.chess.ui.Board;
import cz.martinendler.chess.ui.Piece;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController extends AppAwareController implements Initializable, Reloadable {

	private static final Logger log = LoggerFactory.getLogger(GameController.class);

	@FXML
	private MenuBar menuBar;

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
	private Text messageBubbleText;

	@FXML
	private ScrollPane moveLog;

	private Game game;

	public GameController(App app) {
		super(app);
		log.info("constructor");

		game = new Game();

	}

	protected void addMenusToMenuBar() {

		// create File menu
		Menu fileMenu = new Menu("File");

		// create menu items
		MenuItem newGame = new MenuItem("New Game...");
		newGame.setAccelerator(new KeyCodeCombination(
			KeyCode.N,
			KeyCombination.SHORTCUT_DOWN
		));
		newGame.setOnAction((ActionEvent event) -> {
			log.info("menu: New Game");
		});

		MenuItem open = new MenuItem("Open...");
		open.setAccelerator(new KeyCodeCombination(
			KeyCode.O,
			KeyCombination.SHORTCUT_DOWN
		));
		open.setOnAction((ActionEvent event) -> {
			log.info("menu: Open");
		});

		MenuItem save = new MenuItem("Save");
		save.setAccelerator(new KeyCodeCombination(
			KeyCode.S,
			KeyCombination.SHORTCUT_DOWN
		));
		save.setOnAction((ActionEvent event) -> {
			log.info("menu: Save");
		});

		MenuItem saveAs = new MenuItem("Save As...");
		saveAs.setAccelerator(new KeyCodeCombination(
			KeyCode.S,
			KeyCombination.ModifierValue.DOWN,
			KeyCombination.ModifierValue.UP,
			KeyCombination.ModifierValue.UP,
			KeyCombination.ModifierValue.UP,
			KeyCombination.ModifierValue.DOWN
		));
		saveAs.setOnAction((ActionEvent event) -> {
			log.info("menu: Save As...");
		});

		// add menu items to menu
		fileMenu.getItems().add(newGame);
		fileMenu.getItems().add(open);
		fileMenu.getItems().add(save);
		fileMenu.getItems().add(saveAs);

		// create File menu
		Menu editMenu = new Menu("Edit");

		// create menu items
		MenuItem undoMove = new MenuItem("Undo Move");
		undoMove.setAccelerator(new KeyCodeCombination(
			KeyCode.Z,
			KeyCombination.SHORTCUT_DOWN
		));
		undoMove.setOnAction((ActionEvent event) -> {
			log.info("menu: Undo Move");
		});

		MenuItem redoMove = new MenuItem("Redo Move");
		redoMove.setAccelerator(new KeyCodeCombination(
			KeyCode.Y,
			KeyCombination.SHORTCUT_DOWN
		));
		redoMove.setOnAction((ActionEvent event) -> {
			log.info("menu: Redo Move");
		});

		// add menu items to menu
		editMenu.getItems().add(undoMove);
		editMenu.getItems().add(redoMove);

		// create Help menu
		Menu helpMenu = new Menu("Help");

		// create menu items
		MenuItem aboutChess = new MenuItem("About Chess");
		aboutChess.setOnAction((ActionEvent event) -> {
			log.info("menu: About Chess");
		});

		MenuItem preferences = new MenuItem("Preferences...");
		preferences.setOnAction((ActionEvent event) -> {
			log.info("menu: Preferences...");
		});

		// add menu items to menu
		helpMenu.getItems().add(aboutChess);
		helpMenu.getItems().add(preferences);

		// add menus to the menu bar
		menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

		log.info("menuBar classes: "+ String.join(",", menuBar.getStyleClass()));

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		log.info("initialize");

		addMenusToMenuBar();

		Piece knight0 = new Piece(0);
		Piece knight1 = new Piece(1);
		Piece knight2 = new Piece(2);

		moveLog.setOnMouseClicked((MouseEvent event) -> {
			log.info("onMouseClicked: target " + event.getTarget().toString());
		});

		// game.getBoard().getPiece(Square.fromIndex(1))

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
