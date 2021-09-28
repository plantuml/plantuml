package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import net.sourceforge.plantuml.test.approval.ApprovalTesting;
import net.sourceforge.plantuml.test.approval.ApprovalTestingJUnitExtension;

@ExtendWith(ApprovalTestingJUnitExtension.class)
class FontSpriteSheetTest {

	@Test
	void test_render_font_sprite_on_white() {
		final FontSpriteSheetManager manager = new FontSpriteSheetManager();
		final FontSpriteSheet plain = manager.plain();

		final int margin = 5;
		final int width = plain.getChars().length() * plain.getCharWidth() + 2 * margin;
		final int height = 4 * plain.getLineHeight() + 2 * margin;

		final BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
		final Graphics2D g = image.createGraphics();

		g.setBackground(WHITE);
		g.clearRect(0, 0, image.getWidth(), image.getHeight());

		g.setColor(BLACK);
		g.translate(0, margin);

		for (FontSpriteSheet sheet : new FontSpriteSheet[]{plain, manager.bold(), manager.italic(), manager.boldItalic()}) {
			String testString = String.valueOf(sheet.getChars());
			sheet.drawString(g, testString, margin, sheet.getAscent());
			g.translate(0, sheet.getLineHeight());
		}

		approvalTesting.approve(image);
	}

	@Test
	void test_render_font_sprite_on_alpha() {
		final FontSpriteSheetManager manager = new FontSpriteSheetManager();
		final FontSpriteSheet sheet = manager.plain();

		final float[] numbers = new float[]{0, 0.01f, 0.25f, 1f / 3, 0.5f, 2f / 3, 0.75f, 0.99f, 1};
		final String testString = ".!@#$%^&*0OI1'";

		final int margin = 5;
		final int stringWidth = testString.length() * sheet.getCharWidth();
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

	//
	// Test DSL
	//

	private ApprovalTesting approvalTesting;  // injected by ApprovalTestingJUnitExtension

}
