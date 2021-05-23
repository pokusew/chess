package cz.martinendler.chess.ui;

import cz.martinendler.chess.engine.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

/**
 * A simple chess clock implementation
 */
public class ChessClock {

	/**
	 * Update interval in milliseconds
	 */
	private static final int UPDATE_INTERVAL = 1000;

	public static final long DISABLED = 0L;
	public static final long TEN_MINUTES = 600000L;

	private static final Logger log = LoggerFactory.getLogger(ChessClock.class);

	private volatile boolean threadStopped;
	private @Nullable Thread thread;

	private final long[] remainingTimeOf;
	private @Nullable Side sideToMove;
	private long intervalStart;

	private @NotNull Consumer<@NotNull Runnable> notificationsRunner;
	private @Nullable BiConsumer<@NotNull Side, @NotNull Long> onRemainingTimeChange;
	private @Nullable Consumer<@NotNull Side> onTimeElapsedChange;

	public ChessClock(long initialTime, @NotNull Consumer<@NotNull Runnable> notificationsRunner) {

		remainingTimeOf = new long[Side.values().length];
		setRemainingTime(initialTime);

		// these variables are modified when clock countdown is started
		sideToMove = null;

		this.notificationsRunner = notificationsRunner;

		// start background thread
		// that will update the remaining time (with UPDATE_INTERVAL period)
		startThread();

	}

	public ChessClock(long initialTime) {
		this(initialTime, Runnable::run);
	}

	public @Nullable BiConsumer<@NotNull Side, @NotNull Long> getOnRemainingTimeChange() {
		return onRemainingTimeChange;
	}

	public void setOnRemainingTimeChange(@Nullable BiConsumer<@NotNull Side, @NotNull Long> onRemainingTimeChange) {
		this.onRemainingTimeChange = onRemainingTimeChange;
	}

	public @Nullable Consumer<@NotNull Side> getOnTimeElapsedChange() {
		return onTimeElapsedChange;
	}

	public void setOnTimeElapsedChange(@Nullable Consumer<@NotNull Side> onTimeElapsedChange) {
		this.onTimeElapsedChange = onTimeElapsedChange;
	}

	public @NotNull Consumer<@NotNull Runnable> getNotificationsRunner() {
		return notificationsRunner;
	}

	public void setNotificationsRunner(@NotNull Consumer<@NotNull Runnable> notificationsRunner) {
		this.notificationsRunner = notificationsRunner;
	}

	/**
	 * Sets the remaining time of both sides to the same value
	 *
	 * @param time the remaining time in milliseconds
	 */
	public synchronized void setRemainingTime(final long time) {

		log.info("setRemainingTime: time={}", time);

		if (time < 0) {
			throw new IllegalArgumentException("The time must be greater than 0.");
		}

		Arrays.fill(remainingTimeOf, time);

		if (onRemainingTimeChange != null) {
			notificationsRunner.accept(() -> {
				onRemainingTimeChange.accept(Side.WHITE, time);
				onRemainingTimeChange.accept(Side.BLACK, time);
			});
		}

	}

	/**
	 * Sets the remaining time of the given side
	 *
	 * @param side the side
	 * @param time the remaining time in milliseconds
	 */
	public synchronized void setRemainingTime(final @NotNull Side side, final long time) {

		log.info("setRemainingTime: side={} time={}", side, time);

		if (time < 0) {
			throw new IllegalArgumentException("The time must be greater than 0.");
		}

		remainingTimeOf[side.ordinal()] = time;

		if (onRemainingTimeChange != null) {
			notificationsRunner.accept(() -> {
				onRemainingTimeChange.accept(side, time);
			});
		}

	}

	/**
	 * Updates the remaining time of {@link #sideToMove} (if it is not {@code null})
	 *
	 * @param currentTime current system timestamp in milliseconds
	 */
	private synchronized void updateRemainingTime(long currentTime) {

		// ignore as no countdown is running
		if (sideToMove == null) {
			return;
		}

		final Side side = sideToMove;

		long timeSpent = currentTime - intervalStart;

		// log.info("{} spent {} ms", side, timeSpent);

		long remainingTime = remainingTimeOf[side.ordinal()] - timeSpent;

		if (remainingTime < 0L) {
			remainingTime = 0L;
			sideToMove = null;
		}

		boolean remainingTimeChanged = remainingTimeOf[side.ordinal()] != remainingTime;

		// save the updated remaining time
		remainingTimeOf[side.ordinal()] = remainingTime;

		// renew interval
		intervalStart = currentTime;

		// handle notifications
		if (remainingTimeChanged && (onRemainingTimeChange != null || onTimeElapsedChange != null)) {
			final long remainingTimeFinal = remainingTime;
			notificationsRunner.accept(() -> {

				if (onRemainingTimeChange != null) {
					onRemainingTimeChange.accept(side, remainingTimeFinal);
				}

				if (remainingTimeFinal == 0L && onTimeElapsedChange != null) {
					onTimeElapsedChange.accept(side);
				}

			});
		}

	}

	/**
	 * Starts counting the the remaining time of the given side
	 * <p>
	 * This should be called immediately after the side becomes the side to move
	 * in the {@link cz.martinendler.chess.engine.Game}.
	 *
	 * @param side the side
	 */
	public synchronized void start(@Nullable Side side) {

		long currentTime = System.currentTimeMillis();

		updateRemainingTime(currentTime);

		if (side == null) {
			sideToMove = null;
			return;
		}

		long remainingTime = remainingTimeOf[side.ordinal()];

		if (remainingTime == 0L) {
			sideToMove = null;
			return;
		}

		sideToMove = side;
		intervalStart = currentTime;

	}

	/**
	 * Stops counting
	 */
	public void stop() {
		start(null);
	}

	/**
	 * The code that runs in {@link #thread}
	 */
	private void run() {

		log.info("running");

		while (!threadStopped) {

			long beforeSleep = System.currentTimeMillis();

			updateRemainingTime(beforeSleep);

			// log.info("before sleep = {}", beforeSleep);

			try {
				// noinspection BusyWait
				sleep(UPDATE_INTERVAL);
			} catch (InterruptedException e) {
				log.info("interrupted sleep");
			}

			if (threadStopped) {
				return;
			}

			long afterSleep = System.currentTimeMillis();

			updateRemainingTime(afterSleep);

			// log.info("after sleep diff = {}", afterSleep - beforeSleep);

		}

	}

	/**
	 * Starts the background thread {@link #thread}
	 * that continuously updates the remaining time of the current {@link #sideToMove}
	 * (with {@link #UPDATE_INTERVAL} period)
	 */
	private void startThread() {
		log.info("startThread");
		threadStopped = false;
		thread = new Thread(this::run, "ChessClock Thread");
		thread.start();
	}

	/**
	 * Destroys the chess clock (its underlying thread)
	 */
	public void destroy() {
		threadStopped = true;
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}

}
