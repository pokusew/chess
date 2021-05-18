package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
import cz.martinendler.chess.engine.Game;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.pieces.PieceType;
import cz.martinendler.chess.ui.Board;
import cz.martinendler.chess.ui.Piece;
import cz.martinendler.chess.ui.Square;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameController extends AppAwareController implements Initializable {

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
	private AnchorPane rightView;

	@FXML
	private RightViewController rightViewController;

	private Game game;

	public GameController(App app) {
		super(app);
		log.info("constructor");
		game = null;
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
			newGame();
		});

		MenuItem open = new MenuItem("Open...");
		open.setAccelerator(new KeyCodeCombination(
			KeyCode.O,
			KeyCombination.SHORTCUT_DOWN
		));
		open.setOnAction((ActionEvent event) -> {
			log.info("menu: Open");
			openGame();
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

		log.info("menuBar classes: " + String.join(",", menuBar.getStyleClass()));

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		log.info("initialize");

		addMenusToMenuBar();

		board.moveOriginProperty().addListener((observable, oldValue, newValue) -> {

			log.info("move origin changed to " + newValue);

			if (newValue == null) {
				board.setLegalMoves(0L);
				return;
			}

			if (game == null) {
				log.info("move origin changed but game is null");
				return;
			}

			long hintForOrigin = game.getLegalMoves(newValue.getSquare());

			log.info("hintForOrigin " + Long.toBinaryString(hintForOrigin));

			board.setLegalMoves(hintForOrigin);

		});

		board.setMoveAttemptHandler((origin, destination) -> {

			if (game == null) {
				log.info("move attempt but game is null");
				return;
			}

			doMove(origin, destination);

		});

		newGame();

	}

	public void reload() {
		log.info("reload");
	}

	public void unload() {
		log.info("unload");
	}

	private @Nullable PieceType showPromotionDialog() {

		ChoiceDialog<PieceType> choiceDialog = new ChoiceDialog<>(PieceType.QUEEN, PieceType.getPawnPromotionTypes());

		choiceDialog.setTitle("Pawn promotion");
		choiceDialog.setHeaderText("Pawn promotion");
		choiceDialog.setContentText("Select pawn promotion target:");

		Optional<PieceType> pieceType = choiceDialog.showAndWait();

		return pieceType.orElse(null);

	}

	private void doMove(@NotNull Square origin, @NotNull Square target) {

		if (game == null) {
			log.error("doMove: game == null");
			return;
		}

		PieceType promotion = null;

		if (game.isPromotionMove(origin.getSquare(), target.getSquare())) {

			log.info("doMove: isPromotionMove == true");

			promotion = showPromotionDialog();

			if (promotion == null) {
				// user did not select anything
				// do not attempt to this move
				log.info("doMove: isPromotionMove == true BUT showPromotionDialog() returned null");
				return;
			}

		}

		boolean success = game.doMove(origin.getSquare(), target.getSquare(), promotion);

		if (success) {
			log.info("move executed");
			syncWithGame();
		} else {
			log.error("doMove unexpected error");
		}

	}

	private void syncWithGame() {

		if (game == null) {
			log.error("syncWithGame: game == null");
			return;
		}

		for (cz.martinendler.chess.engine.board.Square sq : cz.martinendler.chess.engine.board.Square.values()) {

			cz.martinendler.chess.engine.pieces.Piece requiredPiece = game.getPiece(sq);

			Piece uiPiece = board.getSquare(sq).getPiece();

			cz.martinendler.chess.engine.pieces.Piece currentPiece = uiPiece != null ? uiPiece.getPiece() : null;

			// this square is okay
			if (requiredPiece == currentPiece) {
				continue;
			}

			// there is should be no piece on this square
			if (requiredPiece == null) {
				board.getSquare(sq).setPiece(null);
				continue;
			}

			// there should be a piece and there is currently none
			// or there should be a different piece
			Piece newUiPiece = new Piece(requiredPiece);
			board.getSquare(sq).setPiece(newUiPiece);

		}

		// reset move origin
		board.setMoveOrigin(null);

		// TODO: sync moveLog

		whiteInfoController.setActive(game.getSideToMove() == Side.WHITE);
		blackInfoController.setActive(game.getSideToMove() == Side.BLACK);
		rightViewController.setMessageBubbleText(game.getSideToMove() + " player is on move");

	}

	private void newGame() {

		// TODO

		game = new Game();

		board.clean();

		syncWithGame();

	}

	private void openGame() {

		showOpenDialog();

	}

	private void showOpenDialog() {

		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Open Game");

		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Portable Game Notation Files", "*.pgn"),
			new FileChooser.ExtensionFilter("All Files", "*.*")
		);

		File selectedFile = fileChooser.showOpenDialog(app.getPrimaryStage());

		log.info("showOpenDialog: " + selectedFile);

	}

	private void showAlert() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(app.getPrimaryStage());
		alert.setTitle("Invalid input");
		alert.setHeaderText("Invalid data entered");
		alert.setContentText("Something must be different.");
		alert.showAndWait();
	}

	// TODO: new game dialog
	// TODO: edit time dialog

}
