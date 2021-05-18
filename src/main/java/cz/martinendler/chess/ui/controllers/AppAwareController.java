package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;

/**
 * Abstract controller that has a one-arg constructor that accepts the {@link App} instance
 *
 * @see cz.martinendler.chess.ControllerFactory
 */
public abstract class AppAwareController {

	/**
	 * App instance (injected by DI, using the one-arg {@link App} accepting constructor)
	 */
	protected final App app;

	public AppAwareController(App app) {
		this.app = app;
	}

}
