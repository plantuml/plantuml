package net.sourceforge.plantuml.klimt.awt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// ::comment when JAVA8
import org.teavm.jso.JSBody;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.canvas.ImageData;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.typedarrays.Uint8ClampedArray;
// ::done

/**
 * Portable image abstraction that works both in standard Java (using
 * BufferedImage) and in TeaVM (using an in-memory pixel array with browser
 * canvas for PNG export).
 */
class PortableImageTeaVM implements PortableImage {
	// ::remove file when JAVA8

	private final int width;
	private final int height;
	private final int imageType;
	private final int[] pixels;

	PortableImageTeaVM(int width, int height, int imageType) {
		this.width = width;
		this.height = height;
		this.imageType = imageType;
		this.pixels = new int[width * height];
		// Initialize with default color based on type
		if (imageType == TYPE_INT_RGB) {
			// Default to white for RGB
			java.util.Arrays.fill(pixels, 0xFFFFFFFF);
		} else {
			// Default to transparent for ARGB
			java.util.Arrays.fill(pixels, 0x00000000);
		}
	}

	/**
	 * Creates a PortableImage from an existing pixel array. The array is copied,
	 * not referenced.
	 */
	PortableImageTeaVM(int width, int height, int imageType, int[] sourcePixels) {
		this.width = width;
		this.height = height;
		this.imageType = imageType;
		this.pixels = new int[width * height];
		System.arraycopy(sourcePixels, 0, this.pixels, 0, Math.min(sourcePixels.length, this.pixels.length));
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getRGB(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0;
		return pixels[y * width + x];
	}

	public void setRGB(int x, int y, int rgb) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		pixels[y * width + x] = rgb;
	}

	public int getType() {
		return imageType;
	}

	public PortableImage getSubimage(int x, int y, int w, int h) {
		// Clamp bounds
		if (x < 0) {
			w += x;
			x = 0;
		}
		if (y < 0) {
			h += y;
			y = 0;
		}
		if (x + w > width)
			w = width - x;
		if (y + h > height)
			h = height - y;
		if (w <= 0 || h <= 0)
			return new PortableImageTeaVM(1, 1, imageType);

		int[] subPixels = new int[w * h];
		for (int row = 0; row < h; row++) {
			int srcOffset = (y + row) * width + x;
			int dstOffset = row * w;
			System.arraycopy(pixels, srcOffset, subPixels, dstOffset, w);
		}
		return new PortableImageTeaVM(w, h, imageType, subPixels);
	}

	public int getTransparency() {
		// 1 = OPAQUE, 2 = BITMASK, 3 = TRANSLUCENT
		return (imageType == TYPE_INT_RGB) ? 1 : 3;
	}

	/**
	 * Returns all pixels as an ARGB int array (row-major order). Returns a copy to
	 * prevent external modification.
	 * 
	 * @return pixel array of size width * height
	 */
	public int[] getPixels() {
		int[] copy = new int[pixels.length];
		System.arraycopy(pixels, 0, copy, 0, pixels.length);
		return copy;
	}

	/**
	 * Converts this image to a PNG data URL using browser's canvas API. Creates an
	 * offscreen canvas, writes pixels via ImageData, and exports as PNG.
	 * 
	 * @return PNG data URL string (data:image/png;base64,...)
	 */
	public String toPngDataUrl() {
		HTMLDocument doc = HTMLDocument.current();
		HTMLCanvasElement canvas = (HTMLCanvasElement) doc.createElement("canvas");
		canvas.setWidth(width);
		canvas.setHeight(height);

		CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) canvas.getContext("2d");
		ImageData imageData = ctx.createImageData(width, height);
		Uint8ClampedArray data = imageData.getData();

		// Convert ARGB (0xAARRGGBB) to RGBA bytes
		int p = 0;
		for (int i = 0; i < pixels.length; i++) {
			int c = pixels[i];
			int a = (c >>> 24) & 0xFF;
			int r = (c >>> 16) & 0xFF;
			int g = (c >>> 8) & 0xFF;
			int b = c & 0xFF;

			data.set(p++, r);
			data.set(p++, g);
			data.set(p++, b);
			data.set(p++, a);
		}

		ctx.putImageData(imageData, 0, 0);
		return canvasToDataUrl(canvas);
	}

	@JSBody(params = { "canvas" }, script = "return canvas.toDataURL('image/png');")
	private static native String canvasToDataUrl(HTMLCanvasElement canvas);

	/**
	 * Scales this image by the given factor using the specified interpolation type.
	 * 
	 * @param scaleFactor       the scale factor (e.g., 2.0 for double size)
	 * @param interpolationType 1=nearest-neighbor, 2=bilinear
	 * @return a new scaled PortableImage
	 */
	public PortableImage scale(double scaleFactor, int interpolationType) {
		final int dstW = (int) Math.round(width * scaleFactor);
		final int dstH = (int) Math.round(height * scaleFactor);

		if (dstW <= 0 || dstH <= 0)
			return new PortableImageTeaVM(1, 1, imageType);

		final int[] dstPixels = new int[dstW * dstH];

		if (interpolationType == 2)
			scaleBilinear(pixels, width, height, dstPixels, dstW, dstH);
		else
			scaleNearestNeighbor(pixels, width, height, dstPixels, dstW, dstH);

		return new PortableImageTeaVM(dstW, dstH, imageType, dstPixels);
	}

	/**
	 * Nearest-neighbor scaling - fast but can be blocky.
	 */
	private static void scaleNearestNeighbor(int[] src, int srcW, int srcH, int[] dst, int dstW, int dstH) {
		for (int y = 0; y < dstH; y++) {
			final int srcY = y * srcH / dstH;
			for (int x = 0; x < dstW; x++) {
				final int srcX = x * srcW / dstW;
				dst[y * dstW + x] = src[srcY * srcW + srcX];
			}
		}
	}

	/**
	 * Bilinear interpolation scaling - smoother results.
	 */
	private static void scaleBilinear(int[] src, int srcW, int srcH, int[] dst, int dstW, int dstH) {
		final double xRatio = (double) (srcW - 1) / dstW;
		final double yRatio = (double) (srcH - 1) / dstH;

		for (int y = 0; y < dstH; y++) {
			final double srcYf = y * yRatio;
			final int y0 = (int) srcYf;
			final int y1 = Math.min(y0 + 1, srcH - 1);
			final double yFrac = srcYf - y0;

			for (int x = 0; x < dstW; x++) {
				final double srcXf = x * xRatio;
				final int x0 = (int) srcXf;
				final int x1 = Math.min(x0 + 1, srcW - 1);
				final double xFrac = srcXf - x0;

				// Get 4 neighboring pixels
				final int c00 = src[y0 * srcW + x0];
				final int c10 = src[y0 * srcW + x1];
				final int c01 = src[y1 * srcW + x0];
				final int c11 = src[y1 * srcW + x1];

				// Interpolate each channel
				final int a = interpolateChannel(c00, c10, c01, c11, xFrac, yFrac, 24);
				final int r = interpolateChannel(c00, c10, c01, c11, xFrac, yFrac, 16);
				final int g = interpolateChannel(c00, c10, c01, c11, xFrac, yFrac, 8);
				final int b = interpolateChannel(c00, c10, c01, c11, xFrac, yFrac, 0);

				dst[y * dstW + x] = (a << 24) | (r << 16) | (g << 8) | b;
			}
		}
	}

	/**
	 * Bilinear interpolation for a single color channel.
	 */
	private static int interpolateChannel(int c00, int c10, int c01, int c11, double xFrac, double yFrac, int shift) {
		final int v00 = (c00 >> shift) & 0xFF;
		final int v10 = (c10 >> shift) & 0xFF;
		final int v01 = (c01 >> shift) & 0xFF;
		final int v11 = (c11 >> shift) & 0xFF;

		final double top = v00 + xFrac * (v10 - v00);
		final double bottom = v01 + xFrac * (v11 - v01);
		final double result = top + yFrac * (bottom - top);

		return Math.min(255, Math.max(0, (int) Math.round(result)));
	}

	@Override
	public BufferedImage getBufferedImage() {
		throw new UnsupportedOperationException("TEAVM9834");
	}

	@Override
	public Graphics2D createGraphics() {
		throw new UnsupportedOperationException("TEAVM9834");
	}

	@Override
	public Graphics getGraphics() {
		throw new UnsupportedOperationException("TEAVM9834");
	}

}
