package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.awt.Color.WHITE;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheet.MAX_CHAR;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheet.MIN_CHAR;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheet.NUM_CHARS;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// I did a few experiments comparing a single PNG containing four font styles (plain / bold / italic / bold-italic) 
// vs four separate PNGs and found the total file size is very similar either way.
//
// It is much simpler to use one PNG for each style so that is what we do.

public class FontSpriteSheetMaker {

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

		final BufferedImage image = new BufferedImage(maxCharWidth * NUM_CHARS, ascent + fontMetrics.getMaxDescent(), TYPE_BYTE_GRAY);
		final Graphics2D g = image.createGraphics();
		g.setColor(WHITE);
		g.setFont(font);
		g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);

		for (char c = MIN_CHAR; c <= MAX_CHAR; c++) {
			g.drawString(String.valueOf(c), 0, ascent);
			g.translate(maxCharWidth, 0);
		}

		return new FontSpriteSheet(image, ascent, fontMetrics.getHeight());
	}
}
