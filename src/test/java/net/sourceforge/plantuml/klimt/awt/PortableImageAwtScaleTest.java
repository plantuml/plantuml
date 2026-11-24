package net.sourceforge.plantuml.klimt.awt;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.Random;
import java.util.zip.CRC32;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Scaling an image must give the same pixels on every platform (issue #3045:
 * the sprites of the stdlib, scaled with the font, were off by one on a few
 * pixels on macOS aarch64, because AffineTransformOp runs native code).
 */
class PortableImageAwtScaleTest {

	/** An ARGB image looking like a stdlib sprite: opaque, with some alpha. */
	private static BufferedImage sample(int type, int width, int height, long seed) {
		final Random rnd = new Random(seed);
		final BufferedImage result = new BufferedImage(width, height, type);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				final int alpha = rnd.nextInt(3) == 0 ? rnd.nextInt(256) : 255;
				result.setRGB(x, y, (alpha << 24) | rnd.nextInt(1 << 24));
			}
		return result;
	}

	private static long crc(PortableImage image) {
		final CRC32 crc = new CRC32();
		for (int y = 0; y < image.getHeight(); y++)
			for (int x = 0; x < image.getWidth(); x++) {
				final int argb = image.getRGB(x, y);
				crc.update(argb >>> 24);
				crc.update(argb >>> 16);
				crc.update(argb >>> 8);
				crc.update(argb);
			}
		return crc.getValue();
	}

	// The expected values are the pixels of AffineTransformOp on Linux. 12/13 and
	// 14/13 are the factors a sprite gets with a font of 12 and 14 (Azure stdlib).
	@ParameterizedTest
	@CsvSource({ //
			"70, 70, 0.923076923076923, 2, 65, 65, ea1657eb", //
			"70, 70, 1.0769230769230769, 2, 75, 75, 4155e24c", //
			"64, 48, 0.4375, 2, 28, 21, f21db1ce", //
			"70, 70, 0.923076923076923, 1, 65, 65, f5f2c872", //
			"33, 17, 2.5, 1, 83, 43, 7489c676", //
	})
	void scaled_pixels_do_not_depend_on_the_platform(int width, int height, double scale, int interpolation,
			int expectedWidth, int expectedHeight, String expectedCrc) {
		final PortableImageAwt image = new PortableImageAwt(
				sample(BufferedImage.TYPE_INT_ARGB, width, height, 3045));
		final PortableImage scaled = image.scale(scale, interpolation);
		assertEquals(expectedWidth, scaled.getWidth());
		assertEquals(expectedHeight, scaled.getHeight());
		assertEquals(expectedCrc, Long.toHexString(crc(scaled)));
	}

	/**
	 * MlibAffine is a port of the native medialib: on Linux, where the references
	 * were made, it must give exactly the pixels of AffineTransformOp. On other
	 * platforms the native code may differ (that is the point of the port), so
	 * nothing is checked there.
	 */
	@Test
	void port_gives_the_pixels_of_medialib_on_linux() {
		assumeTrue(System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("linux"));
		final int[] types = { BufferedImage.TYPE_INT_ARGB, BufferedImage.TYPE_INT_RGB,
				BufferedImage.TYPE_4BYTE_ABGR, BufferedImage.TYPE_3BYTE_BGR, BufferedImage.TYPE_BYTE_INDEXED,
				BufferedImage.TYPE_BYTE_BINARY };
		final Random rnd = new Random(42);
		for (int i = 0; i < 300; i++) {
			final int type = types[rnd.nextInt(types.length)];
			final int width = 1 + rnd.nextInt(100);
			final int height = 1 + rnd.nextInt(100);
			final double scale = 0.05 + 4 * rnd.nextDouble();
			final int interpolation = rnd.nextBoolean() ? AffineTransformOp.TYPE_BILINEAR
					: AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
			final int w = (int) Math.round(width * scale);
			final int h = (int) Math.round(height * scale);
			if (w <= 0 || h <= 0)
				continue;

			final BufferedImage source = sample(type, width, height, i);
			final BufferedImage expected = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			final AffineTransform at = new AffineTransform();
			at.scale(scale, scale);
			new AffineTransformOp(at, interpolation).filter(source, expected);

			final PortableImageAwt actual = new PortableImageAwt(source).scale(scale, interpolation);
			final String message = "type " + type + ", " + width + "x" + height + ", scale " + scale
					+ ", interpolation " + interpolation;
			assertArrayEquals(expected.getRGB(0, 0, w, h, null, 0, w), actual.getPixels(), message);
		}
	}

}
