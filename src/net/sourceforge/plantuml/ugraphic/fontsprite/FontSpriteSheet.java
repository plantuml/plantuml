package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.lang.Integer.parseInt;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static net.sourceforge.plantuml.png.MetadataTag.findMetadataValue;
import static net.sourceforge.plantuml.utils.ImageIOUtils.createImageReader;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Hashtable;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import net.sourceforge.plantuml.png.PngIOMetadata;

public class FontSpriteSheet {

	public static FontSpriteSheet readFromPNG(Path path) throws IOException {
		try (InputStream is = newInputStream(path)) {
			return new FontSpriteSheet(is);
		}
	}

	private static final int MIN_CHAR = 0x21;
	private static final int MAX_CHAR = 0x7e;

	private final int advance;
	private final int ascent;
	private final BufferedImage image;
	private final int lineHeight;
	private final String name;
	private final int pointSize;
	private final int spriteWidth;
	private final int xOffset;

	FontSpriteSheet(BufferedImage image, FontMetrics fontMetrics, int advance, int ascent, int spriteWidth, int xOffset) {
		this.advance = advance;
		this.ascent = ascent;
		this.image = image;
		this.lineHeight = fontMetrics.getHeight();
		this.name = fontMetrics.getFont().getFontName();
		this.pointSize = fontMetrics.getFont().getSize();
		this.spriteWidth = spriteWidth;
		this.xOffset = xOffset;
	}

	public FontSpriteSheet(InputStream in) throws IOException {
		try (ImageInputStream iis = ImageIO.createImageInputStream(in)) {
			final IIOImage iioImage = createImageReader(iis).readAll(0, null);
			advance = getMetadataInt(iioImage, TAG_ADVANCE);
			ascent = getMetadataInt(iioImage, TAG_ASCENT);
			image = (BufferedImage) iioImage.getRenderedImage();
			lineHeight = getMetadataInt(iioImage, TAG_LINE_HEIGHT);
			name = getMetadataString(iioImage, TAG_NAME);
			pointSize = getMetadataInt(iioImage, TAG_POINT_SIZE);
			spriteWidth = getMetadataInt(iioImage, TAG_SPRITE_WIDTH);
			xOffset = getMetadataInt(iioImage, TAG_X_OFFSET);
		}
	}

	public int getAdvance() {
		return advance;
	}

	public int getAscent() {
		return ascent;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public String getName() {
		return name;
	}

	public int getPointSize() {
		return pointSize;
	}

	public String getPreferredFilename() {
		return getName().replace(' ', '-') + "-" + getPointSize() + ".png";
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public void drawString(Graphics g, String s, int x, int y) {
		for (char c : s.toCharArray()) {
			drawChar(g, c, x, y);
			x += advance;
		}
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
	private void drawChar(Graphics g, char c, int x, int y) {
		if (c == ' ') {
			return;
		}

		final int srcLeft = leftPosInSheet(c);
		final int srcRight = srcLeft + spriteWidth;
		final int srcTop = 0;
		final int srcBottom = image.getHeight();

		final int destLeft = x - xOffset;
		final int destRight = destLeft + spriteWidth;
		final int destTop = y - ascent;
		final int destBottom = destTop + image.getHeight();

		g.drawImage(
				new RecoloredImage(image, g.getColor()),
				destLeft, destTop, destRight, destBottom,
				srcLeft, srcTop, srcRight, srcBottom,
				null
		);
	}

	private int leftPosInSheet(char c) {
		if (c < MIN_CHAR || c > MAX_CHAR) {
			return 0;  // tofu
		} else {
			return (c - MIN_CHAR + 1) * spriteWidth;
		}
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

	//
	// PNG Read / Write
	//

	private static final String TAG_ADVANCE = "PlantUml-FontSprite-Advance";

	private static final String TAG_ASCENT = "PlantUml-FontSprite-Ascent";

	private static final String TAG_NAME = "PlantUml-FontSprite-Name";

	private static final String TAG_SPRITE_WIDTH = "PlantUml-FontSprite-SpriteWidth";

	private static final String TAG_POINT_SIZE = "PlantUml-FontSprite-PointSize";

	private static final String TAG_LINE_HEIGHT = "PlantUml-FontSprite-LineHeight";
	
	private static final String TAG_X_OFFSET = "PlantUml-FontSprite-XOffset";

	public void writeAsPNG(Path path) throws IOException {
		try (OutputStream os = newOutputStream(path)) {
			writeAsPNG(os);
		}
	}

	public void writeAsPNG(OutputStream out) throws IOException {
		final PngIOMetadata writer = new PngIOMetadata();
		writer.addText(TAG_ADVANCE, String.valueOf(advance));
		writer.addText(TAG_ASCENT, String.valueOf(ascent));
		writer.addText(TAG_LINE_HEIGHT, String.valueOf(lineHeight));
		writer.addText(TAG_NAME, String.valueOf(name));
		writer.addText(TAG_POINT_SIZE, String.valueOf(pointSize));
		writer.addText(TAG_SPRITE_WIDTH, String.valueOf(spriteWidth));
		writer.addText(TAG_X_OFFSET, String.valueOf(xOffset));
		writer.write(image, out);
	}

	private static int getMetadataInt(IIOImage image, String tag) {
		return parseInt(getMetadataString(image, tag));
	}

	private static String getMetadataString(IIOImage image, String tag) {
		final String string = findMetadataValue(image.getMetadata(), tag);
		if (string == null) {
			throw new IllegalStateException("PNG metadata is missing: " + tag);
		}
		return string;
	}
}
