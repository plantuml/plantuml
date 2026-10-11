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
package net.sourceforge.plantuml.klimt.drawing.font;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontContext;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.text.RichText;
import net.sourceforge.plantuml.text.StyledString;

/**
 * Measures text with the metrics of the fonts embedded in PlantUML (Liberation
 * Sans), without AWT, so that the TeaVM C build lays out exactly as the JVM does
 * on Windows, and draws with the very same fonts.
 *
 * <p>
 * It reproduces what {@code StringBounderAwt} gives on Windows, where Java maps
 * its logical font "SansSerif" to Arial (whose metrics are Liberation Sans's):
 * <ul>
 * <li>a character is as wide as its glyph's advance (fractional metrics: no
 * rounding, no kerning, no ligatures);</li>
 * <li>a logical font is a composite: its line also makes room for the fonts it
 * falls back on for other scripts, which raise its ascent and descent to at
 * least {@link #LOGICAL_MIN_ASCENT} and {@link #LOGICAL_MIN_DESCENT}; and the
 * characters it does not take from Arial (those Arial lacks, and those of
 * Arial's exclusion ranges in Java's fontconfig.properties, such as dashes and
 * typographic quotes) have the widths Java gives them on Windows, measured once
 * by tools/font-metrics/MeasureLogicalFonts.java;</li>
 * <li>a font named Arial (or Liberation Sans) is a physical font: its own line
 * height, and its missing glyph for the characters it lacks.</li>
 * </ul>
 * Other families (serif, monospace...) are measured as "SansSerif" for now: only
 * Liberation Sans is embedded.
 *
 * <p>
 * Known difference: when a text contains a character of a complex script
 * (combining marks U+0300-036F, Hebrew, Arabic, Indic, Thai, bidi controls...),
 * Java2D shapes the whole text with HarfBuzz, which can move a mark's base or
 * hide a soft hyphen. Other text (precomposed accents, Greek, Cyrillic...)
 * measures exactly as with Java2D.
 */
public class StringBounderEmbeddedFont implements StringBounder {

	/**
	 * Minimum ascent and descent of Java's logical fonts on Windows, in 2048ths of
	 * the size, as measured with Java on Windows by the rockuml project
	 * (https://github.com/h3ll5ur7er/rockuml).
	 */
	private static final double LOGICAL_MIN_ASCENT = 2059.0 / 2048.0;
	private static final double LOGICAL_MIN_DESCENT = 450.0 / 2048.0;

	private final FileFormat fileFormat;
	private final boolean windowsWidths;

	public StringBounderEmbeddedFont(FileFormat fileFormat) {
		this(fileFormat, true);
	}

	private StringBounderEmbeddedFont(FileFormat fileFormat, boolean windowsWidths) {
		this.fileFormat = fileFormat;
		this.windowsWidths = windowsWidths;
	}

	/**
	 * A bounder without the widths measured with Java on Windows, for
	 * tools/font-metrics/MeasureLogicalFonts.java only: for the characters that
	 * do not come from Arial, logical fonts use PlantUML's width table.
	 */
	public static StringBounderEmbeddedFont withoutWindowsWidths(FileFormat fileFormat) {
		return new StringBounderEmbeddedFont(fileFormat, false);
	}

	@Override
	public FileFormat getFileFormat() {
		return fileFormat;
	}

	@Override
	public XDimension2D calculateDimension(UFont font, String text) {
		// Same as StringBounderAwt
		if (RichText.isRich(text)) {
			double width = 0;
			double height = 0;
			for (StyledString s : StyledString.build(text)) {
				final UFont newFont = s.getStyle().mutateFont(font);
				final XDimension2D rect = calculateDimensionInternal(newFont, s.getText());
				width += rect.getWidth();
				height = Math.max(height, rect.getHeight());
			}
			return new XDimension2D(width, height);
		}
		return calculateDimensionInternal(font, text);
	}

	@Override
	public double getDescent(UFont font, String text) {
		final EmbeddedFontMetrics metrics = getMetrics(font.getFontFace());
		final double size = font.getSize2D();
		final double descent = relative(metrics, metrics.getDescent());
		if (isLogical(font, text))
			return Math.max(descent, LOGICAL_MIN_DESCENT) * size;
		return descent * size;
	}

	private XDimension2D calculateDimensionInternal(UFont font, String text) {
		if (text.length() == 0)
			return new XDimension2D(0, 0);

		final EmbeddedFontMetrics metrics = getMetrics(font.getFontFace());
		final boolean logical = isLogical(font, text);
		final double size = font.getSize2D();
		final int style = getStyle(font.getFontFace());

		double width = 0;
		for (int i = 0; i < text.length();) {
			final int cp = text.codePointAt(i);
			width += getRelativeAdvance(metrics, logical, style, cp);
			i += Character.charCount(cp);
		}

		double ascent = relative(metrics, metrics.getAscent());
		double descent = relative(metrics, metrics.getDescent());
		if (logical) {
			ascent = Math.max(ascent, LOGICAL_MIN_ASCENT);
			descent = Math.max(descent, LOGICAL_MIN_DESCENT);
		}
		final double leading = relative(metrics, metrics.getLineGap());
		return new XDimension2D(width * size, (ascent + descent + leading) * size);
	}

	/**
	 * Advance of a character as a fraction of the font size.
	 */
	private double getRelativeAdvance(EmbeddedFontMetrics metrics, boolean logical, int style, int cp) {
		if (isInvisible(cp))
			return 0;
		if (logical && windowsWidths) {
			final int measured = LiberationSansMetrics.WINDOWS_SANS_SERIF.getWidth(cp, style);
			if (measured >= 0)
				return measured / 2048.0;
		}
		final int advance = metrics.getAdvance(cp);
		if (advance >= 0)
			return relative(metrics, advance);
		if (logical)
			return StringBounderFromWidthTable.getRelativeWidth(cp);
		return relative(metrics, metrics.getMissingAdvance());
	}

	/**
	 * Characters Java2D replaces by an invisible glyph of no width, although the
	 * font has no glyph for them.
	 */
	private static boolean isInvisible(int cp) {
		return cp == '\t' || cp == '\n' || cp == '\r' || cp == 0x2028 || cp == 0x2029;
	}

	private static double relative(EmbeddedFontMetrics metrics, int units) {
		return units / (double) metrics.getUnitsPerEm();
	}

	private static int getStyle(UFontFace face) {
		if (face.isBold())
			return face.isItalic() ? MeasuredWidths.BOLD_ITALIC : MeasuredWidths.BOLD;
		return face.isItalic() ? MeasuredWidths.ITALIC : MeasuredWidths.REGULAR;
	}

	private static EmbeddedFontMetrics getMetrics(UFontFace face) {
		if (face.isBold())
			return face.isItalic() ? LiberationSansMetrics.BOLD_ITALIC : LiberationSansMetrics.BOLD;
		return face.isItalic() ? LiberationSansMetrics.ITALIC : LiberationSansMetrics.REGULAR;
	}

	/**
	 * Whether Java would use a logical (composite) font: anything but an explicit
	 * Arial, Java falling back to the logical font "Dialog" for unknown families.
	 */
	private static boolean isLogical(UFont font, String text) {
		final String family = firstFamily(font.getFamily(text, UFontContext.SVG));
		return family.equals("arial") == false && family.equals("liberation sans") == false;
	}

	static String firstFamily(String definition) {
		final int comma = definition.indexOf(',');
		final String first = comma == -1 ? definition : definition.substring(0, comma);
		return StringUtils.goLowerCase(first.replace('"', ' ').replace('\'', ' ').trim());
	}

}
