package com.ctreber.acearth.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.ctreber.acearth.renderer.RenderTarget;

/**
 * <p>
 * Swing compatible drawing surface for images and graphics.
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public class PixelCanvas implements RenderTarget {
	final private int fImageWidth;
	final private int fImageHeight;
	final private BufferedImage fEarthImage2;

	/**
	 * <p>
	 * Construct a canvas of the specified size.
	 * 
	 * @param pWidth
	 *            Width
	 * @param pHeight
	 *            Height
	 */
	public PixelCanvas(int pWidth, int pHeight) {
		fImageWidth = pWidth;
		fImageHeight = pHeight;
		fEarthImage2 = new BufferedImage(fImageWidth, fImageHeight, BufferedImage.TYPE_INT_RGB);
	}
	
	public Graphics2D getGraphics2D() {
		return fEarthImage2.createGraphics();
	}

	public void setPixel(int pX, int pY, int pA, int pR, int pG, int pB) {
		setPixel(pX, pY, new Color(pR, pG, pB, pA));
	}

	public void setPixel(int pX, int pY, Color pColor) {
		fEarthImage2.setRGB(pX, pY, pColor.getRGB());
	}

	public int getImageWidth() {
		return fImageWidth;
	}

	public int getImageHeight() {
		return fImageHeight;
	}

	public boolean saveToImage(String pFileName, String pFormat) throws IOException {
		return ImageIO.write(fEarthImage2, pFormat, new File(pFileName));
	}
	
	public void saveToImage(OutputStream os) throws IOException {
		ImageIO.write(fEarthImage2, "png", os);
	}

}
