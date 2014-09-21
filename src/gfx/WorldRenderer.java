package gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import java.nio.*;
import core.GameCanvas;
import core.World;
import entities.*;
import gfx.gui.GUI;
import terrain.*;
import utils.*;

public class WorldRenderer {

	private World world;
	private Entity povEntity;
	public Camera camera = new Camera();
	private float fogColor[] = {1.0f, 1.0f, 1.0f, 1.0f};

	public WorldRenderer(World world, Entity povEntity) {
		this.world = world;
		this.povEntity = povEntity;
	}

	public void updateCamera() {
		Vec3D lookVec = new Vec3D(0, 0, 1).rotateX(povEntity.pitch).rotateY(povEntity.yaw).translate(povEntity.position.x, povEntity.position.y, povEntity.position.z);
		float sinRoll = FastMath.sin(povEntity.roll);
		this.camera.setVectors(povEntity.position.x, povEntity.position.y, povEntity.position.z,
				lookVec.x, lookVec.y, lookVec.z, -sinRoll, FastMath.cos(povEntity.roll), sinRoll);
	}

	public void renderWorld() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		gluLookAt(camera.eyeVec.x, camera.eyeVec.y, camera.eyeVec.z,
				camera.lookVec.x, camera.lookVec.y, camera.lookVec.z,
				camera.upVec.x, camera.upVec.y, camera.upVec.z);

		renderFog();
		renderSkybox();
		renderAxes();

		//TODO: Cut off after a certain distance.
		for(Entity entity : world.entityList) {
			Model model = Model.modelMap.get(entity.model);
			//TODO: Create default model or similar.
			if(model != null)
				renderModel(model,entity.position.x,entity.position.y,entity.position.z,entity.yaw,entity.pitch,entity.roll);
			if (entity instanceof Tangible)
				renderAABB(((Tangible)entity));
		}
		
		for (Chunk chunk : world.getSurroundingChunks(povEntity, 1))
			if (chunk != null)
				renderChunkVoxel(chunk);

		/* Render GUI */
		//TODO: Clean this up.
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, GameCanvas.windowWidth, GameCanvas.windowHeight, 0, 0, 1);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		GUI.currentScreen.render(povEntity);
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
	}

	private void renderFog() {
		// TODO: Consider option for toggling fog modes?
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		temp.asFloatBuffer().put(fogColor).flip();
		glFogi(GL_FOG_MODE, GL_LINEAR);
		glFog(GL_FOG_COLOR, temp.asFloatBuffer());
		glFogf(GL_FOG_DENSITY, 0.2f);
		glHint(GL_FOG_HINT, GL_NICEST);
		glFogf(GL_FOG_START, 5F);
		glFogf(GL_FOG_END, 100F);
		glEnable(GL_FOG);
	}

	private void renderModel(Model model, float x, float y, float z, int yaw, int pitch, int roll) {
		glPushMatrix();
		glTranslated(x, y, z);
		//TODO: Be careful, rotation is CCW.
		glRotated(-yaw,0,1,0);
		glRotated(-pitch,1,0,0);
		glRotated(-roll,0,0,1);
		glColor4f(1,1,1,1);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, GUI.textureID);
		for (TexturedPolygon face : model.faces) {
			glBegin(GL_POLYGON);
			for (int i=0; i<face.vertexIDs.size(); i++) {
				glTexCoord2f(model.texCoords.get(face.texCoordIDs.get(i)).x, model.texCoords.get(face.texCoordIDs.get(i)).y);
				glVertex3f(model.vertices.get(face.vertexIDs.get(i)).x, model.vertices.get(face.vertexIDs.get(i)).y, model.vertices.get(face.vertexIDs.get(i)).z);
			}
			glEnd();
		}
		glDisable(GL_TEXTURE_2D);
		glPopMatrix();
	}

	private void renderChunkVoxel(Chunk chunk) {
		for (int x = (chunk.x<<Chunk.cubicBits); x < (chunk.x<<Chunk.cubicBits)+(0x1<<Chunk.cubicBits); x++)
			for (int y = (chunk.y<<Chunk.cubicBits); y < (chunk.y<<Chunk.cubicBits)+(0x1<<Chunk.cubicBits); y++)
				for (int z = (chunk.z<<Chunk.cubicBits); z < (chunk.z<<Chunk.cubicBits)+(0x1<<Chunk.cubicBits); z++) {
					Tile tile = world.getTile(x, y, z); 
					if (tile.id != 0)
						renderTile(tile, x, y ,z);
				}
	}

	private void renderTile(Tile tile, int x, int y, int z) {
		if (tile.env[0] && tile.env[1] && tile.env[2] && tile.env[3] && tile.env[4] && tile.env[5])
			return;
		
		/* Vertices mapped in conventional OpenGL order: x,y,z starting bottom left and going CCW. */
		Vec3D[] vertices = new Vec3D[]{
				new Vec3D(x+0.75F, y+0.25F, z+0.25F),
				new Vec3D(x+0.25F, y+0.25F, z+0.25F),
				new Vec3D(x+0.25F, y+0.75F, z+0.25F),
				new Vec3D(x+0.75F, y+0.75F, z+0.25F),
				new Vec3D(x+0.75F, y+0.25F, z+0.75F),
				new Vec3D(x+0.25F, y+0.25F, z+0.75F),
				new Vec3D(x+0.25F, y+0.75F, z+0.75F),
				new Vec3D(x+0.75F, y+0.75F, z+0.75F)
		};
		
		if (tile.env[0] || tile.env[8] || tile.env[12] || tile.env[20]) {
			vertices[2].y += 0.25F;
		} if (tile.env[0] || tile.env[9] || tile.env[12] || tile.env[21]) {
			vertices[3].y += 0.25F;
		} if (tile.env[0] || tile.env[8] || tile.env[13] || tile.env[24]) {
			vertices[6].y += 0.25F;
		} if (tile.env[0] || tile.env[9] || tile.env[13] || tile.env[25]) {
			vertices[7].y += 0.25F;
		}
		
		if (tile.env[1] || tile.env[6] || tile.env[11] || tile.env[18]) {
			vertices[0].y -= 0.25F;
		} if (tile.env[1] || tile.env[7] || tile.env[11] || tile.env[19]) {
			vertices[1].y -= 0.25F;
		} if (tile.env[1] || tile.env[6] || tile.env[10] || tile.env[22]) {
			vertices[4].y -= 0.25F;
		} if (tile.env[1] || tile.env[7] || tile.env[10] || tile.env[23]) {
			vertices[5].y -= 0.25F;
		}
		
		if (tile.env[2] || tile.env[6] || tile.env[15] || tile.env[18]) {
			vertices[0].x += 0.25F;
		} if (tile.env[2] || tile.env[9] || tile.env[15] || tile.env[21]) {
			vertices[3].x += 0.25F;
		} if (tile.env[2] || tile.env[6] || tile.env[14] || tile.env[22]) {
			vertices[4].x += 0.25F;
		} if (tile.env[2] || tile.env[9] || tile.env[14] || tile.env[25]) {
			vertices[7].x += 0.25F;
		}
		
		if (tile.env[3] || tile.env[7] || tile.env[16] || tile.env[19]) {
			vertices[1].x -= 0.25F;
		} if (tile.env[3] || tile.env[8] || tile.env[16] || tile.env[20]) {
			vertices[2].x -= 0.25F;
		} if (tile.env[3] || tile.env[7] || tile.env[17] || tile.env[23]) { 
			vertices[5].x -= 0.25F;
		} if (tile.env[3] || tile.env[8] || tile.env[17] || tile.env[24]) {
			vertices[6].x -= 0.25F;
		}
		
		if (tile.env[4] || tile.env[10] || tile.env[14] || tile.env[22]) {
			vertices[4].z += 0.25F;
		} if (tile.env[4] || tile.env[10] || tile.env[17] || tile.env[23]) {
			vertices[5].z += 0.25F;
		} if (tile.env[4] || tile.env[13] || tile.env[17] || tile.env[24]) {
			vertices[6].z += 0.25F;
		} if (tile.env[4] || tile.env[13] || tile.env[14] || tile.env[25]) {
			vertices[7].z += 0.25F;
		}
		
		if (tile.env[5] || tile.env[11] || tile.env[15] || tile.env[18]) {
			vertices[0].z -= 0.25F;
		} if (tile.env[5] || tile.env[11] || tile.env[16] || tile.env[19]) {
			vertices[1].z -= 0.25F;
		} if (tile.env[5] || tile.env[12] || tile.env[16] || tile.env[20]) {
			vertices[2].z -= 0.25F;
		} if (tile.env[5] || tile.env[12] || tile.env[15] || tile.env[21]) {
			vertices[3].z -= 0.25F;
		}
		
		glBegin(GL_QUADS);
		if (!tile.env[0]) {
			glColor3f(255, 255, 0);
			glVertex3f(vertices[2].x, vertices[2].y, vertices[2].z);
			glVertex3f(vertices[6].x, vertices[6].y, vertices[6].z);
			glVertex3f(vertices[7].x, vertices[7].y, vertices[7].z);
			glVertex3f(vertices[3].x, vertices[3].y, vertices[3].z);
		} if (!tile.env[1]) {
			glColor3f(0, 255, 0);
			glVertex3f(vertices[0].x, vertices[0].y, vertices[0].z);
			glVertex3f(vertices[4].x, vertices[4].y, vertices[4].z);
			glVertex3f(vertices[5].x, vertices[5].y, vertices[5].z);
			glVertex3f(vertices[1].x, vertices[1].y, vertices[1].z);
		} if (!tile.env[2]) {
			glColor3f(0, 0, 255);
			glVertex3f(vertices[4].x, vertices[4].y, vertices[4].z);
			glVertex3f(vertices[0].x, vertices[0].y, vertices[0].z);
			glVertex3f(vertices[3].x, vertices[3].y, vertices[3].z);
			glVertex3f(vertices[7].x, vertices[7].y, vertices[7].z);
		} if (!tile.env[3]) {
			glColor3f(0, 255, 255);
			glVertex3f(vertices[1].x, vertices[1].y, vertices[1].z);
			glVertex3f(vertices[5].x, vertices[5].y, vertices[5].z);
			glVertex3f(vertices[6].x, vertices[6].y, vertices[6].z);
			glVertex3f(vertices[2].x, vertices[2].y, vertices[2].z);
		} if (!tile.env[4]) {
			glColor3f(255, 0, 255);
			glVertex3f(vertices[4].x, vertices[4].y, vertices[4].z);
			glVertex3f(vertices[5].x, vertices[5].y, vertices[5].z);
			glVertex3f(vertices[6].x, vertices[6].y, vertices[6].z);
			glVertex3f(vertices[7].x, vertices[7].y, vertices[7].z);
		} if (!tile.env[5]) {
			glColor3f(255, 0, 0);
			glVertex3f(vertices[0].x, vertices[0].y, vertices[0].z);
			glVertex3f(vertices[1].x, vertices[1].y, vertices[1].z);
			glVertex3f(vertices[2].x, vertices[2].y, vertices[2].z);
			glVertex3f(vertices[3].x, vertices[3].y, vertices[3].z);
		}
		glEnd();
		
		/*
		glBegin(GL_QUADS);
		if (!tile.env[0]) {
			glColor3f(1, 1, 0);
			glVertex3f(x, y + 1, z + 1);
			glVertex3f(x + 1, y + 1, z + 1);
			glVertex3f(x + 1, y + 1, z);
			glVertex3f(x, y + 1, z);
		} if (!tile.env[1]) {
			glColor3f(0, 1, 0);
			glVertex3f(x, y, z);
			glVertex3f(x + 1, y, z);
			glVertex3f(x + 1, y, z + 1);
			glVertex3f(x, y, z + 1);
		} if (!tile.env[2]) {
			glColor3f(1, 0, 1);
			glVertex3f(x + 1, y, z + 1);
			glVertex3f(x + 1, y, z);
			glVertex3f(x + 1, y + 1, z);
			glVertex3f(x + 1, y + 1, z + 1);
		} if (!tile.env[3]) {
			glColor3f(1, 0, 0);
			glVertex3f(x, y, z);
			glVertex3f(x, y, z + 1);
			glVertex3f(x, y + 1, z + 1);
			glVertex3f(x, y + 1, z);
		} if (!tile.env[4]) {
			glColor3f(0, 1, 1);
			glVertex3f(x, y, z + 1);
			glVertex3f(x + 1, y, z + 1);
			glVertex3f(x + 1, y + 1, z + 1);
			glVertex3f(x, y + 1, z + 1);
		} if (!tile.env[5]) {
			glColor3f(0, 0, 1);
			glVertex3f(x + 1, y, z);
			glVertex3f(x, y, z);
			glVertex3f(x, y + 1, z);
			glVertex3f(x + 1, y + 1, z);
		}
		glEnd();
		*/
	}

	private void renderAABB(Tangible entity) {
		AABB aabb = (AABB) entity.getBoundingBox();
		float minX = aabb.centre.x - aabb.halfWidth, minY = aabb.centre.y - aabb.halfHeight, minZ = aabb.centre.z - aabb.halfWidth;
		float maxX = aabb.centre.x + aabb.halfWidth, maxY = aabb.centre.y + aabb.halfHeight, maxZ = aabb.centre.z + aabb.halfWidth;
		glColor3f(1, 0.62F, 0);
		glBegin(GL_LINE_LOOP);
		glVertex3f(minX, minY, minZ);
		glVertex3f(minX, minY, maxZ);
		glVertex3f(minX, maxY, maxZ);
		glVertex3f(minX, maxY, minZ);
		glEnd();
		glBegin(GL_LINE_LOOP);
		glVertex3f(maxX, minY, minZ);
		glVertex3f(maxX, minY, maxZ);
		glVertex3f(maxX, maxY, maxZ);
		glVertex3f(maxX, maxY, minZ);
		glEnd();
		glBegin(GL_LINES);
		glVertex3f(minX, minY, minZ);
		glVertex3f(maxX, minY, minZ);
		glVertex3f(minX, minY, maxZ);
		glVertex3f(maxX, minY, maxZ);
		glVertex3f(minX, maxY, maxZ);
		glVertex3f(maxX, maxY, maxZ);
		glVertex3f(minX, maxY, minZ);
		glVertex3f(maxX, maxY, minZ);
		glColor3f(0, 0, 1);
		Vec3D cen = ((Entity)entity).position;
		Vec3D lookVec = new Vec3D(0, 0, 2).rotateX(((Entity)entity).pitch).rotateY(((Entity)entity).yaw).translate(cen);
		glVertex3f(cen.x, cen.y, cen.z);
		glVertex3f(lookVec.x, lookVec.y, lookVec.z);
		glEnd();
	}
	
	private void renderOBB(Tangible entity) {
		OBB obb = (OBB) entity.getBoundingBox();
		Vec3D[] vertices = obb.getVertices();
		glColor3f(1, 0.62F, 0);
		glBegin(GL_LINE_LOOP);
		glVertex3f(vertices[0].x, vertices[0].y, vertices[0].z);
		glVertex3f(vertices[1].x, vertices[1].y, vertices[1].z);
		glVertex3f(vertices[2].x, vertices[2].y, vertices[2].z);
		glVertex3f(vertices[3].x, vertices[3].y, vertices[3].z);
		glEnd();
		glBegin(GL_LINE_LOOP);
		glVertex3f(vertices[4].x, vertices[4].y, vertices[4].z);
		glVertex3f(vertices[5].x, vertices[5].y, vertices[5].z);
		glVertex3f(vertices[6].x, vertices[6].y, vertices[6].z);
		glVertex3f(vertices[7].x, vertices[7].y, vertices[7].z);
		glEnd();
		glBegin(GL_LINES);
		glVertex3f(vertices[0].x, vertices[0].y, vertices[0].z);
		glVertex3f(vertices[4].x, vertices[4].y, vertices[4].z);
		glVertex3f(vertices[1].x, vertices[1].y, vertices[1].z);
		glVertex3f(vertices[5].x, vertices[5].y, vertices[5].z);
		glVertex3f(vertices[2].x, vertices[2].y, vertices[2].z);
		glVertex3f(vertices[6].x, vertices[6].y, vertices[6].z);
		glVertex3f(vertices[3].x, vertices[3].y, vertices[3].z);
		glVertex3f(vertices[7].x, vertices[7].y, vertices[7].z);
		glEnd();
		glBegin(GL_LINES);
		glColor3f(0, 0, 1);
		Vec3D cen = obb.centre;
		Vec3D lookVec = new Vec3D(0, 0, 2).rotateX(((Entity)entity).pitch).rotateY(((Entity)entity).yaw).translate(cen);
		glVertex3f(cen.x, cen.y, cen.z);
		glVertex3f(lookVec.x, lookVec.y, lookVec.z);
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
	
	private void renderSkybox() {
		// Store the current matrix
		glPushMatrix();

		// Reset and transform the matrix.
		glLoadIdentity();
		glRotated(-povEntity.pitch,1,0,0);
		glRotated(povEntity.yaw,0,1,0);
		glRotated(povEntity.roll,0,0,1);
		
		// Enable/Disable features
		glPushAttrib(GL_ENABLE_BIT);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
		glDisable(GL_BLEND);

		// Just in case we set all vertices to white.
		glColor4f(1, 1, 1, 1);

		float f13 = 1F/3F, f23 = 2F/3F;
		// Render the front quad
	    glBindTexture(GL_TEXTURE_2D, GUI.skyboxID);
		glBegin(GL_QUADS);
		glTexCoord2f(0.25F, f23);
		glVertex3f(0.5f, -0.5f, -0.5f);
		glTexCoord2f(0.5F, f23);
		glVertex3f(-0.5f, -0.5f, -0.5f);
		glTexCoord2f(0.5F, f13);
		glVertex3f(-0.5f, 0.5f, -0.5f);
		glTexCoord2f(0.25F, f13);
		glVertex3f(0.5f, 0.5f, -0.5f);
		glEnd();

	    // Render the left quad
	    glBindTexture(GL_TEXTURE_2D, GUI.skyboxID);
	    glBegin(GL_QUADS);
		glTexCoord2f(0, f23);
		glVertex3f(0.5f, -0.5f, 0.5f);
		glTexCoord2f(0.25F, f23);
		glVertex3f(0.5f, -0.5f, -0.5f);
		glTexCoord2f(0.25F, f13);
		glVertex3f(0.5f, 0.5f, -0.5f);
		glTexCoord2f(0, f13);
		glVertex3f(0.5f, 0.5f, 0.5f);
		glEnd();
	 
		// Render the back quad
		glBindTexture(GL_TEXTURE_2D, GUI.skyboxID);
		glBegin(GL_QUADS);
		glTexCoord2f(0.75F, f23);
		glVertex3f(-0.5f, -0.5f, 0.5f);
		glTexCoord2f(1, f23);
		glVertex3f(0.5f, -0.5f, 0.5f);
		glTexCoord2f(1, f13);
		glVertex3f(0.5f, 0.5f, 0.5f);
		glTexCoord2f(0.75F, f13);
		glVertex3f(-0.5f, 0.5f, 0.5f);
		glEnd();

		// Render the right quad
		glBindTexture(GL_TEXTURE_2D, GUI.skyboxID);
		glBegin(GL_QUADS);
		glTexCoord2f(0.5F, f23);
		glVertex3f(-0.5f, -0.5f, -0.5f);
		glTexCoord2f(0.75F, f23);
		glVertex3f(-0.5f, -0.5f, 0.5f);
		glTexCoord2f(0.75F, f13);
		glVertex3f(-0.5f, 0.5f, 0.5f);
		glTexCoord2f(0.5F, f13);
		glVertex3f(-0.5f, 0.5f, -0.5f);
		glEnd();

		// Render the top quad
		glBindTexture(GL_TEXTURE_2D, GUI.skyboxID);
		glBegin(GL_QUADS);
		glTexCoord2f(0.5F, f13);
		glVertex3f(-0.5f, 0.5f, -0.5f);
		glTexCoord2f(0.5F, 0);
		glVertex3f(-0.5f, 0.5f, 0.5f);
		glTexCoord2f(0.25F, 0);
		glVertex3f(0.5f, 0.5f, 0.5f);
		glTexCoord2f(0.25F, f13);
		glVertex3f(0.5f, 0.5f, -0.5f);
		glEnd();

		// Render the bottom quad
		glBindTexture(GL_TEXTURE_2D, GUI.skyboxID);
		glBegin(GL_QUADS);
		glTexCoord2f(0.5F, f23);
		glVertex3f(-0.5f, -0.5f, -0.5f);
		glTexCoord2f(0.5F, 1);
		glVertex3f(-0.5f, -0.5f, 0.5f);
		glTexCoord2f(0.25F, 1);
		glVertex3f(0.5f, -0.5f, 0.5f);
		glTexCoord2f(0.25F, f23);
		glVertex3f(0.5f, -0.5f, -0.5f);
		glEnd();

		// Restore enable bits and matrix
		glPopAttrib();
		glPopMatrix();
	}
}
