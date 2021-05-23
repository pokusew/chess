package cz.martinendler.chess;

import cz.martinendler.chess.ui.controllers.AppAwareController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

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

	public static void main(String[] args) {
		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");
		log.info("main, java.version = " + javaVersion + ", javafx.version = " + javafxVersion);
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
	 * Parses the custom window startup position option
	 * <p>
	 * The custom window startup position can be specified as java VM argument/option:
	 * For example, the following option
	 * <pre>
	 *     -Dcz.martinendler.chess.position=-1500:100
	 * <pre/>
	 * corresponds to the position x=-1500, y=100.
	 *
	 * @param value the value of {@code cz.martinendler.chess.position} option
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

	public boolean withErrorDialog(Runnable operation) {

		try {

			operation.run();

			return true;

		} catch (Exception e) {

			logExceptionAndShowAlert(e);

			return false;

		}

	}

	public <T> T withErrorDialog(Callable<T> operation) {

		try {

			return operation.call();

		} catch (Exception e) {

			logExceptionAndShowAlert(e);

			return null;

		}

	}

}
