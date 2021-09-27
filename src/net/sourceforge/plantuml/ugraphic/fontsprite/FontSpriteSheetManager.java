package net.sourceforge.plantuml.ugraphic.fontsprite;

import static net.sourceforge.plantuml.ugraphic.fontsprite.FontSpriteSheetIO.readFontSpriteSheetFromPNG;

import java.io.InputStream;

// TODO sizes
public class FontSpriteSheetManager {

	private FontSpriteSheet bold;
	private FontSpriteSheet boldItalic;
	private FontSpriteSheet italic;
	private FontSpriteSheet plain;

	public FontSpriteSheet bold() {
		if (bold == null) {
			bold = load("JetBrainsMonoNL-12-Bold.png");
		}
		return bold;
	}

	public FontSpriteSheet boldItalic() {
		if (boldItalic == null) {
			boldItalic = load("JetBrainsMonoNL-12-BoldItalic.png");
		}
		return boldItalic;
	}

	public FontSpriteSheet italic() {
		if (italic == null) {
			italic = load("JetBrainsMonoNL-12-Italic.png");
		}
		return italic;
	}

	public FontSpriteSheet plain() {
		if (plain == null) {
			plain = load("JetBrainsMonoNL-12-Regular.png");
		}
		return plain;
	}

	private static FontSpriteSheet load(String name) {
		try (final InputStream in = FontSpriteSheet.class.getResourceAsStream("/font-sprite-sheets/" + name)) {
			if (in == null) {
				throw new IllegalStateException("Resource not found");
			}
			return readFontSpriteSheetFromPNG(in);
		} catch (Exception e) {
			throw new RuntimeException("Error loading Font Sprite Sheet '" + name + "' : " + e.getMessage(), e);
		}
	}
}
