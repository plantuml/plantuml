package net.sourceforge.plantuml.ugraphic.fontsprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Hashtable;

public class FontSpriteSheet {

	static final int MIN_CHAR = 0x21;
	static final int MAX_CHAR = 0x7e;
	static final int NUM_CHARS = MAX_CHAR - MIN_CHAR + 1;

	private static final String ALL_CHARS;

	static {
		StringBuilder b = new StringBuilder();
		for (char c = MIN_CHAR; c <= MAX_CHAR; c++) {
			b.append(c);
		}
		ALL_CHARS = b.toString();
	}

	private final int ascent;
	private final int charWidth;
	private final int lineHeight;
	private final BufferedImage image;

	public FontSpriteSheet(BufferedImage image, int ascent, int lineHeight) {
		this.image = image;
		this.ascent = ascent;
		this.lineHeight = lineHeight;
		this.charWidth = image.getWidth() / NUM_CHARS;
	}

	public int getAscent() {
		return ascent;
	}

	public CharSequence getChars() {
		return ALL_CHARS;
	}

	public int getCharWidth() {
		return charWidth;
	}

	BufferedImage getImage() {
		return image;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public void drawString(Graphics g, String s, int x, int y) {
		final RecoloredImage recoloredImage = new RecoloredImage(this.image, g.getColor());
		for (char c : s.toCharArray()) {
			drawChar(recoloredImage, g, c, x, y);
			x += charWidth;
		}
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
	private void drawChar(BufferedImage image, Graphics g, char c, int x, int y) {
		if (c == ' ') {
			return;
		}

		if (c < MIN_CHAR || c > MAX_CHAR) {
			// TODO tofu
			throw new IllegalArgumentException("Char '" + c + "' not supported");
		}

		final int srcLeft = (c - MIN_CHAR) * charWidth;
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
					final int pixel = ((byte[]) inData)[0] & 0xFF;
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
