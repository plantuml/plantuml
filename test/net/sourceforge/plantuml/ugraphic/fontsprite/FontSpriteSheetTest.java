package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static net.sourceforge.plantuml.test.ApprovalTesting.approveImage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

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

		approveImage(image);
	}

	@Test
	void test_render_font_sprite_on_alpha() {
		final FontSpriteSheetManager manager = new FontSpriteSheetManager();
		final FontSpriteSheet sheet = manager.plain();

		final String testString = String.valueOf(sheet.getChars());
		final float[] background_alphas = new float[]{0, 1f / 3, 0.5f, 2f / 3, 1};
		final float[] foreground_alphas = {0.01f, 1f / 3, 0.5f, 2f / 3, 0.8f};

		final int width = sheet.getChars().length() * sheet.getCharWidth();
		final int stripeHeight = foreground_alphas.length * sheet.getLineHeight();
		final int height = background_alphas.length * stripeHeight;

		final BufferedImage image = new BufferedImage(width, height, TYPE_INT_ARGB);
		final Graphics2D g = image.createGraphics();

		for (float bg_alpha : background_alphas) {
			g.setColor(new Color(1, 1, 1, bg_alpha));
			g.fillRect(0, 0, width, stripeHeight);

			for (float fg_alpha : foreground_alphas) {
				g.setColor(new Color(1, 1, 1, fg_alpha));
				sheet.drawString(g, testString, 0, sheet.getAscent());
				g.translate(0, sheet.getLineHeight());
			}
		}

		approveImage(image);
	}
}
