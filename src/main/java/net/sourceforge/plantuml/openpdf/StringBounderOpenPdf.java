/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
package net.sourceforge.plantuml.openpdf;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openpdf.text.DocumentException;
import org.openpdf.text.pdf.BaseFont;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontContext;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class StringBounderOpenPdf implements StringBounder {

	private static final Map<String, BaseFont> fontCache = new ConcurrentHashMap<>();

	@Override
	public double getDescent(UFont font, String text) {
		// Same reasoning as in calculateDimension: use the *font*'s descender
		// (constant for the font), not the per-glyph descent of `text`. The
		// font descriptor returns a negative value, so we negate it to match
		// the StringBounder contract (positive descent).
		final BaseFont baseFont = resolveFont(font);
		final int fontSize = font.getSize();
		final float fontDescent = baseFont.getFontDescriptor(BaseFont.DESCENT, fontSize);
		return -fontDescent;
	}

	@Override
	public XDimension2D calculateDimension(UFont font, String text) {
		final BaseFont baseFont = resolveFont(font);
		final int fontSize = font.getSize();

		// --- Width: per-glyph, that's what we actually want ---
		final float width = baseFont.getWidthPoint(text, fontSize);

		// --- Height: must come from the *font*'s metrics, NOT the per-glyph
		// metrics of `text`.
		//
		// AWT's getStringBounds() returns ascent + descent + leading of the
		// *whole font*. We replicate that with the font-descriptor ascender
		// and descender (which are constants for the font, independent of
		// `text`), plus a 15% leading - the standard typographic default that
		// matches what AWT computes for Helvetica/Times/Courier within a few
		// hundredths of a point.
		final float fontAscent = baseFont.getFontDescriptor(BaseFont.ASCENT, fontSize);
		final float fontDescent = baseFont.getFontDescriptor(BaseFont.DESCENT, fontSize); // negative
		final float leading = 0.15f * fontSize;
		final float height = fontAscent - fontDescent + leading;

		final XDimension2D result = new XDimension2D(width, height);
		return result;
	}
	@Override
	public FileFormat getFileFormat() {
		return FileFormat.PDF;
	}

	public static BaseFont resolveFont(UFont ufont) {
		final UFontFace face = ufont.getFontFace();

		final boolean bold = face.isBold();
		final boolean italic = face.isItalic();

		return resolveFont(ufont.getFamily(null, UFontContext.PDF), bold, italic);
	}

	private static BaseFont resolveFont(String family, boolean bold, boolean italic) {
		if (family == null)
			family = "Helvetica";

		final String key = family.toLowerCase() + (bold ? "-b" : "") + (italic ? "-i" : "");
		final BaseFont cached = fontCache.get(key);
		if (cached != null)
			return cached;

		final String baseName;
		final String lower = family.toLowerCase();
		if (lower.contains("courier") || lower.contains("mono")) {
			if (bold && italic)
				baseName = BaseFont.COURIER_BOLDOBLIQUE;
			else if (bold)
				baseName = BaseFont.COURIER_BOLD;
			else if (italic)
				baseName = BaseFont.COURIER_OBLIQUE;
			else
				baseName = BaseFont.COURIER;
		} else if (lower.contains("times") || lower.contains("serif")) {
			if (bold && italic)
				baseName = BaseFont.TIMES_BOLDITALIC;
			else if (bold)
				baseName = BaseFont.TIMES_BOLD;
			else if (italic)
				baseName = BaseFont.TIMES_ITALIC;
			else
				baseName = BaseFont.TIMES_ROMAN;
		} else {
			if (bold && italic)
				baseName = BaseFont.HELVETICA_BOLDOBLIQUE;
			else if (bold)
				baseName = BaseFont.HELVETICA_BOLD;
			else if (italic)
				baseName = BaseFont.HELVETICA_OBLIQUE;
			else
				baseName = BaseFont.HELVETICA;
		}

		try {
			final BaseFont bf = BaseFont.createFont(baseName, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			fontCache.put(key, bf);
			return bf;
		} catch (DocumentException | IOException e) {
			throw new IllegalStateException("Cannot load font " + baseName, e);
		}
	}

}
