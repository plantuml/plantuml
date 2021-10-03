package net.sourceforge.plantuml.ugraphic.fontspritesheet;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static net.sourceforge.plantuml.test.Assertions.assertImagesEqualWithinTolerance;
import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.ALL_CHARS;
import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.JETBRAINS_FONT_FAMILY;
import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.createFontSpriteSheet;
import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.registerJetBrainsFonts;
import static net.sourceforge.plantuml.utils.MathUtils.roundUp;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.CartesianEnumSource;
import org.junitpioneer.jupiter.CartesianProductTest;
import org.junitpioneer.jupiter.CartesianValueSource;
import org.junitpioneer.jupiter.params.IntRangeSource;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.approvaltesting.ApprovalTesting;
import net.sourceforge.plantuml.approvaltesting.ApprovalTestingJUnitExtension;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;

@ExtendWith(ApprovalTestingJUnitExtension.class)
class FontSpriteSheetTest {

	@BeforeAll
	static void before_all() throws Exception {
		registerJetBrainsFonts();
	}

	//
	// Test Cases
	//

	@Test
	void test_stored_font_sprites_on_white() {

		final FontSpriteSheetManager manager = FontSpriteSheetManager.instance();
		final List<FontSpriteSheet> sheets = manager.allSheets();

		double height = 0;
		double width = 0;
		for (FontSpriteSheet sheet : sheets) {
			final Dimension2D dimension = sheet.calculateDimension(ALL_CHARS);
			height += dimension.getHeight();
			width = max(width, dimension.getWidth());
		}

		final int margin = 2;
		height += 2 * margin;
		width += 2 * margin;

		final BufferedImage image = new BufferedImage(roundUp(width), roundUp(height), TYPE_INT_RGB);
		final Graphics2D g = image.createGraphics();

		g.setBackground(WHITE);
		g.clearRect(0, 0, image.getWidth(), image.getHeight());

		g.setColor(BLACK);
		g.translate(margin, margin);

		for (FontSpriteSheet sheet : sheets) {
			final Dimension2D dimension = sheet.calculateDimension(ALL_CHARS);
			sheet.drawString(g, ALL_CHARS, 0, (float) (dimension.getHeight() - sheet.getDescent()));
			g.translate(0, dimension.getHeight());
		}

		approvalTesting.approve(image);
	}

	@Test
	void test_stored_font_sprites_on_alpha() {
		final FontSpriteSheet sheet = FontSpriteSheetManager.instance().findNearestSheet(new Font(null, ITALIC, 9));

		final float[] numbers = new float[]{0, 0.01f, 0.25f, 1f / 3, 0.5f, 2f / 3, 0.75f, 0.99f, 1};
		final String testString = ".!@#$%^&*0OI1☺'";

		final int margin = 5;
		final Dimension2D dimension = sheet.calculateDimension(testString);
		final int stringWidth = roundUp(dimension.getWidth());
		final int stripeWidth = stringWidth * numbers.length;
		final double lineHeight = dimension.getHeight();
		final int stripeHeight = roundUp(numbers.length * lineHeight);

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
						g_fg_alpha.translate(0, lineHeight);
					}
					g_bg_alpha.translate(stringWidth, 0);
				}
				g_bg_color.translate(0, stripeHeight);
			}
			g_font_color.translate(stripeWidth, 0);
		}
		approvalTesting.approve(image);
	}

	@CartesianProductTest(name = "{arguments}")
	@CartesianValueSource(ints = {3, 4, 9, 20})
	@CartesianEnumSource(FontStyle.class)
	void test_font_sheet_draws_same_as_raw_font_using_different_sizes_and_styles(int size, FontStyle style) throws Exception {
		check_font_sheet_draws_same_as_raw_font(style.toFont(size), BLACK);
	}

	@ParameterizedTest(name = "{arguments}")
	@IntRangeSource(from = 0, to = 255)
	void test_font_sheet_draws_same_as_raw_font_using_different_alphas(int alpha) throws Exception {
		check_font_sheet_draws_same_as_raw_font(PLAIN_FONT_20, new Color(1, 1, 1, alpha));
	}

	@CartesianProductTest(name = "{arguments}", value = {"00", "33", "88", "AA", "FF"})
	void test_font_sheet_draws_same_as_raw_font_using_different_colors(String r, String g, String b) throws Exception {
		check_font_sheet_draws_same_as_raw_font(PLAIN_FONT_20, new Color(parseInt(r + g + b, 16)));
	}

	@Test
	void test_getDescent() {
		final FontSpriteSheet sheet = createFontSpriteSheet(PLAIN_FONT_20);
		final UFont uFont = UFont.fromFont(PLAIN_FONT_20);

		assertThat(sheet.getDescent())
				.isEqualTo(NORMAL_BOUNDER.getDescent(uFont, "foo"));
	}

	@CartesianProductTest(name = "{arguments}")
	@CartesianValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20})
	@CartesianValueSource(strings = {"", " ", "x", "foo", ALL_CHARS})
	@CartesianEnumSource(FontStyle.class)
	void test_calculateDimension(int size, String string, FontStyle style) {
		final Font font = style.toFont(size);
		final FontSpriteSheet sheet = createFontSpriteSheet(font);
		final UFont uFont = UFont.fromFont(font);
		final Dimension2D actual = sheet.calculateDimension(string);
		final Dimension2D expected = NORMAL_BOUNDER.calculateDimension(uFont, string);

		assertThat(actual.getHeight())
				.isEqualTo(expected.getHeight());
		assertThat(actual.getWidth())
				.isEqualTo(expected.getWidth());
	}

	//
	// Test DSL
	//

	private static final StringBounder NORMAL_BOUNDER = FileFormat.PNG.getDefaultStringBounder();

	private static final Font PLAIN_FONT_20 = new Font(JETBRAINS_FONT_FAMILY, PLAIN, 20);

	// Kludge to give us meaningful test names
	@SuppressWarnings("unused")
	private enum FontStyle {
		PLAIN(Font.PLAIN),
		ITALIC(Font.ITALIC),
		BOLD(Font.BOLD),
		BOLD_ITALIC(Font.BOLD | Font.ITALIC);

		Font toFont(int size) {
			return new Font(JETBRAINS_FONT_FAMILY, style, size);
		}

		private final int style;

		FontStyle(int style) {
			this.style = style;
		}

	}

	@SuppressWarnings("unused")  // injected by ApprovalTestingJUnitExtension
	private ApprovalTesting approvalTesting;

	private void check_font_sheet_draws_same_as_raw_font(Font font, Color color) throws Exception {
		final String testString = ALL_CHARS;

		final FontSpriteSheet sheet = createFontSpriteSheet(font);
		final int margin = 2;
		final Dimension2D dimension = sheet.calculateDimension(testString);
		final int width = roundUp(dimension.getWidth() + 2 * margin);
		final int height = roundUp(dimension.getHeight() + 2 * margin);

		// Draw using sprites

		final BufferedImage image_from_sprite = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g1 = image_from_sprite.createGraphics();
		g1.setBackground(WHITE);
		g1.setColor(color);
		g1.clearRect(0, 0, width, height);
		g1.translate(margin, margin + sheet.getAscent());
		sheet.drawString(g1, testString, 0, 0);

		// Draw using font

		final BufferedImage image_from_font = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g2 = image_from_font.createGraphics();
		g2.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_GASP);
		g2.setBackground(WHITE);
		g2.setColor(color);
		g2.setFont(font);
		g2.clearRect(0, 0, width, height);
		g2.translate(margin, margin + g2.getFontMetrics().getAscent());

		for (char c : testString.toCharArray()) {
			g2.drawString(String.valueOf(c), 0, 0);
			g2.translate(sheet.getAdvance(), 0);
		}

		// Compare

		try {
			// tolerance value is explained by the comment in FontSpriteSheet.calculateAlpha()
			final int tolerance = color.getAlpha() >= 128 && color.getAlpha() <= 254 ? 1 : 0;

			assertImagesEqualWithinTolerance(image_from_font, image_from_sprite, tolerance);
		} catch (AssertionError e) {
			approvalTesting
					.withMaxFailures(3)
					.fail(a -> {
						ImageIO.write(image_from_font, "png", a.getPathForFailed("_from_font", ".png").toFile());
						ImageIO.write(image_from_sprite, "png", a.getPathForFailed("_from_sprite", ".png").toFile());
						sheet.writeAsPNG(a.getPathForFailed("_sprite_sheet", ".png"));
					})
					.rethrow(e);
		}
	}
}
