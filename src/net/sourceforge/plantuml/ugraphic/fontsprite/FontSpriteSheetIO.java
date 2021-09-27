package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.lang.Integer.parseInt;
import static java.nio.file.Files.newOutputStream;
import static net.sourceforge.plantuml.png.MetadataTag.findMetadataValue;
import static net.sourceforge.plantuml.utils.ImageIOUtils.createImageReader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.sourceforge.plantuml.png.PngIOMetadata;

public class FontSpriteSheetIO {

	private static final String ASCENT_TAG = "PlantUml-FontSprite-Ascent";

	private static final String LINE_HEIGHT_TAG = "PlantUml-FontSprite-LineHeight";

	public static FontSpriteSheet readFontSpriteSheetFromPNG(InputStream in) throws IOException {
		try (ImageInputStream iis = ImageIO.createImageInputStream(in)) {
			final ImageReader reader = createImageReader(iis);
			final IIOImage image = reader.readAll(0, null);
			final int ascent = getMetadataInt(image, ASCENT_TAG);
			final int lineHeight = getMetadataInt(image, LINE_HEIGHT_TAG);
			return new FontSpriteSheet((BufferedImage) image.getRenderedImage(), ascent, lineHeight);
		}
	}

	public static void writeFontSpriteSheetAsPNG(FontSpriteSheet sheet, Path path) throws IOException {
		try (OutputStream os = newOutputStream(path)) {
			writeFontSpriteSheetAsPNG(sheet, os);
		}
	}
	
	public static void writeFontSpriteSheetAsPNG(FontSpriteSheet sheet, OutputStream out) throws IOException {
		final PngIOMetadata writer = new PngIOMetadata();
		writer.addText(ASCENT_TAG, String.valueOf(sheet.getAscent()));
		writer.addText(LINE_HEIGHT_TAG, String.valueOf(sheet.getLineHeight()));
		writer.write(sheet.getImage(), out);
	}

	private static int getMetadataInt(IIOImage image, String tag) {
		final String string = findMetadataValue(image.getMetadata(), tag);
		if (string == null) {
			throw new IllegalStateException("PNG metadata is missing: " + tag);
		}
		return parseInt(string);
	}
}
