package terrain;

public class Tile {
	private Chunk chunk;
	private int chunkX, chunkY, chunkZ;
	public int id;
	public byte metaData;
	public boolean[] env = new boolean[6+12+8];
	
	public Tile(Chunk chunk, int id, int x, int y, int z, byte metaData) {
		this.chunk = chunk;
		this.id = id;
		this.chunkX = x;
		this.chunkY = y;
		this.chunkZ = z;
		this.metaData = metaData;
	}
	
	public void updateEnv() {
		env[0] = chunk.getTile(chunkX, chunkY+1, chunkZ).id != 0;	//up
		env[1] = chunk.getTile(chunkX, chunkY-1, chunkZ).id != 0;	//down
		env[2] = chunk.getTile(chunkX+1, chunkY, chunkZ).id != 0;	//left
		env[3] = chunk.getTile(chunkX-1, chunkY, chunkZ).id != 0;	//right
		env[4] = chunk.getTile(chunkX, chunkY, chunkZ+1).id != 0;	//front
		env[5] = chunk.getTile(chunkX, chunkY, chunkZ-1).id != 0;	//back
		
		env[6] = chunk.getTile(chunkX+1, chunkY-1, chunkZ).id != 0;
		env[7] = chunk.getTile(chunkX-1, chunkY-1, chunkZ).id != 0;
		env[8] = chunk.getTile(chunkX-1, chunkY+1, chunkZ).id != 0;
		env[9] = chunk.getTile(chunkX+1, chunkY+1, chunkZ).id != 0;
		
		env[10] = chunk.getTile(chunkX, chunkY-1, chunkZ+1).id != 0;
		env[11] = chunk.getTile(chunkX, chunkY-1, chunkZ-1).id != 0;
		env[12] = chunk.getTile(chunkX, chunkY+1, chunkZ-1).id != 0;
		env[13] = chunk.getTile(chunkX, chunkY+1, chunkZ+1).id != 0;
		
		env[14] = chunk.getTile(chunkX+1, chunkY, chunkZ+1).id != 0;
		env[15] = chunk.getTile(chunkX+1, chunkY, chunkZ-1).id != 0;
		env[16] = chunk.getTile(chunkX-1, chunkY, chunkZ-1).id != 0;
		env[17] = chunk.getTile(chunkX-1, chunkY, chunkZ+1).id != 0;
		
		env[18] = chunk.getTile(chunkX+1, chunkY-1, chunkZ-1).id != 0;
		env[19] = chunk.getTile(chunkX-1, chunkY-1, chunkZ-1).id != 0;
		env[20] = chunk.getTile(chunkX-1, chunkY+1, chunkZ-1).id != 0;
		env[21] = chunk.getTile(chunkX+1, chunkY+1, chunkZ-1).id != 0;
		env[22] = chunk.getTile(chunkX+1, chunkY-1, chunkZ+1).id != 0;
		env[23] = chunk.getTile(chunkX-1, chunkY-1, chunkZ+1).id != 0;
		env[24] = chunk.getTile(chunkX-1, chunkY+1, chunkZ+1).id != 0;
		env[25] = chunk.getTile(chunkX+1, chunkY+1, chunkZ+1).id != 0;
		//TODO: Test for opaqueness instead of ID.
	}
}
