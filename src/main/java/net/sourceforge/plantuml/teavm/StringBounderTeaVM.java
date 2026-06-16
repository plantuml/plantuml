/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.teavm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class StringBounderTeaVM implements StringBounder {
	// ::remove file when JAVA8

	private static final int MAX_CACHE_SIZE = 8192;

	private static final class CacheKey {
		private final UFont font;
		private final String text;
		private final int hash;

		CacheKey(UFont font, String text) {
			this.font = font;
			this.text = text;
			this.hash = Objects.hash(font, text);
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj instanceof CacheKey == false)
				return false;
			final CacheKey other = (CacheKey) obj;
			return font.equals(other.font) && text.equals(other.text);
		}
	}

	private static final Map<CacheKey, XDimension2D> cache = new LinkedHashMap<>(16, 0.75f, true) {
		@Override
		protected boolean removeEldestEntry(Map.Entry<CacheKey, XDimension2D> eldest) {
			return size() > MAX_CACHE_SIZE;
		}
	};

	@Override
	public XDimension2D calculateDimension(UFont f, String text) {
		if (text == null || text.isEmpty())
			return new XDimension2D(0, 0);

		final UFont font = (UFont) f;
		final CacheKey key = new CacheKey(font, text);

		return cache.computeIfAbsent(key, k -> calculateDimensionSlow(font, text));
	}

	private XDimension2D calculateDimensionSlow(UFont font, String text) {
		final String fontFamily = font.getFamily(null, null);
		final int fontSize = font.getSize();
		final String fontWeight = font.getFontFace().isBold() ? "bold" : "normal";

		final double[] metrics = SvgGraphicsTeaVM.measureTextCanvas(text, fontFamily, fontSize, fontWeight);
		final double width = metrics[0];

		// Use SVG getBBox for height as it's more accurate for layout
		final double[] svgMetrics = SvgGraphicsTeaVM.measureTextSvgBBox(text, fontFamily, fontSize);
		double height = svgMetrics[1];

		// getBBox returns height=0 for whitespace-only strings (no visible glyphs)
		// In this case, use font metrics to get the proper line height
		if (height == 0 && !text.isEmpty()) {
			final double[] detailedMetrics = SvgGraphicsTeaVM.getDetailedTextMetrics(text, fontFamily, fontSize,
					fontWeight);
			height = detailedMetrics[3] + detailedMetrics[4]; // fontBoundingBoxAscent + fontBoundingBoxDescent
		}

		return new XDimension2D(width, height);

	}

	@Override
	public double getDescent(UFont font, String text) {
		if (text == null || text.isEmpty())
			return 0;

		final String fontFamily = font.getFamily(null, null);
		final int fontSize = font.getSize();
		final String fontWeight = font.getFontFace().isBold() ? "bold" : "normal";

		final double[] metrics = SvgGraphicsTeaVM.getDetailedTextMetrics(text, fontFamily, fontSize, fontWeight);
		// metrics[0] = width
		// metrics[1] = actualBoundingBoxAscent (glyph-specific ascent above baseline)
		// metrics[2] = actualBoundingBoxDescent (glyph-specific descent below baseline,
		// can be negative for characters above baseline like '"')
		// metrics[3] = fontBoundingBoxAscent (font-level ascent, constant for all
		// glyphs)
		// metrics[4] = fontBoundingBoxDescent (font-level descent, constant for all
		// glyphs, like Java's LineMetrics.getDescent())
		return metrics[4];
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		if ("TEAVM".equalsIgnoreCase(propertyName))
			return true;

		if ("SVG".equalsIgnoreCase(propertyName))
			return true;

		return false;
	}

	@Override
	public FileFormat getFileFormat() {
		return FileFormat.SVG;
	}
}
