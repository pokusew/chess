package cz.martinendler.chess;

import cz.martinendler.chess.ui.Square;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * JavaFX App
 */
public class App extends Application {

	private static Scene scene;

	@Override
	public void start(Stage stage) throws IOException {
		scene = new Scene(loadFXML("primary"));
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());


		TilePane tile = new TilePane();
		tile.setHgap(8);
		tile.setPrefColumns(4);
		for (int i = 0; i < 20; i++) {
			tile.getChildren().add(new Square());
		}

		scene.setRoot(tile);


		stage.setTitle("Chess");
		stage.setX(-1500);
		stage.setY(100);

		stage.setScene(scene);
		stage.show();

		// Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		// stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
		// stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);


	}

	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
