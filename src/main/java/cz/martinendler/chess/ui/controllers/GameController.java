package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
import cz.martinendler.chess.engine.Game;
import cz.martinendler.chess.engine.GameLoadingException;
import cz.martinendler.chess.engine.Player;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.move.Move;
import cz.martinendler.chess.engine.move.MoveConversionException;
import cz.martinendler.chess.engine.pieces.PieceType;
import cz.martinendler.chess.pgn.PgnParseException;
import cz.martinendler.chess.pgn.PgnUtils;
import cz.martinendler.chess.pgn.entity.PgnDatabase;
import cz.martinendler.chess.pgn.entity.PgnGame;
import cz.martinendler.chess.pgn.entity.PgnGameTermination;
import cz.martinendler.chess.ui.*;
import cz.martinendler.chess.utils.StringUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class GameController extends AppAwareController implements Initializable, LifecycleAwareController {

	private static final Logger log = LoggerFactory.getLogger(GameController.class);

	// FXML elements and controllers (injected by FXML loader)
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
	private final @NotNull ChessClock clock;
	private @Nullable Game game;
	private @Nullable GameOptions.GameType gameType;
	private @Nullable Side humanSide;
	private boolean dirty;
	private @Nullable File saveTo;
	private int moveIndex;

	public GameController(App app) {

		super(app);

		log.info("constructor");

		clock = new ChessClock(ChessClock.DISABLED, Platform::runLater);

		reset();

	}

	@Override
	public void stop() {

		log.info("stop");

		// cleanup chess clock thread
		clock.destroy();

	}

	private void reset() {

		log.info("reset");

		clock.stop();

		game = null;
		gameType = null;
		humanSide = null;

		dirty = false;
		saveTo = null;

		moveIndex = -1;

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

			if (game.getLastMoveIndex() != moveIndex) {
				log.info("move origin changed but game.getLastMoveIndex() != moveIndex");
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

		rightViewController.activeMoveIndexProperty().addListener((observable, oldValue, newValue) -> {

			if (game == null) {
				log.info("active move index changed but game is null");
				return;
			}

			viewMove(newValue.intValue());

		});

		clock.setOnRemainingTimeChange((side, time) -> {

			// log.info("clock onRemainingTimeChange: side={} time={}", side, time);

			String formattedTime = StringUtils.formatTimeDuration(time);

			if (side.isWhite()) {
				whiteInfoController.setSideTime(formattedTime);
			} else {
				blackInfoController.setSideTime(formattedTime);
			}

		});

		clock.setOnTimeElapsedChange(side -> {

			log.info("clock onTimeElapsedChange: side={}", side);

			showTimeElapsedDialog(side);

		});

		if (app.getOpenArg() != null && !app.getOpenArg().isEmpty()) {
			log.info("initialize: opening game from open arg with default options");
			openGame(new File(app.getOpenArg()), new GameOptions());
		} else {
			// start with a new game
			log.info("initialize: calling newGame()");
			newGame();
		}

	}


	private static @NotNull String moveIndexToDescription(int moveIndex) {

		if (moveIndex < -1) {
			return "UNKNOWN move";
		}

		if (moveIndex == -1) {
			return "starting position";
		}

		int fullMoveCounter = (moveIndex / 2) + 1;
		Side moveSide = moveIndex % 2 == 0 ? Side.WHITE : Side.BLACK;

		return "move number " + fullMoveCounter + " (" + moveSide.toString() + ")";

	}

	private void viewMove(int newMoveIndex) {

		if (game == null) {
			log.info("viewMove: game is null");
			return;
		}

		if (newMoveIndex == moveIndex) {
			log.info(
				"viewMove: no change (newMoveIndex {} == moveIndex {})",
				newMoveIndex, moveIndex
			);
			return;
		}

		if (newMoveIndex < -1 || newMoveIndex > game.getLastMoveIndex()) {
			log.error(
				"viewMove: moveIndex {} is out of the valid range [-1, {}]",
				newMoveIndex, game.getLastMoveIndex()
			);
			return;
		}

		// update the move index
		moveIndex = newMoveIndex;
		log.info("viewMove: set moveIndex to {}", moveIndex);

		if (moveIndex == game.getLastMoveIndex()) {
			log.info("viewMove: it is the last move index");
			syncWithGame();
			return;
		}

		// update the board
		syncBoardWithGame(moveIndex);
		rightViewController.setMessageBubbleText("Showing board state\nfor " + moveIndexToDescription(moveIndex));

		// highlight the move
		Move specificMove = game.getMove(moveIndex);
		if (specificMove != null) {
			long highlights = specificMove.getFrom().getBitboard() | specificMove.getTo().getBitboard();
			board.setHighlights(highlights);
		}

	}

	private void playRandomMove() {

		if (game == null) {
			log.info("playRandomMove: game == null");
			return;
		}

		if (game.getLastMoveIndex() != moveIndex) {
			log.info("playRandomMove: game.getLastMoveIndex() != moveIndex");
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

		if (game.getLastMoveIndex() != moveIndex) {
			log.info("doMove: game.getLastMoveIndex() != moveIndex");
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

	private void syncBoardWithGame() {
		syncBoardWithGame(moveIndex);
	}

	private void syncBoardWithGame(int specificMoveIndex) {

		if (game == null) {
			log.error("syncWithGame: game == null");
			return;
		}

		for (cz.martinendler.chess.engine.board.Square sq : cz.martinendler.chess.engine.board.Square.values()) {

			cz.martinendler.chess.engine.pieces.Piece requiredPiece = game.getPiece(specificMoveIndex, sq);

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

	}

	private void syncWithGame() {

		if (game == null) {
			log.error("syncWithGame: game == null");
			return;
		}

		moveIndex = game.getLastMoveIndex();

		board.setHighlights(0L);

		syncBoardWithGame();

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

		// if computer player is on move, do its move
		if (game.getResult() == PgnGameTermination.UNKNOWN && gameType == GameOptions.GameType.HUMAN_COMPUTER) {
			if (humanSide != null && game.getSideToMove() == humanSide.flip()) {
				playRandomMove();
			}
		}

		// notify chess clock about side to move change
		clock.start(game.getSideToMove());

	}

	private void offerSaveBeforeReset() {

		if (dirty && game != null) {
			// TODO
			log.info("offerSaveBeforeReset: should offer save of the current game");
		}

	}

	private void replaceGame(
		@NotNull Game newGame,
		@NotNull GameOptions.GameType gameType,
		@NotNull Side humanSide,
		long whiteTimeLimit,
		long blackTimeLimit
	) {

		offerSaveBeforeReset();
		reset();

		game = newGame;
		this.gameType = gameType;
		this.humanSide = humanSide;
		dirty = true;
		saveTo = null;

		clock.setRemainingTime(Side.WHITE, whiteTimeLimit);
		clock.setRemainingTime(Side.BLACK, blackTimeLimit);

		if (gameType == GameOptions.GameType.HUMAN_COMPUTER) {
			Player computer = game.getPlayer(humanSide.flip());
			computer.setName(computer.getName() + " (computer)");
		}

		syncWithGame();

	}

	private void newGame() {

		log.info("newGame: starting new game");

		GameOptions options = showNewGameOptionsDialog();

		if (options == null) {
			log.info("newGame: options == null");
			return;
		}

		replaceGame(
			new Game(options.getStartingFen()),
			options.getType(),
			options.getHumanSide(),
			options.getWhiteTimeLimit(),
			options.getBlackTimeLimit()
		);

	}

	private void openGame() {

		log.info("openGame: opening game from file ...");

		File selectedFile = showOpenDialog();

		if (selectedFile == null) {
			log.info("openGame: no file selected");
			return;
		}

		openGame(selectedFile, null);

	}

	private void openGame(@NotNull File selectedFile, @Nullable GameOptions options) {

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

		Game gameFromPgn = null;

		try {
			gameFromPgn = new Game(pgnDatabase.games.get(gameIdx));
		} catch (GameLoadingException | MoveConversionException e) {
			app.logExceptionAndShowAlert(e);
			return;
		}

		if (options == null) {
			options = showOpenGameOptionsDialog();
			if (options == null) {
				log.info("openGame: options == null");
				return;
			}
		}

		replaceGame(
			gameFromPgn,
			options.getType(),
			options.getHumanSide(),
			options.getWhiteTimeLimit(),
			options.getBlackTimeLimit()
		);

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

		save(selectedFile);

		saveTo = selectedFile;

	}

	private void undoMove() {

		if (game == null) {
			log.info("undoMove: game == null");
			return;
		}

		// when playing against computer we must undo two moves (human and computer)
		// otherwise syncWithGame() would immediately replay the computer move
		if (game.undoLastMoves(gameType == GameOptions.GameType.HUMAN_COMPUTER ? 2 : 1)) {
			log.info("undoMove: successful");
			dirty = true;
			syncWithGame();
		} else {
			log.info("undoMove: unsuccessful");
		}

	}

	/**
	 * Shows pawn promotion dialog and waits until user selects something or closes the dialog
	 *
	 * @return the selected piece or {@code null} if the user did not select anything or closed the dialog
	 */
	private @Nullable PieceType showPromotionDialog() {

		ChoiceDialog<PieceType> choiceDialog = new ChoiceDialog<>(
			PieceType.QUEEN,
			PieceType.getPawnPromotionTypes()
		);

		choiceDialog.setTitle("Pawn promotion");
		choiceDialog.setHeaderText("Pawn promotion");
		choiceDialog.setContentText("Select pawn promotion target:");

		Optional<PieceType> pieceType = choiceDialog.showAndWait();

		return pieceType.orElse(null);

	}

	/**
	 * Shows PGN game selection dialog and waits until user selects something or closes the dialog
	 *
	 * @param numGames number of games in the PGN database
	 * @return the selected game id in range [0, numGames - 1]
	 * or {@code null} if the user did not select anything or closed the dialog
	 */
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

	/**
	 * Shows open file dialog and waits until user selects something or closes the dialog
	 *
	 * @return the selected file {@code null} if the user did not select anything or closed the dialog
	 */
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

	/**
	 * Shows save file dialog and waits until user selects something or closes the dialog
	 *
	 * @return the selected file {@code null} if the user did not select anything or closed the dialog
	 */
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

	private @Nullable GameOptions showGameOptionsDialog(@NotNull String title, @NotNull String fxml) {

		// TODO: consider reusing same instance of the dialog

		Pair<AnchorPane, GameDialogController> loaded = app.loadFXML(fxml);
		AnchorPane root = loaded.getKey();
		GameDialogController controller = loaded.getValue();

		Stage dialogStage = new Stage();
		dialogStage.setTitle(title);
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

	private @Nullable GameOptions showOpenGameOptionsDialog() {
		return showGameOptionsDialog("Open PGN Game Options", "view/game-dialog");
	}

	private @Nullable GameOptions showNewGameOptionsDialog() {
		return showGameOptionsDialog("New Game Options", "view/new-game-dialog");
	}

	private void showTimeElapsedDialog(@NotNull Side side) {

		if (game == null) {
			log.info("showTimeElapsedDialog: game == null");
			return;
		}

		final String message = "Time on " + side.toString() + "'s chess clock elapsed";

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initOwner(app.getPrimaryStage());
		alert.setTitle("Time elapsed");
		alert.setHeaderText(message);
		alert.setContentText(message);
		alert.showAndWait();

	}

	// TODO: edit time dialog
	// TODO: edit player name dialog

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

}
