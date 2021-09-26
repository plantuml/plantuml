package net.sourceforge.plantuml.ugraphic.fontsprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class FontSpriteSheet {

	private CharSequence chars;
	private final int ascent;
	private final int charWidth;
	private final int lineHeight;
	private final BufferedImage image;
	private final char maxChar;
	private final char minChar;

	public FontSpriteSheet(BufferedImage image, int ascent, int lineHeight, char maxChar, char minChar, int charWidth) {
		this.image = image;
		this.ascent = ascent;
		this.lineHeight = lineHeight;
		this.maxChar = maxChar;
		this.minChar = minChar;
		this.charWidth = charWidth;
	}

	public int getAscent() {
		return ascent;
	}

	public CharSequence getChars() {
		if (chars == null) {
			StringBuilder b = new StringBuilder();
			for (char c = minChar; c <= maxChar; c++) {
				b.append(c);
			}
			this.chars = b.toString();
		}
		return chars;
	}

	public int getCharWidth() {
		return charWidth;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public void drawString(Graphics2D g, String s, int x, int y) {
		final RecoloredImage recoloredImage = new RecoloredImage(this.image, g.getColor());
		for (char c : s.toCharArray()) {
			drawChar(recoloredImage, g, c, x, y);
			x += charWidth;
		}
	}

	public void writePNG(File file) throws IOException {
		ImageIO.write(image, "png", file);
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
	private void drawChar(BufferedImage image, Graphics2D g, char c, int x, int y) {
		if (c == ' ') {
			return;
		}

		if (c < minChar || c > maxChar) {
			// TODO tofu
			throw new IllegalArgumentException("Char '" + c + "' not supported");
		}

		final int srcLeft = (c - minChar) * charWidth;
		final int srcRight = srcLeft + charWidth;
		final int srcTop = 0;
		final int srcBottom = image.getHeight();

		final int destLeft = x;
		final int destRight = destLeft + charWidth;
		final int destTop = y - ascent;
		final int destBottom = destTop + image.getHeight();

		g.drawImage(
				image,
				destLeft, destTop, destRight, destBottom,
				srcLeft, srcTop, srcRight, srcBottom,
				null
		);
	}

	/**
	 * Used for colouring the chars.  No idea if this is the most efficient way.
	 */
	private static class RecoloredImage extends BufferedImage {
		private final int red;
		private final int green;
		private final int blue;
		private final int alpha;

		public RecoloredImage(BufferedImage image, Color color) {
			super(image.getColorModel(), image.getRaster(), image.isAlphaPremultiplied(), cloneProperties(image));
			this.red = color.getRed();
			this.green = color.getGreen();
			this.blue = color.getBlue();
			this.alpha = color.getAlpha();
		}

		@Override
		public int getType() {
			return TYPE_CUSTOM;
		}

		@Override
		public ColorModel getColorModel() {
			return new ColorModel(8) {

				@Override
				public int getRGB(Object inData) {
					final int pixel = ((byte[]) inData)[0];
					final int alpha0 = ((pixel * alpha) / 255) & 0xFF;
					return (alpha0 << 24)
							| (red << 16)
							| (green << 8)
							| (blue << 0);
				}

				@Override
				public int getRed(int pixel) {
					throw new UnsupportedOperationException();
				}

				@Override
				public int getGreen(int pixel) {
					throw new UnsupportedOperationException();
				}

				@Override
				public int getBlue(int pixel) {
					throw new UnsupportedOperationException();
				}

				@Override
				public int getAlpha(int pixel) {
					throw new UnsupportedOperationException();
				}
			};
		}
	}

	private static Hashtable<String, Object> cloneProperties(BufferedImage image) {
		final Hashtable<String, Object> properties = new Hashtable<>();
		final String[] names = image.getPropertyNames();
		if (names != null) {
			for (String name : names) {
				properties.put(name, image.getProperty(name));
			}
		}
		return properties;
	}
}
