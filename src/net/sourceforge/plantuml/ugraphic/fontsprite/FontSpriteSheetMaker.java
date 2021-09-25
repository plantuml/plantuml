package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.awt.Color.WHITE;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FontSpriteSheetMaker {

	public static final char MIN_CHAR = 0x21;
	public static final char MAX_CHAR = 0x7E;

	public static FontSpriteSheet createFontSpriteSheet(Font font) {
		final Graphics2D g0 = new BufferedImage(1, 1, TYPE_BYTE_GRAY).createGraphics();
		g0.setFont(font);
		final FontMetrics fontMetrics = g0.getFontMetrics();

		final int ascent = fontMetrics.getMaxAscent();

		int maxCharWidth = 0;
		for (char c = MIN_CHAR; c <= MAX_CHAR; c++) {
			int charWidth = fontMetrics.charWidth(c);
			if (charWidth > maxCharWidth) maxCharWidth = charWidth;
		}

		final int numChars = MAX_CHAR - MIN_CHAR + 1;

		final BufferedImage image = new BufferedImage(maxCharWidth * numChars, ascent + fontMetrics.getMaxDescent(), TYPE_BYTE_GRAY);
		final Graphics2D g = image.createGraphics();
		g.setColor(WHITE);
		g.setFont(font);
		g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);

		for (char c = MIN_CHAR; c <= MAX_CHAR; c++) {
			g.drawString(String.valueOf(c), 0, ascent);
			g.translate(maxCharWidth, 0);
		}

		return new FontSpriteSheet(image, ascent, fontMetrics.getHeight(), MAX_CHAR, MIN_CHAR, maxCharWidth);
	}
}
