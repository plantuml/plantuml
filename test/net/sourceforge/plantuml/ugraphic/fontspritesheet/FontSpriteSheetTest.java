package net.sourceforge.plantuml.ugraphic.fontspritesheet;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.util.Arrays.stream;
import static net.sourceforge.plantuml.test.Assertions.assertImagesEqual;
import static net.sourceforge.plantuml.test.ColorComparators.COMPARE_PIXEL_EXACT;
import static net.sourceforge.plantuml.test.ColorComparators.comparePixelWithSBTolerance;
import static net.sourceforge.plantuml.test.TestUtils.renderAsImage;
import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.ALL_CHARS;
import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.JETBRAINS_FONT_FAMILY;
import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.createFontSpriteSheet;
import static net.sourceforge.plantuml.ugraphic.fontspritesheet.FontSpriteSheetMaker.registerJetBrainsFonts;
import static net.sourceforge.plantuml.utils.MathUtils.roundUp;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Comparator;
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
import net.sourceforge.plantuml.graphic.color.ColorHSB;
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
	@CartesianValueSource(ints = {20, 9, 5, 4})
	@CartesianEnumSource(FontStyle.class)
	void test_font_sheet_draws_same_as_raw_font_using_different_sizes_and_styles(int size, FontStyle style) {
		final CheckOptions options = new CheckOptions();
		options.size = size;
		options.style = style;
		options.comparator = new Comparator<ColorHSB>() {
			@Override
			public int compare(ColorHSB expected, ColorHSB actual) {
				return (
						abs(expected.getHue() - actual.getHue()) > 0
								|| abs(expected.getSaturation() - actual.getSaturation()) > 0
								|| abs(expected.getBrightness() - actual.getBrightness()) > 0.10
				) ? 1 : 0;
			}

			@Override
			public String toString() {
				return "custom";
			}
		};
		
		check_font_sheet_draws_same_as_raw_font(options);
	}

	@ParameterizedTest(name = "{arguments}")
	@IntRangeSource(from = 0, to = 255)
	void test_font_sheet_draws_same_as_raw_font_using_different_alphas(int alpha) {
		final CheckOptions options = new CheckOptions();
		options.color = new Color(1, 1, 1, alpha);
		options.comparator = comparePixelWithSBTolerance(0, 0.005f);
		check_font_sheet_draws_same_as_raw_font(options);
	}

	@CartesianProductTest(name = "{arguments}", value = {"00", "33", "88", "AA", "FF"})
	void test_font_sheet_draws_same_as_raw_font_using_different_colors(String r, String g, String b) {
		final CheckOptions options = new CheckOptions();
		options.color = new Color(parseInt(r + g + b, 16));
		check_font_sheet_draws_same_as_raw_font(options);
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

	@Test
	void test_png_read_write() throws Exception {
		final int style = ITALIC;
		final int pointSize = 20;

		final FontRenderContext frc = new BufferedImage(1, 1, TYPE_BYTE_GRAY).createGraphics().getFontRenderContext();

		final Font font = stream(getLocalGraphicsEnvironment().getAllFonts())
				.filter(f -> f.getLineMetrics("x", frc).getLeading() > 0)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("This test needs a font with non-zero leading"))
				.deriveFont(style, pointSize);

		final FontSpriteSheet original = createFontSpriteSheet(font);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		original.writeAsPNG(baos);

		final ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
		final FontSpriteSheet loaded = new FontSpriteSheet(inputStream);

		// isNotZero() & isNotEmpty() ensure we do not overlook a failure because the expected value is the default field value

		assertThat(loaded.getAdvance())
				.isNotZero()
				.isEqualTo(original.getAdvance());
		assertThat(loaded.getAscent())
				.isNotZero()
				.isEqualTo(original.getAscent());
		assertThat(loaded.getDescent())
				.isNotZero()
				.isEqualTo(original.getDescent());
		assertThat(loaded.getLeading())
				.isNotZero()
				.isEqualTo(original.getLeading());
		assertThat(loaded.getName())
				.isNotEmpty()
				.isEqualTo(font.getFontName());
		assertThat(loaded.getPointSize())
				.isNotZero()
				.isEqualTo(pointSize);
		assertThat(loaded.getSpriteWidth())
				.isNotZero()
				.isEqualTo(original.getSpriteWidth());
		assertThat(loaded.getStyle())
				.isNotZero()
				.isEqualTo(style);
		assertThat(loaded.getXOffset())
				.isNotZero()
				.isEqualTo(original.getXOffset());

		assertImagesEqual(original.getAlphaImage(), loaded.getAlphaImage());
	}

	@CartesianProductTest(name = "{arguments}")
	@CartesianValueSource(strings = {
			"<b>bold",
			"<i>italic",
			"<s>strike",
			"<u>underlined",
			"<w>wave",
			"plain",
	})
//	@CartesianValueSource(strings = {"red", "green", "blue", "yellow", "cyan", "magenta", "black"})
	@CartesianValueSource(strings = {"black"})
	@CartesianValueSource(ints = {9, 20})
	@IntRangeSource(from = 0, to = 255, step = 14)
	void test_plantuml_draws_same_with_font_and_sprite(String text, String fgColor, int size, int backAlpha) throws Exception {
		final String[] source = {
				"@startuml",
				"!$BGCOLOR=transparent",
				"!$FONT_NAME='JetBrains Mono NL'",
				"!theme plain",
				"skinparam ActivityBackgroundColor none",
				"skinparam ActivityBorderColor none",
				"skinparam ActivityFontSize " + size,
				String.format(":<color:%s><back:#000000%02X>%s %s;", fgColor, backAlpha, text, ALL_CHARS),
				"@enduml",
		};

		FontSpriteSheetManager.USE = false;
		final BufferedImage fromFont = renderAsImage(source);

		FontSpriteSheetManager.USE = true;
		final BufferedImage fromSprite = renderAsImage(source);

		final Comparator<ColorHSB> comparator = new Comparator<ColorHSB>() {
			@Override
			public int compare(ColorHSB expected, ColorHSB actual) {
				return (
						abs(expected.getHue() - actual.getHue()) > 0
								|| abs(expected.getSaturation() - actual.getSaturation()) > 0
								|| abs(expected.getBrightness() - actual.getBrightness()) > 0.05
				) ? 1 : 0;
			}

			@Override
			public String toString() {
				return "custom";
			}
		};

		try {
			assertImagesEqual(fromFont, fromSprite, comparator, 15);
		} catch (AssertionError e) {
			approvalTesting
					.withMaxFailures(1)
					.fail(a -> {
						ImageIO.write(fromFont, "png", a.getPathForFailed("_from_font", ".png").toFile());
						ImageIO.write(fromSprite, "png", a.getPathForFailed("_from_sprite", ".png").toFile());
					})
					.rethrow(e);
		}
	}

	//
	// Test DSL
	//

	private static final StringBounder NORMAL_BOUNDER = FileFormat.PNG.getDefaultStringBounder();

	public static final Font PLAIN_FONT_9 = new Font(JETBRAINS_FONT_FAMILY, PLAIN, 9);
	public static final Font PLAIN_FONT_20 = new Font(JETBRAINS_FONT_FAMILY, PLAIN, 20);

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

	private static class CheckOptions {
		Color color = BLACK;
		Comparator<ColorHSB> comparator = COMPARE_PIXEL_EXACT;
		int size = 20;
		String string = ALL_CHARS;
		FontStyle style = FontStyle.PLAIN;
	}

	private void check_font_sheet_draws_same_as_raw_font(CheckOptions options) {
		final Font font = options.style.toFont(options.size);
		final FontSpriteSheet sheet = createFontSpriteSheet(font);
		final int margin = 5;
		final Dimension2D dimension = sheet.calculateDimension(options.string);
		final int width = roundUp(dimension.getWidth() + 2 * margin);
		final int height = roundUp(dimension.getHeight() + 2 * margin);

		// Draw using sprites

		final BufferedImage image_from_sprite = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g1 = image_from_sprite.createGraphics();
		g1.setBackground(WHITE);
		g1.setColor(options.color);
		g1.clearRect(0, 0, width, height);
		float x = margin;
		final float spriteY = margin + sheet.getAscent();
		sheet.drawString(g1, options.string, x, spriteY);

		// Draw using font

		final BufferedImage image_from_font = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g2 = image_from_font.createGraphics();
		g2.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_GASP);
		g2.setBackground(WHITE);
		g2.setColor(options.color);
		g2.setFont(font);
		g2.clearRect(0, 0, width, height);

		final float fontAscent = new TextLayout(options.string, font, g2.getFontRenderContext()).getAscent();
		final float fontY = margin + fontAscent;

		for (char c : options.string.toCharArray()) {
			g2.drawString(String.valueOf(c), x, fontY);
			x += sheet.getAdvance();
		}

		// Compare

		try {
			assertImagesEqual(image_from_font, image_from_sprite, options.comparator, 0);
		} catch (AssertionError e) {
			approvalTesting
					.withMaxFailures(2)
					.fail(a -> {
						ImageIO.write(image_from_font, "png", a.getPathForFailed("_from_font", ".png").toFile());
						ImageIO.write(image_from_sprite, "png", a.getPathForFailed("_from_sprite", ".png").toFile());
						sheet.writeAsPNG(a.getPathForFailed("_sprite_sheet", ".png"));
					})
					.rethrow(e);
		}
	}
}
