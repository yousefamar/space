package gfx;

import static org.lwjgl.opengl.GL11.*;
import core.ParadigmShiftGame;
import utils.*;
import entities.Entity;
import gfx.gui.GUI;
import gfx.gui.TextRenderer;
import gfx.gui.TrueTypeFont;

public class DebugGUI extends GUI {

	private int FPS;
	private int FPSCount;
	private long lastFPS;
	private float usedRAM;

	public DebugGUI() {
		lastFPS = ParadigmShiftGame.getTime();
	}

	public void updateFPS() {
	    if (ParadigmShiftGame.getTime() - lastFPS > 1000) {
	    	FPS = FPSCount;
	        FPSCount = 0;
	        lastFPS += 1000;
	    }
	    FPSCount++;
	}
	
	public void onTickGame() {
		long availableRAM = Runtime.getRuntime().totalMemory();
		long freeRAM = Runtime.getRuntime().freeMemory();
		usedRAM = (((float)(availableRAM-freeRAM))/((float)availableRAM))*100;
	}
	
	@Override
	public void render(Entity povEntity) { //TODO: Player, not povEntity.
		updateFPS();
		renderTestImage();
		renderVector(new Vec2D(povEntity.speed.x, povEntity.speed.z),200,50);
		renderVector(new Vec2D(povEntity.speed.y, povEntity.speed.y),300,50);
		Vec3D lookVec = new Vec3D(0, 0, 1).rotateX(povEntity.pitch).rotateY(povEntity.yaw);
		renderVector(new Vec2D(-lookVec.x, lookVec.z),400,50);
		renderVector(new Vec2D(-lookVec.x, lookVec.y),500,50);
		renderVector(new Vec2D(1, 0).rotateZ(povEntity.pitch),600,50);
		glColor3f(0.24F, 0.7F, 0.44F);
		TextRenderer.renderString("FPS: "+FPS, 700, 50, 1, 1, TrueTypeFont.ALIGN_LEFT);
		TextRenderer.renderString("RAM usage: "+usedRAM+"%", 700, 80, 1, 1, TrueTypeFont.ALIGN_LEFT);
	}

	private void renderTestImage() {
		glEnable(GL_TEXTURE_2D);
		glColor3f(1, 1, 1);
		glBindTexture(GL_TEXTURE_2D, GUI.textureID);
		glBegin(GL_QUADS);
		glTexCoord2f(0, 1);
		glVertex3f(0, 100, 0);
		glTexCoord2f(1, 1);
		glVertex3f(100, 100, 0);
		glTexCoord2f(1, 0);
		glVertex3f(100, 0, 0);
		glTexCoord2f(0, 0);
		glVertex3f(0, 0, 0);
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}
	
	private void renderVector(Vec2D vec, int x, int y) {
		glBegin(GL_LINES);
		
		glColor3f(0, 1, 0);
		glVertex2f(20+x, 0+y);
		glVertex2f(20+x, 40+y);
		
		glColor3f(1, 0, 0);
		glVertex2f(0+x, 20+y);
		glVertex2f(40+x, 20+y);

		glColor3f(1, 1, 1);
		glVertex2f(20+x, 20+y);
		glVertex2f(vec.x*20+20+x, vec.y*-20+20+y);

		glEnd();
	}
}
