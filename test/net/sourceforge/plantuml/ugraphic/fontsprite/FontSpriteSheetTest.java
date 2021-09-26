package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.util.Objects.requireNonNull;
import static net.sourceforge.plantuml.test.TestUtils.testOutputDir;
import static org.assertj.swing.assertions.Assertions.assertThat;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

class FontSpriteSheetTest {

	@Test
	void test_render_font_sprite_on_white() throws Exception {
		final FontSpriteSheetManager manager = new FontSpriteSheetManager();
		final FontSpriteSheet plain = manager.plain();

		final BufferedImage im = new BufferedImage(
				plain.getCharWidth() * plain.getChars().length() + 20,
				plain.getLineHeight() * 4 + 20,
				TYPE_INT_RGB
		);

		final Graphics2D g = im.createGraphics();
		g.setBackground(WHITE);
		g.clearRect(0, 0, im.getWidth(), im.getHeight());

		g.setColor(BLACK);

		for (FontSpriteSheet sheet : new FontSpriteSheet[]{plain, manager.bold(), manager.italic(), manager.boldItalic()}) {
			sheet.drawString(g, String.valueOf(sheet.getChars()), 10, sheet.getAscent());
			g.translate(0, sheet.getLineHeight());
		}

		ImageIO.write(im, "png", testOutputDir("render_font_sprite_on_white").resolve("output.png").toFile());

		final BufferedImage reference = ImageIO.read(requireNonNull(getClass().getResourceAsStream("/render_font_sprite_on_white.png")));

		assertThat(im)
				.isEqualTo(reference);
	}
}
