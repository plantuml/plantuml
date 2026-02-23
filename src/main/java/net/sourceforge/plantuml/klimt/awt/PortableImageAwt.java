package net.sourceforge.plantuml.klimt.awt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

/**
 * Portable image abstraction that works both in standard Java (using
 * BufferedImage) and in TeaVM (using an in-memory pixel array with browser
 * canvas for PNG export).
 */
class PortableImageAwt implements PortableImage {

	static {
		assert PortableImage.TYPE_INT_RGB == BufferedImage.TYPE_INT_RGB;
		assert PortableImage.TYPE_INT_ARGB == BufferedImage.TYPE_INT_ARGB;
		assert PortableImage.TYPE_INT_ARGB_PRE == BufferedImage.TYPE_INT_ARGB_PRE;
	}

	private final BufferedImage image;

	public PortableImageAwt(int width, int height, int imageType) {
		this.image = new BufferedImage(width, height, imageType);
	}

	public PortableImageAwt(BufferedImage image) {
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

	public PortableImageAwt getSubimage(int x, int y, int width, int height) {
		return new PortableImageAwt(image.getSubimage(x, y, width, height));
	}

	public Graphics2D createGraphics() {
		return image.createGraphics();
	}

	public Graphics getGraphics() {
		return image.getGraphics();
	}

	public int getTransparency() {
		return image.getTransparency();
	}

	/**
	 * Converts this image to a PNG data URL (data:image/png;base64,...). In
	 * standard Java, uses ImageIO to encode the BufferedImage.
	 * 
	 * @return PNG data URL string
	 */
	public String toPngDataUrl() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", baos);
			byte[] bytes = baos.toByteArray();
			String base64 = Base64.getEncoder().encodeToString(bytes);
			return "data:image/png;base64," + base64;
		} catch (IOException e) {
			throw new RuntimeException("Failed to encode image as PNG", e);
		}
	}

	/**
	 * Returns all pixels as an ARGB int array (row-major order).
	 * 
	 * @return pixel array of size width * height
	 */
	public int[] getPixels() {
		int w = image.getWidth();
		int h = image.getHeight();
		return image.getRGB(0, 0, w, h, null, 0, w);
	}

	/**
	 * Scales this image by the given factor using the specified interpolation type.
	 * 
	 * @param scaleFactor       the scale factor (e.g., 2.0 for double size)
	 * @param interpolationType AffineTransformOp interpolation type
	 * @return a new scaled PortableImage
	 */
	public PortableImageAwt scale(double scaleFactor, int interpolationType) {
		final int w = (int) Math.round(image.getWidth() * scaleFactor);
		final int h = (int) Math.round(image.getHeight() * scaleFactor);
		final BufferedImage scaled = new BufferedImage(w, h, image.getType());
		final AffineTransform at = new AffineTransform();
		at.scale(scaleFactor, scaleFactor);
		final AffineTransformOp scaleOp = new AffineTransformOp(at, interpolationType);
		return new PortableImageAwt(scaleOp.filter(image, scaled));
	}

}
