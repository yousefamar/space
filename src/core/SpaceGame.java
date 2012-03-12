package core;

import gfx.WorldRenderer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class SpaceGame {

	private final static int screenSize[] = {800, 500};
	private WorldRenderer window = new WorldRenderer();

	public SpaceGame() {
		window.init(screenSize[0], screenSize[1]);
	}

	private void tick() {
		this.input();
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			window.povEntity.moveRel(window.camera, 1F, 0, 0);
			window.updateCamera();
		} if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			window.povEntity.moveRel(window.camera, -1F, 0, 0);
			window.updateCamera();
		} if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			window.povEntity.moveRel(window.camera, 0, 0, 1F);
			window.updateCamera();
		} if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			window.povEntity.moveRel(window.camera, 0, 0, -1F);
			window.updateCamera();
		} if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			window.povEntity.moveRel(window.camera, 0, 1F, 0);
			window.updateCamera();
		} if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			window.povEntity.moveRel(window.camera, 0, -1F, 0);
			window.updateCamera();
		}
		//window.updateCamera()
		//TODO: Standardise this.
	}

	private void input() {
		while (Mouse.next()) {
			window.povEntity.setRotation(window.povEntity.yaw + Mouse.getDX()/ 2, window.povEntity.pitch + Mouse.getDY() / 2);
			window.updateCamera();
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					System.out.println("Quitting");
					Display.destroy();
					System.exit(0);
				}
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					System.out.println("Esc key released");
				}
			}
		}
	}

	private void render() {
		window.render();
	}

	public static void main(String args[]) {
		try {
			Display.setDisplayMode(new DisplayMode(screenSize[0], screenSize[1]));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		SpaceGame game = new SpaceGame();
		while (!Display.isCloseRequested()) {
			game.tick();
			game.render();
			Display.update();
		}
		Display.destroy();
	}
}
