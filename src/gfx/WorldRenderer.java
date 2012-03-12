package gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.input.Mouse;

import entities.*;
import terrain.Chunk;
import utils.Vec3DF;

public class WorldRenderer {

	public Camera camera = new Camera();
	public Entity povEntity = new Player();
	private Chunk testChunks[][] = new Chunk[5][5];

	public WorldRenderer() {
		for (int x = 0; x < testChunks.length; x++)
			for (int z = 0; z < testChunks[x].length; z++)
					testChunks[x][z] = new Chunk(x, 0, z);	
	}

	public void init(int windowWidth, int windowHeight) {
		updateCamera();
		Mouse.setGrabbed(true);
		glClearColor(0, 0, 0, 1);
		glViewport(0, 0, windowWidth, windowHeight);

		glShadeModel(GL_SMOOTH);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);// NICEST);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		// Frustum Culling
		gluPerspective(90.0F, ((float) windowWidth / (float) windowHeight),
				0.1F, 1000.0F);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	public void updateCamera() {
		Entity entity = povEntity;
		Vec3DF lookVec = new Vec3DF(0, 0, 1).rotateX(entity.pitch)
				.rotateY(entity.yaw).translate(entity.x, entity.y, entity.z);
		float radRoll = (float) Math.toRadians(entity.roll);
		float cosRoll = (float) Math.cos(radRoll);
		float sinRoll = (float) Math.sin(radRoll);
		this.camera.setVectors(entity.x, entity.y, entity.z, lookVec.x,
				lookVec.y, lookVec.z, -sinRoll, cosRoll, sinRoll);
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();

		gluLookAt(camera.eyeVec.x, camera.eyeVec.y, camera.eyeVec.z,
				camera.lookVec.x, camera.lookVec.y, camera.lookVec.z,
				camera.upVec.x, camera.upVec.y, camera.upVec.z);

		this.renderAxes();

		for (Chunk[] chunks : testChunks)
			for (Chunk chunk : chunks)
				this.renderChunk(chunk);
	}

	private void renderChunk(Chunk chunk) {
		for (int x = 0; x < Chunk.cubicSize; x++)
			for (int y = 0; y < Chunk.cubicSize; y++)
				for (int z = 0; z < Chunk.cubicSize; z++)
					if (chunk.getTile(x, y, z) != 0)
						this.renderTile(chunk.x * Chunk.cubicSize + x, chunk.y
								* Chunk.cubicSize + y, chunk.z
								* Chunk.cubicSize + z, scanEnv(chunk, x, y, z));
	}

	private boolean[] scanEnv(Chunk chunk, int x, int y, int z) {
		boolean up = chunk.getTile(x, y + 1, z) != 0;
		boolean down = chunk.getTile(x, y - 1, z) != 0;
		boolean north = chunk.getTile(x, y, z - 1) != 0;
		boolean south = chunk.getTile(x, y, z + 1) != 0;
		boolean east = chunk.getTile(x + 1, y, z) != 0;
		boolean west = chunk.getTile(x - 1, y, z) != 0;
		return new boolean[] { up, down, north, south, east, west };
	}

	private void renderTile(int x, int y, int z, boolean env[]) {
		glBegin(GL_QUADS);
		if (!env[0]) {
			glColor3f(255, 255, 0);
			glVertex3f(x, y + 1, z + 1);
			glVertex3f(x + 1, y + 1, z + 1);
			glVertex3f(x + 1, y + 1, z);
			glVertex3f(x, y + 1, z);
		}
		if (!env[1]) {
			glColor3f(0, 255, 0);
			glVertex3f(x, y, z);
			glVertex3f(x + 1, y, z);
			glVertex3f(x + 1, y, z + 1);
			glVertex3f(x, y, z + 1);
		}
		if (!env[2]) {
			glColor3f(0, 0, 255);
			glVertex3f(x + 1, y, z);
			glVertex3f(x, y, z);
			glVertex3f(x, y + 1, z);
			glVertex3f(x + 1, y + 1, z);
		}
		if (!env[3]) {
			glColor3f(0, 255, 255);
			glVertex3f(x, y, z + 1);
			glVertex3f(x + 1, y, z + 1);
			glVertex3f(x + 1, y + 1, z + 1);
			glVertex3f(x, y + 1, z + 1);
		}
		if (!env[4]) {
			glColor3f(255, 0, 255);
			glVertex3f(x + 1, y, z + 1);
			glVertex3f(x + 1, y, z);
			glVertex3f(x + 1, y + 1, z);
			glVertex3f(x + 1, y + 1, z + 1);
		}
		if (!env[5]) {
			glColor3f(255, 0, 0);
			glVertex3f(x, y, z);
			glVertex3f(x, y, z + 1);
			glVertex3f(x, y + 1, z + 1);
			glVertex3f(x, y + 1, z);
		}
		glEnd();
	}

	private void renderAxes() {
		glBegin(GL_LINES);

		// Draw x-axis line.
		glColor3f(1, 0, 0);
		glVertex3f(0, 0, 0);
		glVertex3f(100, 0, 0);

		// Draw y-axis line.
		glColor3f(0, 1, 0);
		glVertex3f(0, 0, 0);
		glVertex3f(0, 100, 0);

		// Draw z-axis line.
		glColor3f(0, 0, 1);
		glVertex3f(0, 0, 0);
		glVertex3f(0, 0, 100);

		// Draw chunk lines.
		glColor3f(1, 0, 1);
		for (int i = 0; i < 4; i++) {
			glVertex3f(i * 16 + 16, 0, 0);
			glVertex3f(i * 16 + 16, 0, 100);
			glVertex3f(0, 0, i * 16 + 16);
			glVertex3f(100, 0, i * 16 + 16);
		}

		glEnd();
	}
}
