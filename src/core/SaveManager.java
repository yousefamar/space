package core;

import java.io.*;
import terrain.*;

public class SaveManager {
	
	private synchronized static void loadChunk(Chunk chunk) {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(ParadigmShiftGame.saveDir + "data" + File.separator
					+ "saves" + File.separator + chunk.world.name + File.separator + "map"
					+ File.separator + chunk.x + chunk.y + chunk.z + ".chunk"));
			for (int x = 0; x < (0x1<<Chunk.cubicBits); x++)
				for (int y = 0; y < (0x1<<Chunk.cubicBits); y++)
					for (int z = 0; z < (0x1<<Chunk.cubicBits); z++) {
						int saveData = in.readByte();
						chunk.tiles[x][y][z] = new Tile(chunk, saveData>>8, x, y, z, (byte) (saveData&0xFF));
					}
			in.close();
		} catch (IOException e) {
			chunk.generate();
			save(chunk);
		}
	}
	
	private synchronized static void saveChunk(Chunk chunk) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(ParadigmShiftGame.saveDir + "data" + File.separator
					+ "saves" + File.separator + chunk.world.name + File.separator + "map"
					+ File.separator + chunk.x + chunk.y + chunk.z + ".chunk"));
			for (int x = 0; x < (0x1<<Chunk.cubicBits); x++)
				for (int y = 0; y < (0x1<<Chunk.cubicBits); y++)
					for (int z = 0; z < (0x1<<Chunk.cubicBits); z++) {
						Tile tile = chunk.tiles[x][y][z];
						out.writeByte((tile.id<<8)|tile.metaData);
					}
			out.close();
		} catch (IOException e) {}
	}

	public static void save(final Chunk chunk) {
		new Thread(new Runnable() {
            public void run() { saveChunk(chunk); }
        }).start();
	}
	
	public static void load(final Chunk chunk) {
		//TODO: Figure out the best way to do this.
		//new Thread(new Runnable() {
        //    public void run() { 
            	loadChunk(chunk);
        //    }
        //}).start();
	}
}
