/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6837 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.util.EnumSet;

import net.sourceforge.plantuml.ugraphic.UFont;

public class FontConfiguration {

	private final EnumSet<FontStyle> styles;
	private final UFont currentFont;
	private final UFont motherFont;
	private final HtmlColor motherColor;
	private final HtmlColor currentColor;
	private final HtmlColor extendedColor;

	public FontConfiguration(UFont font, HtmlColor color) {
		this(getStyles(font), font, color, font, color, null);
	}

	private static EnumSet<FontStyle> getStyles(UFont font) {
		final boolean bold = font.isBold();
		final boolean italic = font.isItalic();
		if (bold && italic) {
			return EnumSet.of(FontStyle.ITALIC, FontStyle.BOLD);
		}
		if (bold) {
			return EnumSet.of(FontStyle.BOLD);
		}
		if (italic) {
			return EnumSet.of(FontStyle.ITALIC);
		}
		return EnumSet.noneOf(FontStyle.class);
	}

	@Override
	public String toString() {
		return styles.toString() + " " + currentColor;
	}

	private FontConfiguration(EnumSet<FontStyle> styles, UFont motherFont, HtmlColor motherColor, UFont currentFont,
			HtmlColor currentColor, HtmlColor extendedColor) {
		this.styles = styles;
		this.currentFont = currentFont;
		this.motherFont = motherFont;
		this.currentColor = currentColor;
		this.motherColor = motherColor;
		this.extendedColor = extendedColor;
	}

	FontConfiguration changeColor(HtmlColor htmlColor) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont, htmlColor, extendedColor);
	}

	FontConfiguration changeExtendedColor(HtmlColor newExtendedColor) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont, currentColor, newExtendedColor);
	}

	FontConfiguration changeSize(float size) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont.deriveSize(size), currentColor,
				extendedColor);
	}

	public FontConfiguration resetFont() {
		return new FontConfiguration(styles, motherFont, motherColor, motherFont, motherColor, null);
	}

	FontConfiguration add(FontStyle style) {
		final EnumSet<FontStyle> r = styles.clone();
		r.add(style);
		return new FontConfiguration(r, motherFont, motherColor, currentFont, currentColor, extendedColor);
	}

	public FontConfiguration italic() {
		return add(FontStyle.ITALIC);
	}

	FontConfiguration remove(FontStyle style) {
		final EnumSet<FontStyle> r = styles.clone();
		r.remove(style);
		return new FontConfiguration(r, motherFont, motherColor, currentFont, currentColor, extendedColor);
	}

	public UFont getFont() {
		UFont result = currentFont;
		for (FontStyle style : styles) {
			result = style.mutateFont(result);
		}
		return result;
	}

	public HtmlColor getColor() {
		return currentColor;
	}

	public HtmlColor getExtendedColor() {
		return extendedColor;
	}

	public boolean containsStyle(FontStyle style) {
		return styles.contains(style);
	}

}
