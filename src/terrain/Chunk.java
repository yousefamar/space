package terrain;

import core.SaveManager;
import core.World;
import utils.Noise;

public class Chunk {

	//public static final int cubicSize = 16;
	public static final int cubicBits = 4;
	
	public World world;
	public final int x, y, z;
	public Tile[][][] tiles = new Tile[0x1<<cubicBits][0x1<<cubicBits][0x1<<cubicBits];

	public Chunk(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		SaveManager.load(this);
	}

	public Tile getTile(int x, int y, int z) {
		try {
			return tiles[x][y][z];
		}catch (ArrayIndexOutOfBoundsException e) {
			return world.getTile((0x1<<cubicBits)*this.x + x, (0x1<<cubicBits)*this.y + y, (0x1<<cubicBits)*this.z + z);
		}
	}
	
	public void generate() {
		int cubicCunkSize = (0x1<<cubicBits);
		for (int x = 0; x < cubicCunkSize; x++)
			for (int y = 0; y < cubicCunkSize; y++)
				for (int z = 0; z < cubicCunkSize; z++)
					tiles[x][y][z] = new Tile(this, (Noise.triLerpaDerp(world.seed, this.x+(float)x/(cubicCunkSize-1), this.y+(float)y/(cubicCunkSize-1), this.z+(float)z/(cubicCunkSize-1))-(((float)y/(cubicCunkSize-1))-0.5))>0?1:0, x, y, z, (byte) 0);
	}
	
	public void updateTileEnvs() {
		for (int x = 0; x < (0x1<<cubicBits); x++)
			for (int y = 0; y < (0x1<<cubicBits); y++)
				for (int z = 0; z < (0x1<<cubicBits); z++)
					tiles[x][y][z].updateEnv();
	}
}
