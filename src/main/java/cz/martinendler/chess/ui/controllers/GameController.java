package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
import cz.martinendler.chess.engine.Game;
import cz.martinendler.chess.engine.GameLoadingException;
import cz.martinendler.chess.engine.Player;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.move.MoveConversionException;
import cz.martinendler.chess.engine.pieces.PieceType;
import cz.martinendler.chess.pgn.PgnParseException;
import cz.martinendler.chess.pgn.PgnUtils;
import cz.martinendler.chess.pgn.entity.PgnDatabase;
import cz.martinendler.chess.pgn.entity.PgnGame;
import cz.martinendler.chess.pgn.entity.PgnGameTermination;
import cz.martinendler.chess.ui.Board;
import cz.martinendler.chess.ui.GameOptions;
import cz.martinendler.chess.ui.Piece;
import cz.martinendler.chess.ui.Square;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameController extends AppAwareController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(GameController.class);

	// FXML elements and controllers
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

	// controller state
	private boolean dirty;
	private @Nullable File saveTo;
	private @Nullable Game game;
	private @Nullable GameOptions.GameType gameType;
	private @Nullable Side humanSide;

	public GameController(App app) {
		super(app);
		log.info("constructor");
	}

	private void reset() {
		log.info("reset");
		dirty = false;
		saveTo = null;
		game = null;
		gameType = null;
		humanSide = null;
	}

	protected void addMenusToMenuBar() {

		// create File menu
		Menu fileMenu = new Menu("File");

		// create menu items
		MenuItem newGameItem = new MenuItem("New Game...");
		newGameItem.setAccelerator(new KeyCodeCombination(
			KeyCode.N,
			KeyCombination.SHORTCUT_DOWN
		));
		newGameItem.setOnAction((ActionEvent event) -> {
			log.info("menu: New Game");
			newGame();
		});

		MenuItem openItem = new MenuItem("Open...");
		openItem.setAccelerator(new KeyCodeCombination(
			KeyCode.O,
			KeyCombination.SHORTCUT_DOWN
		));
		openItem.setOnAction((ActionEvent event) -> {
			log.info("menu: Open");
			openGame();
		});

		MenuItem saveItem = new MenuItem("Save");
		saveItem.setAccelerator(new KeyCodeCombination(
			KeyCode.S,
			KeyCombination.SHORTCUT_DOWN
		));
		saveItem.setOnAction((ActionEvent event) -> {
			log.info("menu: Save");
			saveGame();
		});

		MenuItem saveAsItem = new MenuItem("Save As...");
		saveAsItem.setAccelerator(new KeyCodeCombination(
			KeyCode.S,
			KeyCombination.ModifierValue.DOWN,
			KeyCombination.ModifierValue.UP,
			KeyCombination.ModifierValue.UP,
			KeyCombination.ModifierValue.UP,
			KeyCombination.ModifierValue.DOWN
		));
		saveAsItem.setOnAction((ActionEvent event) -> {
			log.info("menu: Save As...");
			saveGameAs();
		});

		// add menu items to menu
		fileMenu.getItems().add(newGameItem);
		fileMenu.getItems().add(openItem);
		fileMenu.getItems().add(saveItem);
		fileMenu.getItems().add(saveAsItem);

		// create File menu
		Menu editMenu = new Menu("Edit");

		// create menu items
		MenuItem undoMoveItem = new MenuItem("Undo Move");
		undoMoveItem.setAccelerator(new KeyCodeCombination(
			KeyCode.Z,
			KeyCombination.SHORTCUT_DOWN
		));
		undoMoveItem.setOnAction((ActionEvent event) -> {
			log.info("menu: Undo Move");
			undoMove();
		});

		// TODO: uncomment once implemented
		// MenuItem redoMoveItem = new MenuItem("Redo Move");
		// redoMoveItem.setAccelerator(new KeyCodeCombination(
		// 	KeyCode.Y,
		// 	KeyCombination.SHORTCUT_DOWN
		// ));
		// redoMoveItem.setOnAction((ActionEvent event) -> {
		// 	log.info("menu: Redo Move");
		// });

		// add menu items to menu
		editMenu.getItems().add(undoMoveItem);
		// editMenu.getItems().add(redoMoveItem);

		// TODO: uncomment once implemented
		// // create Help menu
		// Menu helpMenu = new Menu("Help");
		//
		// // create menu items
		// MenuItem aboutChessItem = new MenuItem("About Chess");
		// aboutChessItem.setOnAction((ActionEvent event) -> {
		// 	log.info("menu: About Chess");
		// 	showHelpDialog();
		// });
		//
		// MenuItem preferencesItem = new MenuItem("Preferences...");
		// preferencesItem.setOnAction((ActionEvent event) -> {
		// 	log.info("menu: Preferences...");
		// 	showPreferencesDialog();
		// });
		//
		// // add menu items to menu
		// helpMenu.getItems().add(aboutChessItem);
		// helpMenu.getItems().add(preferencesItem);

		// add menus to the menu bar
		menuBar.getMenus().addAll(fileMenu, editMenu);

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

		// start with a new game
		newGame();

	}

	private void playRandomMove() {

		if (game == null) {
			log.info("playRandomMove: game == null");
			return;
		}

		log.info("playRandomMove: sideToMove = {}", game.getSideToMove());

		if (game.doRandomMove()) {
			dirty = true;
			log.info("random move executed");
			syncWithGame();
		} else {
			log.error("doRandomMove unexpected error");
		}

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
			dirty = true;
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

		rightViewController.updateMoveLog(game.getMoveLog());

		whiteInfoController.setSideName(game.getPlayer(Side.WHITE).getName());
		blackInfoController.setSideName(game.getPlayer(Side.BLACK).getName());

		whiteInfoController.setActive(game.getSideToMove() == Side.WHITE);
		blackInfoController.setActive(game.getSideToMove() == Side.BLACK);

		switch (game.getResult()) {
			case UNKNOWN -> rightViewController.setMessageBubbleText(game.getPlayer(game.getSideToMove()).getName() + " is on move");
			case DRAWN_GAME -> rightViewController.setMessageBubbleText("Drawn by stalemate");
			case WHITE_WINS -> rightViewController.setMessageBubbleText(game.getPlayer(Side.WHITE).getName() + " won");
			case BLACK_WINS -> rightViewController.setMessageBubbleText(game.getPlayer(Side.BLACK).getName() + " won");
		}

		if (game.getResult() == PgnGameTermination.UNKNOWN && gameType == GameOptions.GameType.HUMAN_COMPUTER) {
			if (humanSide != null && game.getSideToMove() == humanSide.flip()) {
				playRandomMove();
			}
		}

	}

	private void offerSaveBeforeReset() {

		if (dirty && game != null) {
			// TODO
			log.info("offer save");
		}

	}

	private void newGame() {

		log.info("newGame: starting new game");

		GameOptions options = showGameDialog();

		if (options == null) {
			log.info("newGame: options == null");
			return;
		}

		offerSaveBeforeReset();
		reset();

		gameType = options.getType();
		humanSide = options.getHumanSide();

		game = new Game(options.getStartingFen());

		if (gameType == GameOptions.GameType.HUMAN_COMPUTER) {
			Player computer = game.getPlayer(humanSide.flip());
			computer.setName(computer.getName() + " (computer)");
		}

		syncWithGame();

	}

	private void openGame() {

		log.info("openGame: opening game from file ...");

		File selectedFile = showOpenDialog();

		if (selectedFile == null) {
			log.info("openGame: no file selected");
			return;
		}

		GameOptions options = showGameDialog();

		if (options == null) {
			log.info("openGame: options == null");
			return;
		}

		offerSaveBeforeReset();
		reset();

		gameType = options.getType();
		humanSide = options.getHumanSide();

		PgnDatabase pgnDatabase = null;

		try {
			pgnDatabase = PgnUtils.parseFile(selectedFile.getAbsolutePath());
		} catch (IOException | PgnParseException e) {
			app.logExceptionAndShowAlert(e);
			return;
		}

		if (pgnDatabase.games.size() == 0) {
			log.info("empty PGN database");
			return;
		}

		int gameIdx = 0;

		if (pgnDatabase.games.size() > 1) {
			Integer idx = showPgnGameSelectionDialog(pgnDatabase.games.size());
			if (idx == null) {
				log.info("no PGN game selected");
				return;
			}
			gameIdx = idx;
		}

		game = null;

		try {
			game = new Game(pgnDatabase.games.get(gameIdx));
		} catch (GameLoadingException | MoveConversionException e) {
			app.logExceptionAndShowAlert(e);
			return;
		}

		if (gameType == GameOptions.GameType.HUMAN_COMPUTER) {
			Player computer = game.getPlayer(humanSide.flip());
			computer.setName(computer.getName() + " (computer)");
		}

		syncWithGame();

	}

	private void save(File target) {

		if (game == null) {
			log.info("save: game == null");
			return;
		}

		if (target == null) {
			log.info("save: target == null");
			return;
		}

		log.info("save: target = {}", target);

		try {

			PgnGame pgnGame = game.toPgnGame();
			PgnDatabase db = new PgnDatabase();
			db.games.add(pgnGame);

			try (PrintWriter out = new PrintWriter(target.getAbsoluteFile())) {
				out.println(db.toString());
			} catch (FileNotFoundException e) {
				app.logExceptionAndShowAlert(e);
				return;
			}

		} catch (Exception e) {
			app.logExceptionAndShowAlert(e);
			return;
		}


	}

	private void saveGame() {

		if (game == null) {
			log.info("saveGame: game == null");
			return;
		}

		if (saveTo != null) {
			save(saveTo);
			return;
		}

		saveGameAs();

	}

	private void saveGameAs() {

		if (game == null) {
			log.info("saveGameAs: game == null");
			return;
		}

		File selectedFile = showSaveDialog();

		if (selectedFile == null) {
			log.info("saveGameAs: selectedFile == null");
			return;
		}

		try {

			PgnGame pgnGame = game.toPgnGame();
			PgnDatabase db = new PgnDatabase();
			db.games.add(pgnGame);

			try (PrintWriter out = new PrintWriter(selectedFile.getAbsoluteFile())) {
				out.println(db.toString());
			} catch (FileNotFoundException e) {
				app.logExceptionAndShowAlert(e);
				return;
			}

		} catch (Exception e) {
			app.logExceptionAndShowAlert(e);
			return;
		}

		saveTo = selectedFile;

	}

	private void undoMove() {

		if (game == null) {
			log.info("undoMove: game == null");
			return;
		}

		if (game.undoLastMove()) {
			log.info("undoMove: successful");
			dirty = true;
			syncWithGame();
		} else {
			log.info("undoMove: unsuccessful");
		}

	}

	private @Nullable PieceType showPromotionDialog() {

		ChoiceDialog<PieceType> choiceDialog = new ChoiceDialog<>(PieceType.QUEEN, PieceType.getPawnPromotionTypes());

		choiceDialog.setTitle("Pawn promotion");
		choiceDialog.setHeaderText("Pawn promotion");
		choiceDialog.setContentText("Select pawn promotion target:");

		Optional<PieceType> pieceType = choiceDialog.showAndWait();

		return pieceType.orElse(null);

	}

	private @Nullable Integer showPgnGameSelectionDialog(int numGames) {

		List<Integer> idx = IntStream.iterate(0, i -> i + 1)
			.limit(numGames).boxed().collect(Collectors.toUnmodifiableList());

		ChoiceDialog<Integer> choiceDialog = new ChoiceDialog<>(0, idx);

		choiceDialog.setTitle("PGN Game Selection");
		choiceDialog.setHeaderText("PGN Game Selection");
		choiceDialog.setContentText("Select PGN game from the file:");

		Optional<Integer> gameIdx = choiceDialog.showAndWait();

		return gameIdx.orElse(null);

	}

	private @Nullable File showOpenDialog() {

		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Open Game");

		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Portable Game Notation Files", "*.pgn"),
			new FileChooser.ExtensionFilter("All Files", "*.*")
		);

		File selectedFile = fileChooser.showOpenDialog(app.getPrimaryStage());

		log.info("showOpenDialog: " + selectedFile);

		return selectedFile;

	}

	private @Nullable File showSaveDialog() {

		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Save Game");

		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Portable Game Notation Files", "*.pgn"),
			new FileChooser.ExtensionFilter("All Files", "*.*")
		);

		File selectedFile = fileChooser.showSaveDialog(app.getPrimaryStage());

		log.info("showSaveDialog: " + selectedFile);

		return selectedFile;

	}

	private @Nullable GameOptions showGameDialog() {

		// TODO: consider reusing same instance of the dialog

		Pair<AnchorPane, GameDialogController> loaded = app.loadFXML("view/game-dialog");
		AnchorPane root = loaded.getKey();
		GameDialogController controller = loaded.getValue();

		Stage dialogStage = new Stage();
		dialogStage.setTitle("Game Options");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(app.getPrimaryStage());
		Scene scene = new Scene(root);
		dialogStage.setScene(scene);

		GameOptions options = new GameOptions();

		controller.setDialogStage(dialogStage);
		controller.setOptions(options);

		dialogStage.showAndWait();

		if (!controller.isSuccessful()) {
			return null;
		}

		return options;

	}

	// TODO: edit time dialog
	// TODO: edit player name dialog

}
