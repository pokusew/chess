package cz.martinendler.chess;

import cz.martinendler.chess.ui.controllers.AppAwareController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

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

	@Override
	public void start(Stage stage) {

		log.info("start");

		primaryStage = stage;
		primaryStage.setTitle("Chess");

		// show the main window in the second display for easier developing
		primaryStage.setX(-1500);
		primaryStage.setY(100);

		// Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		// stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
		// stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

		initRootLayout();

	}

	private void initRootLayout() {

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

}
