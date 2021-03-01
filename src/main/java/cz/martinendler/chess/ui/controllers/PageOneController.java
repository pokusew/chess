package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
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
import javafx.scene.layout.AnchorPane;
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

	public PageOneController(App app) {
		super(app);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		log.info("initialize");



	}

	public void reload() {
		log.info("reload");
	}

	public void unload() {
		log.info("unload");
	}

}
