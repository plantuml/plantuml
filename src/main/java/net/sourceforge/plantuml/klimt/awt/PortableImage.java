package net.sourceforge.plantuml.klimt.awt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PortableImage {

	public static final int TYPE_INT_ARGB = BufferedImage.TYPE_INT_ARGB;
	public static final int TYPE_INT_ARGB_PRE = BufferedImage.TYPE_INT_ARGB_PRE;
	public static final int TYPE_INT_RGB = BufferedImage.TYPE_INT_RGB;

	private final BufferedImage image;

	public PortableImage(int width, int height, int imageType) {
		this.image = new BufferedImage(width, height, imageType);
	}

	public PortableImage(BufferedImage image) {
		this.image = image;
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public int getRGB(int x, int y) {
		return image.getRGB(x, y);
	}

	public void setRGB(int x, int y, int rgb) {
		image.setRGB(x, y, rgb);
	}

	public int getType() {
		return image.getType();
	}

	public BufferedImage getBufferedImage() {
		return image;
	}

	public PortableImage getSubimage(int x, int y, int width, int height) {
		return new PortableImage(image.getSubimage(x, y, width, height));
	}

	public Graphics2D createGraphics() {
		return image.createGraphics();
	}

	public int getTransparency() {
		return image.getTransparency();
	}

	public Graphics getGraphics() {
		return image.getGraphics();
	}

}
