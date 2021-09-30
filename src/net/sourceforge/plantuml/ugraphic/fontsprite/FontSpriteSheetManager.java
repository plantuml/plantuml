package net.sourceforge.plantuml.ugraphic.fontsprite;

import static java.awt.Font.PLAIN;
import static net.sourceforge.plantuml.utils.CollectionUtils.immutableList;

import java.awt.Font;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FontSpriteSheetManager {

	private static final FontSpriteSheetManager INSTANCE = new FontSpriteSheetManager();

	public static FontSpriteSheetManager instance() {
		return INSTANCE;
	}

	static final List<Integer> FONT_SIZES = immutableList(9, 20);

	private final Map<String, FontSpriteSheet> cache = new ConcurrentHashMap<>();

	private FontSpriteSheetManager() {
	}

	public FontSpriteSheet getNearestSheet(int style, int size) {
		if (size < 16) {
			size = 9;
		} else {
			size = 20;
		}

		final String cacheKey = style + "-" + size;

		FontSpriteSheet sheet = cache.get(cacheKey);
		if (sheet == null) { // TODO concurrency?
			sheet = load(style, size);
			cache.put(cacheKey, sheet);
		}
		return sheet;
	}

	private static FontSpriteSheet load(int style, int size) {
		final StringBuilder name = new StringBuilder("/font-sprite-sheets/JetBrains-Mono-NL-");
		if (style == PLAIN) name.append("Regular-");
		if ((style & Font.BOLD) > 0) name.append("Bold-");
		if ((style & Font.ITALIC) > 0) name.append("Italic-");
		name.append(size).append(".png");

		try (final InputStream in = FontSpriteSheet.class.getResourceAsStream(name.toString())) {
			if (in == null) {
				throw new RuntimeException(String.format("Resource '%s' not found", name));
			}
			return new FontSpriteSheet(in);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error loading Font Sprite Sheet '%s' : %s", name, e.getMessage()), e);
		}
	}
}
