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
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.typedarrays.Uint8ClampedArray;

import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.teavm.c.PngImageC;
import net.sourceforge.plantuml.utils.Base64Coder;
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

	/**
	 * Cached PNG data URL. When non-null, {@link #toPngDataUrl()} returns
	 * this directly instead of re-encoding from pixels.
	 */
	private final String pngDataUrl;

	PortableImageTeaVM(int width, int height, int imageType) {
		this.width = width;
		this.height = height;
		this.imageType = imageType;
		this.pixels = new int[width * height];
		this.pngDataUrl = null;
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
		this.pngDataUrl = null;
		System.arraycopy(sourcePixels, 0, this.pixels, 0, Math.min(sourcePixels.length, this.pixels.length));
	}

	/**
	 * Creates a PortableImage from raw PNG bytes. The width and height are
	 * parsed from the PNG IHDR header (bytes 16-23). The original bytes are
	 * stored as a data URL so that {@link #toPngDataUrl()} can return them
	 * directly without re-encoding.
	 * <p>
	 * Pixel data is not decoded; {@link #getRGB(int, int)} returns 0.
	 * This is acceptable because in TeaVM the image is rendered via the
	 * SVG {@code <image>} element using the data URL.
	 */
	PortableImageTeaVM(byte[] pngBytes, String base64) {
		if (pngBytes.length < 24)
			throw new IllegalArgumentException("Invalid PNG: too short (" + pngBytes.length + " bytes)");

		this.width = readInt32BE(pngBytes, 16);
		this.height = readInt32BE(pngBytes, 20);
		this.imageType = TYPE_INT_ARGB;
		this.pixels = new int[0];
		this.pngDataUrl = "data:image/png;base64," + base64;
	}

	private static int readInt32BE(byte[] data, int offset) {
		return ((data[offset] & 0xFF) << 24)
				| ((data[offset + 1] & 0xFF) << 16)
				| ((data[offset + 2] & 0xFF) << 8)
				| (data[offset + 3] & 0xFF);
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
		if (pngDataUrl != null)
			return pngDataUrl;

		if (TeaVM.isC())
			return "data:image/png;base64,"
					+ new String(Base64Coder.encode(PngImageC.encode(pixels, width, height, false)));

		HTMLDocument doc = HTMLDocument.current();
		HTMLCanvasElement canvas = (HTMLCanvasElement) doc.createElement("canvas");
		canvas.setWidth(width);
		canvas.setHeight(height);

		CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) canvas.getContext("2d");
		ImageData imageData = ctx.createImageData(width, height);

		// Convert ARGB (0xAARRGGBB) to RGBA bytes in a plain Java array first, then
		// hand the whole buffer to JavaScript in ONE call. Writing the pixels one
		// component at a time through Uint8ClampedArray.set(index, value) costs one
		// Java -> JS call per byte: cheap with the JS backend, but on the Wasm GC
		// backend every such call crosses the Wasm/JS boundary, i.e. millions of
		// crossings for a single large image.
		final byte[] rgba = new byte[pixels.length * 4];
		int p = 0;
		for (int i = 0; i < pixels.length; i++) {
			final int c = pixels[i];
			rgba[p++] = (byte) (c >>> 16); // r
			rgba[p++] = (byte) (c >>> 8); // g
			rgba[p++] = (byte) c; // b
			rgba[p++] = (byte) (c >>> 24); // a
		}
		copyRgba(imageData.getData(), Int8Array.copyFromJavaArray(rgba));

		ctx.putImageData(imageData, 0, 0);
		return canvasToDataUrl(canvas);
	}

	/**
	 * Bulk copy of RGBA bytes into the ImageData buffer. The Int8Array is
	 * reinterpreted as unsigned bytes (same bits), so (byte) 0xFF becomes 255.
	 */
	@JSBody(params = { "dst", "src" }, script = "dst.set(new Uint8ClampedArray(src.buffer, src.byteOffset, src.length));")
	private static native void copyRgba(Uint8ClampedArray dst, Int8Array src);

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
			scaleBilinear(pixels, width, height, dstPixels, dstW, dstH, scaleFactor);
		else
			scaleNearestNeighbor(pixels, width, height, dstPixels, dstW, dstH, scaleFactor);

		return new PortableImageTeaVM(dstW, dstH, imageType, dstPixels);
	}

	/**
	 * Nearest-neighbor scaling. It reproduces the result of
	 * <code>AffineTransformOp(TYPE_NEAREST_NEIGHBOR)</code>: the source pixel is
	 * taken at the center of each destination pixel, using 16.16 fixed-point
	 * coordinates. Destination pixels whose center falls outside of the source
	 * stay transparent.
	 */
	private static void scaleNearestNeighbor(int[] src, int srcW, int srcH, int[] dst, int dstW, int dstH,
			double scale) {
		final double inv = 1.0 / scale;
		final long dx = (long) Math.floor(inv * 65536.0);
		final long x0 = (long) Math.floor(0.5 * inv * 65536.0);
		for (int y = 0; y < dstH; y++) {
			if ((y + 0.5) / scale >= srcH)
				break;
			final int srcY = (int) (((long) Math.floor((y + 0.5) * inv * 65536.0)) >> 16);
			for (int x = 0; x < dstW; x++) {
				if ((x + 0.5) / scale >= srcW)
					break;
				final int srcX = (int) ((x0 + x * dx) >> 16);
				dst[y * dstW + x] = src[srcY * srcW + srcX];
			}
		}
	}

	/**
	 * Bilinear scaling. It reproduces (almost) bit for bit the result of the
	 * <code>AffineTransformOp(TYPE_BILINEAR)</code> used by the JVM version:
	 * samples are taken at the pixel centers with 16.16 fixed-point coordinates,
	 * the vertical interpolation is done first, each stage is rounded to 8 bits
	 * (the pixels on the border are truncated, not rounded), the abscissa is
	 * accumulated from the first column with a non-negative source position, and the channels
	 * (alpha included) are interpolated independently. Destination pixels whose
	 * center falls outside of the source stay transparent.
	 */
	private static void scaleBilinear(int[] src, int srcW, int srcH, int[] dst, int dstW, int dstH,
			double scale) {
		final double inv = 1.0 / scale;
		final long dx = (long) Math.floor(inv * 65536.0);
		// The abscissa is computed exactly on the first destination column whose
		// source position is not negative, then increased by a constant step.
		int xLeft = 0;
		while ((xLeft + 0.5) * inv - 0.5 < 0)
			xLeft++;
		final long xStart = (long) Math.floor(((xLeft + 0.5) * inv - 0.5) * 65536.0) - xLeft * dx;

		for (int y = 0; y < dstH; y++) {
			if ((y + 0.5) / scale >= srcH)
				break;
			final long yy = (long) Math.floor(((y + 0.5) * inv - 0.5) * 65536.0);
			final int y0 = (int) (yy >> 16);
			final int yf = (int) (yy & 0xFFFF);
			final int ya = Math.max(0, Math.min(srcH - 1, y0));
			final int yb = Math.max(0, Math.min(srcH - 1, y0 + 1));

			for (int x = 0; x < dstW; x++) {
				if ((x + 0.5) / scale >= srcW)
					break;
				final long xx = xStart + x * dx;
				final int x0 = (int) (xx >> 16);
				final int xf = (int) (xx & 0xFFFF);
				final int xa = Math.max(0, Math.min(srcW - 1, x0));
				final int xb = Math.max(0, Math.min(srcW - 1, x0 + 1));

				final boolean onBorder = x0 < 0 || y0 < 0 || x0 + 1 > srcW - 1 || y0 + 1 > srcH - 1;
				final int round = onBorder ? 0 : 32768;

				final int c00 = src[ya * srcW + xa];
				final int c10 = src[ya * srcW + xb];
				final int c01 = src[yb * srcW + xa];
				final int c11 = src[yb * srcW + xb];

				final int a = interpolateChannel(c00, c10, c01, c11, xf, yf, round, 24);
				final int r = interpolateChannel(c00, c10, c01, c11, xf, yf, round, 16);
				final int g = interpolateChannel(c00, c10, c01, c11, xf, yf, round, 8);
				final int b = interpolateChannel(c00, c10, c01, c11, xf, yf, round, 0);

				dst[y * dstW + x] = (a << 24) | (r << 16) | (g << 8) | b;
			}
		}
	}

	/**
	 * Bilinear interpolation for a single channel (fractions on 16 bits).
	 */
	private static int interpolateChannel(int c00, int c10, int c01, int c11, int xf, int yf, int round, int shift) {
		final int v00 = (c00 >>> shift) & 0xFF;
		final int v10 = (c10 >>> shift) & 0xFF;
		final int v01 = (c01 >>> shift) & 0xFF;
		final int v11 = (c11 >>> shift) & 0xFF;

		final int left = v00 + (int) ((((long) (v01 - v00)) * yf + round) >> 16);
		final int right = v10 + (int) ((((long) (v11 - v10)) * yf + round) >> 16);
		return left + (int) ((((long) (right - left)) * xf + round) >> 16);
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
