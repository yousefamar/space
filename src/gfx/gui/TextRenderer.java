package gfx.gui;

import java.awt.Font;

public class TextRenderer {
	private static TrueTypeFont font = new TrueTypeFont(new Font("Arial", Font.PLAIN, 24), false);

	public static void init() {
	}

	public static void renderString(String string, int x, int y, int scaleX, int scaleY, int format) {
		font.drawString(x, y, string, scaleX, scaleY, format);
	}
}
