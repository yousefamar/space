package terrain;

import java.util.HashMap;

public class TileType {

	private static HashMap<Integer, TileType> tileTypeMap = new HashMap<Integer, TileType>();

	static {
		tileTypeMap.put(0, new TileType(0x00));
		tileTypeMap.put(1, new TileType(0x01));
	}
	
	public byte id;
	
	public TileType(int id) {
		this.id = (byte) id;
	}
	
	public static TileType getTileType(int id) {
		return tileTypeMap.get(id);
	}
}
