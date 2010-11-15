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
 * Revision $Revision: 5506 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Font;
import java.util.EnumSet;

public class FontConfiguration {

	private final EnumSet<FontStyle> styles;
	private final Font currentFont;
	private final Font motherFont;
	private final Color motherColor;
	private final Color currentColor;
	private final Color extendedColor;

	public FontConfiguration(Font font, Color color) {
		this(EnumSet.noneOf(FontStyle.class), font, color, font, color, null);
	}
	
	@Override
	public String toString() {
		return styles.toString()+" "+currentColor;
	}

	private FontConfiguration(EnumSet<FontStyle> styles, Font motherFont, Color motherColor, Font currentFont,
			Color currentColor, Color extendedColor) {
		this.styles = styles;
		this.currentFont = currentFont;
		this.motherFont = motherFont;
		this.currentColor = currentColor;
		this.motherColor = motherColor;
		this.extendedColor = extendedColor;
	}

	FontConfiguration changeColor(HtmlColor htmlColor) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont, htmlColor.getColor(), extendedColor);
	}

	FontConfiguration changeExtendedColor(Color newExtendedColor) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont, currentColor, newExtendedColor);
	}

	FontConfiguration changeSize(float size) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont.deriveFont(size), currentColor, extendedColor);
	}

	public FontConfiguration resetFont() {
		return new FontConfiguration(styles, motherFont, motherColor, motherFont, motherColor, null);
	}

	FontConfiguration add(FontStyle style) {
		final EnumSet<FontStyle> r = styles.clone();
		r.add(style);
		return new FontConfiguration(r, motherFont, motherColor, currentFont, currentColor, extendedColor);
	}

	FontConfiguration remove(FontStyle style) {
		final EnumSet<FontStyle> r = styles.clone();
		r.remove(style);
		return new FontConfiguration(r, motherFont, motherColor, currentFont, currentColor, extendedColor);
	}

	public Font getFont() {
		Font result = currentFont;
		for (FontStyle style : styles) {
			result = style.mutateFont(result);
		}
		return result;
	}

	public Color getColor() {
		return currentColor;
	}
	
	public Color getExtendedColor() {
		return extendedColor;
	}

	public boolean containsStyle(FontStyle style) {
		return styles.contains(style);
	}

}
