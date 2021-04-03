package cz.martinendler.chess.engine;

public enum PlayerType {

	WHITE(0),
	BLACK(1);

	private static final PlayerType[] idToPlayerTypeMap = new PlayerType[]{
		WHITE,
		BLACK,
	};

	public final int id;

	PlayerType(int id) {
		this.id = id;
	}

	public static PlayerType fromId(int id) {
		return idToPlayerTypeMap[id];
	}

	public static int getTotalCount() {
		return idToPlayerTypeMap.length;
	}

}
