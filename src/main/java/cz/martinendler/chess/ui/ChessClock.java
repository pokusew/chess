package cz.martinendler.chess.ui;

import cz.martinendler.chess.engine.Side;
import javafx.application.Platform;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.time.Clock;

import static java.lang.Thread.sleep;


public class ChessClock implements Runnable {

	private final Text whiteClockNode;
	private final Text blackClockNode;

	private int whiteTime;
	private int blackTime;
	private boolean stopped;
	private Side sideToMove;

	public ChessClock(@NotNull Text whiteClockNode, @NotNull Text blackClockNode) {
		this.whiteClockNode = whiteClockNode;
		this.blackClockNode = blackClockNode;
		// use Platform.runLater(() -> {});
	}

	@Override
	public void run() {

		while (!stopped) {

			//

			// System.currentTimeMillis()
			//
			// try {
			// 	sleep(1000);
			// } catch (InterruptedException e) {
			//
			// }



		}

	}

	/**
	 * Sets time in minutes and seconds to both labels.
	 *
	 * @param time Time to be set (in seconds).
	 */
	public void setWhiteAndBlackTime(int time) {

		this.whiteTime = time;
		this.blackTime = time;

		// update UI

	}

	public void stop() {
		this.stopped = true;
	}

}
