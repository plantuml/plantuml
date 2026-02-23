package net.sourceforge.plantuml.klimt.awt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Portable image abstraction that works both in standard Java (using
 * BufferedImage) and in TeaVM (using an in-memory pixel array with browser
 * canvas for PNG export).
 */
public interface PortableImage {

//	public static final int TYPE_INT_RGB = BufferedImage.TYPE_INT_RGB;
//	public static final int TYPE_INT_ARGB = BufferedImage.TYPE_INT_ARGB;
//	public static final int TYPE_INT_ARGB_PRE = BufferedImage.TYPE_INT_ARGB_PRE;

	public static final int TYPE_INT_RGB = 1;
	public static final int TYPE_INT_ARGB = 2;
	public static final int TYPE_INT_ARGB_PRE = 3;

	public BufferedImage getBufferedImage();

	public int getWidth();

	public int getHeight();

	public Graphics2D createGraphics();

	public Graphics getGraphics();

	public int getRGB(int x, int y);

	public void setRGB(int x, int y, int rgb);

	public String toPngDataUrl();

	public int getTransparency();

	public PortableImage getSubimage(int x, int y, int width, int height);

	public PortableImage scale(double scaleFactor, int interpolationType);

}
