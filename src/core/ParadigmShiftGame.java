package core;

import java.io.File;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;

import terrain.Chunk;
import entities.*;
import gfx.Model;

public class ParadigmShiftGame {

	public static final String saveDir = System.getProperty("user.home")+File.separator+".paradigmshift"+File.separator;
	
	private long lastFPS = getTime();
	private GameCanvas gameCanvas;
	public World world = new World("world1");
	public Player player = new Player(world);
	
	public ParadigmShiftGame(DisplayMode fullDM) {
		gameCanvas = new GameCanvas(this, fullDM.getWidth(), fullDM.getHeight());
		Model.loadModels();
		world.joinEntitiy(player);
		world.joinEntitiy(new Box(world));
	}
	
	public void tickFrame() {
		if (getTime() - lastFPS > 50) {
			tickGame();
			lastFPS += 50;
		}
		gameCanvas.render();
	}

	private void tickGame() {
		this.input();
		float fwd = 0, strafe = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			fwd++;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			fwd--;
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			player.jump();
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			player.decend();
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			strafe++;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			strafe--;
		if(fwd!=0||strafe!=0)
			player.moveRel(fwd, strafe);
		
		world.tick();
		
		gameCanvas.onTickGame();
	}

	private void input() {
		while (Mouse.next())
			player.setRotation(player.yaw + Mouse.getDX()/2, player.pitch + Mouse.getDY()/2);

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					System.out.println("Quitting");
					Display.destroy();
					System.exit(0);
				} else if (Keyboard.getEventKey() == Keyboard.KEY_R) {
					System.out.println("Refreshing Tile Envs.");
					for (Chunk  chunk : world.loadedChunks.values())
						chunk.updateTileEnvs();
				}
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					System.out.println("Esc key released");
				}
			}
		}
	}
	
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static void main(String args[]) {
		DisplayMode fullDM = Display.getDesktopDisplayMode();
		try {
			Display.setDisplayMode(fullDM);
			Display.setFullscreen(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		ParadigmShiftGame game = new ParadigmShiftGame(fullDM);
		while (!Display.isCloseRequested()) {
			game.tickFrame();
			Display.sync(100);
			Display.update();
		}
		Display.destroy();
	}
}