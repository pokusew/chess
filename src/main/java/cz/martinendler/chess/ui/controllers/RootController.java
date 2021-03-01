package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;
import cz.martinendler.chess.ui.routing.Route;
import cz.martinendler.chess.ui.routing.RouteName;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RootController extends AppAwareController implements Initializable {

	private static final Logger log = LoggerFactory.getLogger(RootController.class);

	@FXML
	private Button btnOne;

	@FXML
	private Button btnTwo;

	@FXML
	private AnchorPane page;

	private Route activeRoute;
	private final ArrayList<Route> routes;

	public RootController(App app) {
		super(app);
		routes = new ArrayList<>(2);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		routes.add(new Route(RouteName.ONE, app.loadFXML("view/one")));
		routes.add(new Route(RouteName.TWO, app.loadFXML("view/two")));

		log.info("initialize");

		btnOne.setOnAction(event -> {
			log.info("btnRecipes clicked");
			redirectTo(RouteName.ONE);
		});

		btnTwo.setOnAction(event -> {
			log.info("btnTags clicked");
			redirectTo(RouteName.TWO);
		});

		redirectTo(RouteName.ONE);

	}

	private void setView(Parent view) {
		page.getChildren().setAll(view);
	}

	private void redirectTo(RouteName routeName) {

		Route prevRoute = activeRoute;

		activeRoute = routes.get(routeName.index);

		if (activeRoute == prevRoute) {
			activeRoute.controller.reload();
			return;
		}

		activeRoute.controller.reload();
		setView(activeRoute.view);

		if (prevRoute != null) {
			prevRoute.controller.unload();
		}

	}

	// @FXML
	// private void redirectTo(ActionEvent event) {
	//
	// }

}
