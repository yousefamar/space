package core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import gfx.*;
import gfx.gui.GUI;

import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameCanvas {

	public static int windowWidth, windowHeight;
	private WorldRenderer window;

	public GameCanvas(ParadigmShiftGame game, int windowWidth, int windowHeight) {
		window = new WorldRenderer(game.world, game.player);
		GameCanvas.windowWidth = windowWidth;
		GameCanvas.windowHeight = windowHeight;
		window.updateCamera();
		Mouse.setGrabbed(true);
		glClearColor(0, 0, 0, 1);
		glViewport(0, 0, windowWidth, windowHeight);

		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);// NICEST);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		// Frustum Culling
		gluPerspective(90F, ((float) windowWidth / (float) windowHeight), 0.1F, 1000.0F);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	public void render() {
		window.renderWorld();
	}

	public void onTickGame() {
		window.updateCamera();
		GUI.currentScreen.onTickGame();
	}
}
