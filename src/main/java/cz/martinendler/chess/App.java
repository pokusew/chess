package cz.martinendler.chess;

import cz.martinendler.chess.ui.controllers.AppAwareController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * JavaFX App
 * <p>
 * Flow:
 * 1. constructor
 * 2. main
 * 3. launch
 * 4. init
 * 5. start
 * 6. (when the whole applications exits) stop
 */
public class App extends Application {

	private static final Logger log = LoggerFactory.getLogger(App.class);

	private ControllerFactory controllerFactory;
	private Stage primaryStage;

	private static @Nullable String openArg = null;

	public ControllerFactory getControllerFactory() {
		return controllerFactory;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public App() {
		super();
		log.info("constructor");
	}

	@Override
	public void init() throws Exception {
		log.info("init");
		super.init();
		controllerFactory = new ControllerFactory(this);
	}

	@Override
	public void stop() throws Exception {
		log.info("stop");
		super.stop();
	}

	/**
	 * Parses the {@code --open} argument from CLI arguments
	 *
	 * @param args the CLI arguments
	 * @return the value of the {@code --open} argument (may be an empty string)
	 * or {@code null} if there is none
	 */
	private static @Nullable String parseOpenArg(String[] args) {

		List<String> openArgs = Arrays.stream(args)
			.filter(str -> str != null && str.startsWith("--open="))
			.collect(Collectors.toUnmodifiableList());

		if (openArgs.size() == 0) {
			return null;
		}

		// last one wins
		@NotNull String lastOpenArgs = openArgs.get(openArgs.size() - 1);

		return lastOpenArgs.substring(7);

	}

	public @Nullable String getOpenArg() {
		return openArg;
	}

	public static void main(String[] args) {
		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");
		openArg = parseOpenArg(args);
		log.info(
			"main: java.version = {}, javafx.version = {}, args = {}, openArg={}",
			javaVersion, javafxVersion, args, openArg
		);
		launch(args);
	}

	public static final class StartupPosition {

		private final int x;
		private final int y;

		public StartupPosition(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

	}

	/**
	 * Parses the custom window startup position option value
	 * <p>
	 * For example, the value {@code -1500:100} corresponds to the position {@code x=-1500, y=100}.
	 *
	 * @param value the value of the option
	 * @return {@link StartupPosition} the custom startup position, or {@code null} if there was an parsing error
	 */
	public static @Nullable StartupPosition parseCustomWindowStartupPosition(@Nullable String value) {

		if (value == null || value.isEmpty()) {
			return null;
		}

		String[] parts = value.split(":");

		if (parts.length != 2) {
			log.error("parseCustomWindowStartupPosition: invalid value '" + value + "'");
			return null;
		}

		try {

			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);

			return new StartupPosition(x, y);

		} catch (NumberFormatException e) {

			log.error("parseCustomWindowStartupPosition: invalid value '" + value + "'");
			return null;

		}

	}

	/**
	 * Tries to load and parse the option for custom window startup position
	 * <p>
	 * The custom window startup position can be specified as:
	 * 1. Java VM argument/option {@code cz.martinendler.chess.position}
	 * 2. Environment variable {@code CHESS_UI_POSITION}
	 * If both are set, {@code cz.martinendler.chess.position} takes precedence.
	 * <p>
	 * The syntax of the value is x:y. For example, passing the option
	 * {@code -Dcz.martinendler.chess.position=-1500:100}, sets the startup position to x=-1500, y=100.
	 *
	 * @return {@link StartupPosition} the custom startup position, or {@code null} if there was an error
	 */
	public static @Nullable StartupPosition getCustomWindowStartupPosition() {

		String position = null;

		// first try the VM argument/option
		try {
			position = System.getProperty("cz.martinendler.chess.position", null);
		} catch (SecurityException e) {
			log.error("an error occurred while accessing cz.martinendler.chess.position property", e);
		}

		// fallback to the env variable
		if (position == null) {
			try {
				position = System.getenv("CHESS_UI_POSITION");
			} catch (SecurityException e) {
				log.error("an error occurred while accessing CHESS_UI_POSITION env variable", e);
			}
		}

		return parseCustomWindowStartupPosition(position);

	}

	@Override
	public void start(Stage stage) {

		log.info("start");

		primaryStage = stage;
		primaryStage.setTitle("Chess");

		// allows easily change the main window position
		// may be useful during development with a second display
		StartupPosition position = getCustomWindowStartupPosition();
		if (position != null) {
			log.info("primary stage custom x={} y={}", position.x, position.y);
			primaryStage.setX(position.getX());
			primaryStage.setY(position.getY());
		}

		// center the main window on the primary screen (default behavior, no need for custom code)
		// Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		// stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
		// stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

		initRootScene();

	}

	/**
	 * Initializes the root scene and shows it in the {@link #primaryStage}
	 */
	private void initRootScene() {

		Pair<Parent, AppAwareController> root = loadFXML("view/game");

		Scene scene = new Scene(root.getKey());

		// add global styles
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.show();
		// initially stage's width and height are set to a size needed by the controls on its scene
		primaryStage.setMinWidth(primaryStage.getWidth());
		primaryStage.setMinHeight(primaryStage.getHeight());

	}

	/**
	 * Loads a FXML file from the given URL (relative to the resources path for {@link App} class)
	 * <p>
	 * Uses {@link #controllerFactory} to create all controllers
	 * (that allows for an easy DI, mainly of the {@link App} instance).
	 *
	 * @param fxml the FXML file URL (relative to the resources path for {@link App} class)
	 * @param <P>  type of the root element
	 * @param <C>  type of the controller
	 * @return pair of the root element and the controller
	 */
	public <P, C> Pair<P, C> loadFXML(String fxml) {

		URL location = App.class.getResource(fxml + ".fxml");
		FXMLLoader loader = new FXMLLoader(location);
		loader.setControllerFactory(controllerFactory);

		try {
			P elem = loader.<P>load();
			C controller = loader.getController();
			return new Pair<>(elem, controller);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Logs the given exception and shows an error alert dialog to the user
	 * Waits until the user closes the dialog
	 *
	 * @param e the exception
	 */
	public void logExceptionAndShowAlert(Exception e) {

		log.error("withErrorDialog: " + e.toString());

		// show "feedback" to the user
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(primaryStage);
		alert.setTitle("An error occurred");
		alert.setHeaderText("An error occurred");
		alert.setContentText(e.getMessage());
		alert.showAndWait();

	}

	/**
	 * Runs the given operation and handles all exceptions using {@link #logExceptionAndShowAlert(Exception e)}
	 *
	 * @param operation the operation to run
	 */
	public boolean withErrorDialog(Runnable operation) {

		try {

			operation.run();

			return true;

		} catch (Exception e) {

			logExceptionAndShowAlert(e);

			return false;

		}

	}

	/**
	 * Runs the given operation that returns an result of type {@code T}
	 * and handles all exceptions using {@link #logExceptionAndShowAlert(Exception e)}
	 *
	 * @param operation the operation to run
	 * @param <T>       type of the result of the operation
	 * @return the result of the operation if it finished without any exceptions, {@code null} otherwise
	 */
	public <T> @Nullable T withErrorDialog(Callable<T> operation) {

		try {

			return operation.call();

		} catch (Exception e) {

			logExceptionAndShowAlert(e);

			return null;

		}

	}

}
