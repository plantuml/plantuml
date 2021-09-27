package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.Font.createFont;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static net.sourceforge.plantuml.test.TestUtils.testOutputDir;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheetIO.writeFontSpriteSheetAsPNG;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheetMaker.createFontSpriteSheet;
import static org.assertj.swing.assertions.Assertions.assertThat;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FontSpriteSheetMakerTest {

	//
	// Test Cases
	//

	@ParameterizedTest
	@ValueSource(strings = {"Bold", "BoldItalic", "Italic", "Regular"})
	void test_create_sheets(String style) throws Exception {
		final int size = 12;
		final File fontFile = Paths.get("JetBrainsMono-2.242").resolve("JetBrainsMonoNL-" + style + ".ttf").toFile();
		final Font font = createFont(TRUETYPE_FONT, fontFile).deriveFont((float) size);
		final FontSpriteSheet sheet = createFontSpriteSheet(font);
		final Path target = testOutputDir("font-sprite-sheets").resolve("JetBrainsMonoNL-" + size + "-" + style + ".png");
		writeFontSpriteSheetAsPNG(sheet, target);
	}

	// I have seen one font fail because the real maxAscent is one bigger than FontMetrics.getMaxAscent() returns - so the top pixel row was missing in the sprite sheet)
	@ParameterizedTest
	@CsvSource(value = {
			"Bold       , 8",
			"BoldItalic , 8",
			"Italic     , 8",
			"Regular    , 8",
			"Bold       , 20",
			"BoldItalic , 20",
			"Italic     , 20",
			"Regular    , 20"
	})
	void test_font_sheet_matches_raw_font(String style, int size) throws Exception {
		final File fontFile = Paths.get("JetBrainsMono-2.242").resolve("JetBrainsMonoNL-" + style + ".ttf").toFile();

		final Font font = createFont(TRUETYPE_FONT, fontFile).deriveFont((float) size);

		final FontSpriteSheet sheet = createFontSpriteSheet(font);

		final String string = sheet.getChars().toString();

		final int width = string.length() * sheet.getCharWidth() + 20;

		final int height = sheet.getLineHeight() + 20;

		final BufferedImage fontImage = createFontImage(width, height, font, sheet.getCharWidth(), string);

		final BufferedImage sheetImage = createSheetImage(width, height, sheet, string);

		try {
			assertThat(sheetImage)
					.isEqualTo(fontImage);
		} catch (AssertionError e) {
			final Path dir = testOutputDir("font-sheet-matches-raw-font");
			final String prefix = style + "-" + size;
			ImageIO.write(fontImage, "png", dir.resolve(prefix + "-from-font.png").toFile());
			ImageIO.write(sheetImage, "png", dir.resolve(prefix + "-from-sheet.png").toFile());
			writeFontSpriteSheetAsPNG(sheet, dir.resolve(prefix + "-sheet.png"));
			throw e;
		}
	}

	//
	// Test DSL
	//

	private BufferedImage createFontImage(int width, int height, Font font, int charWidth, String string) {
		final BufferedImage im = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g = im.createGraphics();
		g.setBackground(WHITE);
		g.clearRect(0, 0, im.getWidth(), im.getHeight());

		g.setColor(BLACK);
		g.setFont(font);
		g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
		g.translate(10, 10);

		for (char c : string.toCharArray()) {
			g.drawString(String.valueOf(c), 0, g.getFontMetrics().getAscent());
			g.translate(charWidth, 0);
		}

		return im;
	}

	private BufferedImage createSheetImage(int width, int height, FontSpriteSheet sheet, String string) {
		final BufferedImage im = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g = im.createGraphics();
		g.setBackground(WHITE);
		g.clearRect(0, 0, im.getWidth(), im.getHeight());

		g.setColor(BLACK);
		g.translate(10, 10);
		sheet.drawString(g, string, 0, sheet.getAscent());

		return im;
	}
}
