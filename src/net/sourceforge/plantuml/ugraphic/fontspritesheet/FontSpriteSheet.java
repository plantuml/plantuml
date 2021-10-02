package net.sourceforge.plantuml.ugraphic.fontspritesheet;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Math.round;
import static java.nio.file.Files.newOutputStream;
import static net.sourceforge.plantuml.png.MetadataTag.findMetadataValue;
import static net.sourceforge.plantuml.utils.ImageIOUtils.createImageReader;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import net.sourceforge.plantuml.png.PngIOMetadata;

public class FontSpriteSheet {

	private static final int MIN_CHAR = 0x21;
	private static final int MAX_CHAR = 0x7e;

	private final Map<Integer, SoftReference<BufferedImage>> colorisedImageCache = new ConcurrentHashMap<>();
	private final int advance;
	private final BufferedImage alphaImage;
	private final int ascent;
	private final float descent;
	private final int lineHeight;
	private final String name;
	private final int pointSize;
	private final int spriteWidth;
	private final int style;
	private final int xOffset;

	FontSpriteSheet(BufferedImage alphaImage, FontMetrics fontMetrics, int advance, int ascent, float descent, int spriteWidth, int xOffset) {
		this.advance = advance;
		this.alphaImage = alphaImage;
		this.ascent = ascent;
		this.descent = descent;
		this.lineHeight = fontMetrics.getHeight();
		this.name = fontMetrics.getFont().getFontName();
		this.pointSize = fontMetrics.getFont().getSize();
		this.spriteWidth = spriteWidth;
		this.style = fontMetrics.getFont().getStyle();
		this.xOffset = xOffset;
	}

	FontSpriteSheet(InputStream in) throws IOException {
		try (ImageInputStream iis = ImageIO.createImageInputStream(in)) {
			final IIOImage iioImage = createImageReader(iis).readAll(0, null);
			advance = getMetadataInt(iioImage, TAG_ADVANCE);
			alphaImage = (BufferedImage) iioImage.getRenderedImage();
			ascent = getMetadataInt(iioImage, TAG_ASCENT);
			descent = getMetadataFloat(iioImage, TAG_DESCENT);
			lineHeight = getMetadataInt(iioImage, TAG_LINE_HEIGHT);
			name = getMetadataString(iioImage, TAG_NAME);
			pointSize = getMetadataInt(iioImage, TAG_POINT_SIZE);
			spriteWidth = getMetadataInt(iioImage, TAG_SPRITE_WIDTH);
			style = getMetadataInt(iioImage, TAG_STYLE);
			xOffset = getMetadataInt(iioImage, TAG_X_OFFSET);
		}
	}

	public int getAdvance() {
		return advance;
	}

	public int getAscent() {
		return ascent;
	}

	public float getDescent() {
		return descent;
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

	String getPreferredFilename() {
		return getName().replace(' ', '-') + "-" + getPointSize() + ".png";
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public int getStyle() {
		return style;
	}

	@Override
	public String toString() {
		return getName() + " " + getPointSize();
	}

	//
	// Drawing
	//

	public void drawString(Graphics g, String s, int x, int y) {
		final BufferedImage colorisedImage = getOrCreateColorisedImage(g.getColor());

		for (char c : s.toCharArray()) {
			if (c != ' ') {
				drawChar(g, colorisedImage, c, x, y);
			}
			x += advance;
		}
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
	private void drawChar(Graphics g, BufferedImage colorisedImage, char c, int x, int y) {
		// We draw strings by blitting each char from an image that has all pixels set to the requested color
		// and has alpha values copied from alphaImage.
		// 
		// As an alternative I tried making a ColorModel class that returns colorized pixels
		// with their alpha value read direct from alphaImage but drawing that way was 2 - 3 times slower
		// than the blitting approach.

		final int height = colorisedImage.getHeight();

		final int srcLeft = spriteIndex(c) * spriteWidth;
		final int srcRight = srcLeft + spriteWidth;
		final int srcTop = 0;
		final int srcBottom = height;

		final int destLeft = x - xOffset;
		final int destRight = destLeft + spriteWidth;
		final int destTop = y - ascent;
		final int destBottom = destTop + height;

		g.drawImage(
				colorisedImage,
				destLeft, destTop, destRight, destBottom,
				srcLeft, srcTop, srcRight, srcBottom,
				null
		);
	}

	private int spriteIndex(char c) {
		if (c < MIN_CHAR || c > MAX_CHAR) {
			return 0;  // tofu
		} else {
			return c - MIN_CHAR + 1;
		}
	}

	private BufferedImage getOrCreateColorisedImage(Color color) {
		final int cacheKey = color.getRGB();
		final SoftReference<BufferedImage> ref = colorisedImageCache.get(cacheKey);
		BufferedImage image = ref != null ? ref.get() : null;
		if (image == null) {
			image = createColorisedImage(color);
			colorisedImageCache.put(cacheKey, new SoftReference<>(image));
		}
		return image;
	}

	private BufferedImage createColorisedImage(Color color) {
		final BufferedImage image = new BufferedImage(alphaImage.getWidth(), alphaImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		final DataBuffer data = image.getRaster().getDataBuffer();
		final DataBuffer alphaData = alphaImage.getRaster().getDataBuffer();
		final float colorAlpha = color.getAlpha() / 255f;
		final int colorRgb = color.getRGB() & 0x00FFFFFF;
		final int dataSize = data.getSize();

		for (int i = 0; i < dataSize; i++) {
			data.setElem(i, colorRgb | calculateAlpha(colorAlpha, alphaData.getElem(i)));
		}

		return image;
	}

	private int calculateAlpha(float colorAlpha, int spriteAlpha) {
		// This calculation gets very close to matching what happens in Graphics2D.drawString()
		// but some values are off by one when colorAlpha is between 128 and 254.
		//
		// I think it's because Graphics2D.drawString() uses floating point for alpha calculations
		// but alphaImage stores 8-bit integers so there is less accuracy here.

		return (round(colorAlpha * spriteAlpha) & 0xFF) << 24;
	}

	//
	// PNG Read / Write
	//

	private static final String TAG_ADVANCE = "PlantUml-FontSprite-Advance";
	private static final String TAG_ASCENT = "PlantUml-FontSprite-Ascent";
	private static final String TAG_DESCENT = "PlantUml-FontSprite-Descent";
	private static final String TAG_LINE_HEIGHT = "PlantUml-FontSprite-LineHeight";
	private static final String TAG_NAME = "PlantUml-FontSprite-Name";
	private static final String TAG_POINT_SIZE = "PlantUml-FontSprite-PointSize";
	private static final String TAG_SPRITE_WIDTH = "PlantUml-FontSprite-SpriteWidth";
	private static final String TAG_STYLE = "PlantUml-FontSprite-Style";
	private static final String TAG_X_OFFSET = "PlantUml-FontSprite-XOffset";

	void writeAsPNG(Path path) throws IOException {
		try (OutputStream os = newOutputStream(path)) {
			writeAsPNG(os);
		}
	}

	private void writeAsPNG(OutputStream out) throws IOException {
		final PngIOMetadata writer = new PngIOMetadata();
		writer.addText(TAG_ADVANCE, String.valueOf(advance));
		writer.addText(TAG_ASCENT, String.valueOf(ascent));
		writer.addText(TAG_DESCENT, String.valueOf(descent));
		writer.addText(TAG_LINE_HEIGHT, String.valueOf(lineHeight));
		writer.addText(TAG_NAME, String.valueOf(name));
		writer.addText(TAG_POINT_SIZE, String.valueOf(pointSize));
		writer.addText(TAG_SPRITE_WIDTH, String.valueOf(spriteWidth));
		writer.addText(TAG_STYLE, String.valueOf(style));
		writer.addText(TAG_X_OFFSET, String.valueOf(xOffset));
		writer.write(alphaImage, out);
	}

	private static float getMetadataFloat(IIOImage image, String tag) {
		return parseFloat(getMetadataString(image, tag));
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
