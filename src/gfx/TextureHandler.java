package gfx;

import static org.lwjgl.opengl.GL11.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;

public class TextureHandler {

	public static int loadTexture(String path){
		//TODO: Make a no texture found texture;
		BufferedImage img = null;
		try { img = ImageIO.read(new File(path)); } catch (IOException e) { e.printStackTrace(); }
		
		int[] pixels = new int[img.getWidth() * img.getHeight()];
        img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

        ByteBuffer imgBuf = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);
        
        for(int y = 0; y < img.getHeight(); y++) {
            for(int x = 0; x < img.getWidth(); x++) {
                int pixel = pixels[y * img.getWidth() + x];
                imgBuf.put((byte) ((pixel >> 16) & 0xFF));	//R
                imgBuf.put((byte) ((pixel >> 8) & 0xFF));	//G
                imgBuf.put((byte) (pixel & 0xFF));			//B
                imgBuf.put((byte) ((pixel >> 24) & 0xFF));	//A
            }
        }
        imgBuf.flip();

		IntBuffer intBuf = BufferUtils.createIntBuffer(1);
		glGenTextures(intBuf);
		intBuf.rewind();
		int texID = intBuf.get(0);
		glBindTexture(GL_TEXTURE_2D, texID);
		glPixelStorei(GL_UNPACK_ALIGNMENT,1);
		//glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		//glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		//glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		//glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		//glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		//glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imgBuf);
		return texID;
	}
}
