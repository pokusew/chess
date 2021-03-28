package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
import cz.martinendler.chess.ui.Board;
import cz.martinendler.chess.ui.Square;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

		// TilePane tile = new TilePane();
		// board.setHgap(8);
		// board.setVgap(8);
		// board.setPrefRows(8);
		// board.setPrefColumns(8);

		// VBox box = new VBox();
		//
		// boolean white = true;
		//
		// for (int i = 0; i < 8; i++) {
		// 	white = !white;
		// 	for (int j = 0; j < 8; j++) {
		// 		white = !white;
		// 		board.getChildren().add(new Square(white, i * 8 + j));
		// 		// board.(new Square(), i, j);
		// 	}
		// }

	}

	public void reload() {
		log.info("reload");
	}

	public void unload() {
		log.info("unload");
	}

}
