package cz.martinendler.chess.ui.controllers;

import cz.martinendler.chess.App;

/**
 * A controller that has a cleanup function
 */
public interface LifecycleAwareController {

	/**
	 * Called by parent controller of the {@link App} when this controller should stop
	 * In this method, all cleanup should be done.
	 */
	void stop();

}
