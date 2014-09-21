package core;

import java.util.*;
import physics.PhysicsEngine;
import entities.Entity;
import terrain.*;
import utils.*;

public class World {

	public final String name;
	public final int seed = getSeed();
	/* Map order: x->y->z */ //TODO: Make private.
	public HashMap<String, Chunk> loadedChunks = new HashMap<String, Chunk>();
	public ArrayList<Entity> entityList = new ArrayList<Entity>();
	private PhysicsEngine physEngine = new PhysicsEngine(this);
	private Tile voidTile = new Tile(null, 0, 0, 0, 0, (byte)0);
	
	private static int getSeed() {
		// long seed = null;
		// if(seed == 0)
		int seed = (int) System.currentTimeMillis();
		// if not isinstance(seed, (int, long)):
		// seed = hash(seed)
		return seed;
	}
	
	public World(String name) {
		this.name = name;
		for (int x = 0; x < 1; x++)
			for (int z = 0; z < 1; z++)
				loadedChunks.put(FastMath.mergeCoords(x, 0, z), new Chunk(this, x, 0, z));
		for (Chunk  chunk : loadedChunks.values())
			chunk.updateTileEnvs();
	}
	
	protected void joinEntitiy(Entity entity) {
		entityList.add(entity);
	}
	
	public void tick() {

		//TODO: Consider making a separate list for entities that tick.
		for (Entity entity : entityList)
			entity.tick();
		
		physEngine.tick();
	}

	public Collection<Chunk> getSurroundingChunks(Entity povEntity, int limit) {
		// TODO: Consider eliminating the middle-man (Chunk array).
		/*int root3 = (limit+1)*2-1;
		Chunk[][][] chunks = new Chunk[root3][root3][root3];
		for (int x = 0; x < root3; x++)
			for (int y = 0; y < root3; y++)
				for (int z = 0; z < root3; z++)
					chunks[x][y][z] = chunkMap.get(x).get(y).get(z);*/
		return loadedChunks.values();
	}
	
	private Chunk getChunk(int x, int y, int z) {
		return loadedChunks.get(FastMath.mergeCoords(x, y, z));
	}

	public Tile getTile(int x, int y, int z) {
		Chunk chunk = getChunk(x>>Chunk.cubicBits, y>>Chunk.cubicBits, z>>Chunk.cubicBits);
		if (chunk != null)
			return chunk.getTile(x&((0x1<<Chunk.cubicBits)-1), y&((0x1<<Chunk.cubicBits)-1), z&((0x1<<Chunk.cubicBits)-1));
		//TODO: Think about void tile.
		return voidTile;
	}
	
	public Tile getTile(Vec3D vec) {
		return getTile((int)vec.x, (int)vec.y, (int)vec.z);
	}

	public boolean isVecInTile(Vec3D vec) {
		Tile tile = getTile(vec);
		return (tile!=null && tile.id>0);
	}
}
