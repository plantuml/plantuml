package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// TODO sizes
public class FontSpriteSheetManager {

	private FontSpriteSheet bold;
	private FontSpriteSheet boldItalic;
	private FontSpriteSheet italic;
	private FontSpriteSheet plain;

	public FontSpriteSheet bold() {
		if (bold == null) {
			bold = new FontSpriteSheet(
					loadImageFromResource("/font-sprite-sheets/JetBrainsMonoNL-12-Bold.png"),
					13,           // ascent
					17,           // lineHeight
					(char) 0x21,  // minChar
					(char) 0x7e,  // maxChar
					7             // charWidth
			);
		}
		return bold;
	}

	public FontSpriteSheet boldItalic() {
		if (boldItalic == null) {
			boldItalic = new FontSpriteSheet(
					loadImageFromResource("/font-sprite-sheets/JetBrainsMonoNL-12-Bold.png"),
					13,           // ascent
					17,           // lineHeight
					(char) 0x21,  // minChar
					(char) 0x7e,  // maxChar
					7             // charWidth
			);
		}
		return boldItalic;
	}

	public FontSpriteSheet italic() {
		if (italic == null) {
			italic = new FontSpriteSheet(
					loadImageFromResource("/font-sprite-sheets/JetBrainsMonoNL-12-Bold.png"),
					13,           // ascent
					17,           // lineHeight
					(char) 0x21,  // minChar
					(char) 0x7e,  // maxChar
					7             // charWidth
			);
		}
		return italic;
	}

	public FontSpriteSheet plain() {
		if (plain == null) {
			plain = new FontSpriteSheet(
					loadImageFromResource("/font-sprite-sheets/JetBrainsMonoNL-12-Regular.png"),
					13,           // ascent
					17,           // lineHeight
					(char) 0x21,  // minChar
					(char) 0x7e,  // maxChar
					7             // charWidth
			);
		}
		return plain;
	}

	private BufferedImage loadImageFromResource(String name) {
		try {
			return ImageIO.read(requireNonNull(FontSpriteSheet.class.getResourceAsStream(name)));
		} catch (IOException e) {
			throw new RuntimeException("Error loading Font Sprite Sheet resource : " + e.getMessage(), e);
		}
	}
}
