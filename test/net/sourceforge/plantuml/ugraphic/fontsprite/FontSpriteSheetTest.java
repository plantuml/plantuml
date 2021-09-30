package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.Font.BOLD;
import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.max;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheetMaker.ALL_CHARS;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheetMaker.JETBRAINS_FONT_FAMILY;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheetMaker.createFontSpriteSheet;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheetMaker.registerJetBrainsFonts;
import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheetManager.FONT_SIZES;
import static org.assertj.swing.assertions.Assertions.assertThat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.test.approval.ApprovalTesting;
import net.sourceforge.plantuml.test.approval.ApprovalTestingJUnitExtension;

@ExtendWith(ApprovalTestingJUnitExtension.class)
class FontSpriteSheetTest {

	@BeforeAll
	static void before_all() throws Exception {
		registerJetBrainsFonts();
	}

	@Test
	void test_stored_font_sprites_on_white() {

		final FontSpriteSheetManager manager = FontSpriteSheetManager.instance();

		final List<FontSpriteSheet> sheets = new ArrayList<>();
		for (int size : FONT_SIZES) {
			sheets.add(manager.getNearestSheet(PLAIN, size));
			sheets.add(manager.getNearestSheet(ITALIC, size));
			sheets.add(manager.getNearestSheet(BOLD, size));
			sheets.add(manager.getNearestSheet(BOLD | ITALIC, size));
		}

		int height = 0;
		int width = 0;
		for (FontSpriteSheet sheet : sheets) {
			height += sheet.getLineHeight();
			width = max(width, sheet.getSpriteWidth() * ALL_CHARS.length());
		}

		final int margin = 2;
		height += 2 * margin;
		width += 2 * margin;

		final BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g = image.createGraphics();

		g.setBackground(WHITE);
		g.clearRect(0, 0, image.getWidth(), image.getHeight());

		g.setColor(BLACK);
		g.translate(margin, margin);

		for (FontSpriteSheet sheet : sheets) {
			sheet.drawString(g, ALL_CHARS, 0, sheet.getAscent());
			g.translate(0, sheet.getLineHeight());
		}

		approvalTesting.approve(image);
	}

	@Test
	void test_stored_font_sprites_on_alpha() {
		final FontSpriteSheetManager manager = FontSpriteSheetManager.instance();
		final FontSpriteSheet sheet = manager.getNearestSheet(ITALIC, 9);

		final float[] numbers = new float[]{0, 0.01f, 0.25f, 1f / 3, 0.5f, 2f / 3, 0.75f, 0.99f, 1};
		final String testString = ".!@#$%^&*0OI1☺'";

		final int margin = 5;
		final int stringWidth = testString.length() * sheet.getSpriteWidth();
		final int stripeWidth = stringWidth * numbers.length;
		final int stripeHeight = numbers.length * sheet.getLineHeight();

		final BufferedImage image = new BufferedImage(2 * stripeWidth + 2 * margin, numbers.length * stripeHeight + 2 * margin, TYPE_INT_ARGB);
		final Graphics2D g_font_color = image.createGraphics();
		g_font_color.translate(margin, margin);

		for (int font_color : new int[]{0, 1}) {
			final Graphics2D g_bg_color = (Graphics2D) g_font_color.create();
			for (float bg_color : numbers) {
				final Graphics2D g_bg_alpha = (Graphics2D) g_bg_color.create();
				for (float bg_alpha : numbers) {
					g_bg_alpha.setColor(new Color(bg_color, bg_color, bg_color, bg_alpha));
					g_bg_alpha.fillRect(0, 0, stringWidth, stripeHeight);
					final Graphics2D g_fg_alpha = (Graphics2D) g_bg_alpha.create();
					for (float fg_alpha : numbers) {
						g_fg_alpha.setColor(new Color(font_color, font_color, font_color, fg_alpha));
						sheet.drawString(g_fg_alpha, testString, 0, sheet.getAscent());
						g_fg_alpha.translate(0, sheet.getLineHeight());
					}
					g_bg_alpha.translate(stringWidth, 0);
				}
				g_bg_color.translate(0, numbers.length * sheet.getLineHeight());
			}
			g_font_color.translate(stripeWidth, 0);
		}
		approvalTesting.approve(image);
	}

	@ParameterizedTest(name = "{arguments}")
	@CsvSource(value = {
			" 3, Regular   ",
			" 3, Italic    ",
			" 3, Bold      ",
			" 3, BoldItalic",
			" 4, Regular   ",
			" 4, Italic    ",
			" 4, Bold      ",
			" 4, BoldItalic",
			" 9, Regular   ",
			" 9, Italic    ",
			" 9, Bold      ",
			" 9, BoldItalic",
			"20, Regular   ",
			"20, Italic    ",
			"20, Bold      ",
			"20, BoldItalic",
	})
	void test_font_sheet_draws_same_as_raw_font(int size, String style) throws Exception {

		final String testString = ALL_CHARS;

		//noinspection MagicConstant
		final Font font = new Font(JETBRAINS_FONT_FAMILY, styleFromName(style), size);

		final FontSpriteSheet sheet = createFontSpriteSheet(font);
		final int margin = 2;
		final int width = testString.length() * sheet.getSpriteWidth() + 2 * margin;
		final int height = sheet.getLineHeight() + 2 * margin;

		// Draw using sprites

		final BufferedImage image_from_sprite = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g1 = image_from_sprite.createGraphics();
		g1.setBackground(WHITE);
		g1.setColor(BLACK);
		g1.clearRect(0, 0, width, height);
		g1.translate(margin, margin + sheet.getAscent());
		sheet.drawString(g1, testString, 0, 0);

		// Draw using font

		final BufferedImage image_from_font = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g2 = image_from_font.createGraphics();
		g2.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_GASP);
		g2.setBackground(WHITE);
		g2.setColor(BLACK);
		g2.setFont(font);
		g2.clearRect(0, 0, width, height);
		g2.translate(margin, margin + g2.getFontMetrics().getAscent());

		for (char c : testString.toCharArray()) {
			g2.drawString(String.valueOf(c), 0, 0);
			g2.translate(sheet.getAdvance(), 0);
		}

		// Compare

		try {
			assertThat(image_from_sprite)
					.isEqualTo(image_from_font);
		} catch (AssertionError e) {
			final String baseName = approvalTesting.getBaseName();
			final Path dir = approvalTesting.getDir();
			ImageIO.write(image_from_font, "png", dir.resolve(baseName + "_from_font.failed.png").toFile());
			ImageIO.write(image_from_sprite, "png", dir.resolve(baseName + "_from_sprite.failed.png").toFile());
			sheet.writeAsPNG(dir.resolve(baseName + "_sprite_sheet.failed.png"));
			throw e;
		}
	}

	//
	// Test DSL
	//

	private ApprovalTesting approvalTesting;  // injected by ApprovalTestingJUnitExtension

	private int styleFromName(String styleName) {
		if ("Regular".equals(styleName)) return PLAIN;
		if ("Bold".equals(styleName)) return BOLD;
		if ("Italic".equals(styleName)) return ITALIC;
		if ("BoldItalic".equals(styleName)) return BOLD | ITALIC;
		throw new IllegalArgumentException();
	}
}
