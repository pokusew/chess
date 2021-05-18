package cz.martinendler.chess.engine;

import org.jetbrains.annotations.NotNull;

/**
 * A player to be used in {@link Game}
 */
public class Player {

	private @NotNull String name;

	public Player(@NotNull String name) {
		this.name = name;
	}

	public @NotNull String getName() {
		return name;
	}

	public void setName(@NotNull String name) {
		this.name = name;
	}

}
