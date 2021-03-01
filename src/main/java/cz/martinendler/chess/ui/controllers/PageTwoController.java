package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PageTwoController extends AppAwareController implements Initializable, Reloadable {

	private static final Logger log = LoggerFactory.getLogger(PageTwoController.class);

	public PageTwoController(App app) {
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
