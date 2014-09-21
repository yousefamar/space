package gfx.gui;

import java.io.File;

import entities.Entity;
import gfx.DebugGUI;
import gfx.TextureHandler;

public abstract class GUI {
	
	//TODO: Organise GUI class tree and packages and create layer system. Account for window resize.
	public static GUI currentScreen = new DebugGUI();
	public static int textureID = TextureHandler.loadTexture("data"+File.separator+"textures"+File.separator+"nosignal.png");
	public static int skyboxID = TextureHandler.loadTexture("data"+File.separator+"textures"+File.separator+"skybox.png");

	
	public abstract void render(Entity povEntity);
	
	public void onTickGame(){}
}
