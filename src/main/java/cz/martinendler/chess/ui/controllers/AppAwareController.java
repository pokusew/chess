package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;

public abstract class AppAwareController {

	protected final App app;

	public AppAwareController(App app) {
		this.app = app;
	}

}
