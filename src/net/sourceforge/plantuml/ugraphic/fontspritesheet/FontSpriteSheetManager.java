package net.sourceforge.plantuml.ugraphic.fontspritesheet;

import static java.awt.Font.BOLD;
import static java.awt.Font.ITALIC;
import static java.awt.Font.PLAIN;
import static java.util.Collections.unmodifiableList;
import static net.sourceforge.plantuml.utils.CollectionUtils.immutableList;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;

public class FontSpriteSheetManager {

	public static boolean USE = false;  // TODO temporary kludge
	
	private static final FontSpriteSheetManager INSTANCE = new FontSpriteSheetManager();

	public static FontSpriteSheetManager instance() {
		return INSTANCE;
	}

	static final List<Integer> FONT_SIZES = immutableList(9, 20);

	private final Map<String, FontSpriteSheet> cache = new ConcurrentHashMap<>();

	private FontSpriteSheetManager() {
	}

	public FontSpriteSheet findNearestSheet(Font font) {
		final int size = font.getSize() < 16 ? 9 : 20;

		final String cacheKey = font.getStyle() + "-" + size;

		FontSpriteSheet sheet = cache.get(cacheKey);
		if (sheet == null) { // TODO concurrency?
			sheet = load(font.getStyle(), size);
			cache.put(cacheKey, sheet);
		}
		return sheet;
	}

	public StringBounder createStringBounder() {
		return new StringBounder() {
			@Override
			public Dimension2D calculateDimension(UFont font, String text) {
				return findNearestSheet(font.getUnderlayingFont())
						.calculateDimension(text);
			}

			@Override
			public double getDescent(UFont font, String text) {
				return findNearestSheet(font.getUnderlayingFont())
						.getDescent();
			}
		};
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

	List<FontSpriteSheet> allSheets() {
		final List<FontSpriteSheet> sheets = new ArrayList<>();
		for (int size : FONT_SIZES) {
			sheets.add(findNearestSheet(new Font(null, PLAIN, size)));
			sheets.add(findNearestSheet(new Font(null, ITALIC, size)));
			sheets.add(findNearestSheet(new Font(null, BOLD, size)));
			sheets.add(findNearestSheet(new Font(null, BOLD | ITALIC, size)));
		}
		return unmodifiableList(sheets);
	}
}
