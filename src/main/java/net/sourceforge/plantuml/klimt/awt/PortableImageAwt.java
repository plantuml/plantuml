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
		if (scaleFactor == 1)
			return this;

		final int w = (int) Math.round(image.getWidth() * scaleFactor);
		final int h = (int) Math.round(image.getHeight() * scaleFactor);

		// AffineTransformOp runs native code (medialib) whose pixels depend on the
		// platform: on macOS aarch64, a few pixels along the border come out off by
		// one, so the PNG embedded in an SVG is not the same as on Linux (issue
		// #3045). MlibAffine is a pure Java port of that code, which gives the very
		// same pixels as medialib on Linux. It is only used for the source types
		// where this has been checked: a premultiplied or a gray source (linear
		// color space) is interpolated differently by AffineTransformOp.
		if (w > 0 && h > 0 && isPortedType(interpolationType)) {
			final int srcW = image.getWidth();
			final int srcH = image.getHeight();
			final int[] src = image.getRGB(0, 0, srcW, srcH, null, 0, srcW);
			final int[] dst = new int[w * h];
			if (interpolationType == AffineTransformOp.TYPE_BILINEAR)
				MlibAffine.bilinear(src, srcW, srcH, dst, w, h, scaleFactor);
			else
				MlibAffine.nearest(src, srcW, srcH, dst, w, h, scaleFactor);

			final BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			scaled.setRGB(0, 0, w, h, dst, 0, w);
			return new PortableImageAwt(scaled);
		}

		// Always use TYPE_INT_ARGB as destination: copying image.getType() may yield a
		// type without alpha (e.g. RGB color type 2) or TYPE_CUSTOM, which produces a
		// blank image after AffineTransformOp and breaks the GraalVM native build
		// (missing ComponentColorModel JNI/reflection configuration).
		final BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		final AffineTransform at = new AffineTransform();
		at.scale(scaleFactor, scaleFactor);
		final AffineTransformOp scaleOp = new AffineTransformOp(at, interpolationType);
		return new PortableImageAwt(scaleOp.filter(image, scaled));
	}

	private boolean isPortedType(int interpolationType) {
		if (interpolationType != AffineTransformOp.TYPE_BILINEAR
				&& interpolationType != AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
			return false;

		// MlibAffine draws nothing for such sizes: leave them to AffineTransformOp
		if (image.getWidth() >= (1 << 15) || image.getHeight() >= (1 << 15))
			return false;

		switch (image.getType()) {
		case BufferedImage.TYPE_INT_ARGB:
		case BufferedImage.TYPE_INT_RGB:
		case BufferedImage.TYPE_4BYTE_ABGR:
		case BufferedImage.TYPE_3BYTE_BGR:
		case BufferedImage.TYPE_BYTE_INDEXED:
		case BufferedImage.TYPE_BYTE_BINARY:
			return true;
		}
		return false;
	}

}
