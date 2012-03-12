package terrain;

import java.io.*;

import utils.Noise;

public class Chunk {

	public static final int cubicSize = 16;

	public static final int testSeed = getSeed();
	public final int x;
	public final int y;
	public final int z;
	private byte tiles[][][] = new byte[cubicSize][cubicSize][cubicSize];

	private static int getSeed() {
		// long seed = null;
		// if(seed == 0)
		int seed = (int) System.currentTimeMillis();
		// if not isinstance(seed, (int, long)):
		// seed = hash(seed)
		return seed;
	}

	public Chunk(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.load();
	}

	public byte getTile(int x, int y, int z) {
		try {
			return tiles[x][y][z];
		} catch (Exception e) {
			// Tile is outside of chunk
			return 1;
		}
	}

	private void load() {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream("data"
					+ File.separator + "saves" + File.separator + "world1"
					+ File.separator + "map" + File.separator + x + y + z
					+ ".chunk"));
			for (int x = 0; x < Chunk.cubicSize; x++)
				for (int y = 0; y < Chunk.cubicSize; y++)
					for (int z = 0; z < Chunk.cubicSize; z++)
						tiles[x][y][z] = in.readByte();
			in.close();
		} catch (IOException e) {
			this.generate();
			this.save();
		}
	}

	private void generate() {
		for (int x = 0; x < Chunk.cubicSize; x++)
			for (int y = 0; y < Chunk.cubicSize; y++)
				for (int z = 0; z < Chunk.cubicSize; z++)
					tiles[x][y][z] = (byte) ((Noise.triLerpaDerp(testSeed, this.x+(float)x/15, this.y+(float)y/15, this.z+(float)z/15)-(((float)y/15)-0.5))>0?1:0);
	}

	private void save() {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					"data" + File.separator + "saves" + File.separator
							+ "world1" + File.separator + "map"
							+ File.separator + x + y + z + ".chunk"));
			for (int x = 0; x < Chunk.cubicSize; x++)
				for (int y = 0; y < Chunk.cubicSize; y++)
					for (int z = 0; z < Chunk.cubicSize; z++)
						out.writeByte(tiles[x][y][z]);
			out.close();
		} catch (IOException e) {
		}
	}
}
