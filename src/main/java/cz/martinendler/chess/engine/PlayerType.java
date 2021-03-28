package cz.martinendler.chess.engine;

public enum PlayerType {

	WHITE(0),
	BLACK(1);

	public final int id;

	PlayerType(int id) {
		this.id = id;
	}

	// TODO: fromId / toId

}
