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
 *
 */
package net.sourceforge.plantuml.klimt.font;

import java.awt.Font;
import java.util.Objects;

public class UFont {

	private final FontStack fontStack;
	private final int style;
	private final int size;

	public static UFont build(String fullDefinition, int fontStyle, int fontSize) {
		final FontStack fontStack = new FontStack(fullDefinition);
		return new UFont(fontStack, fontStyle, fontSize);
	}

	private UFont(FontStack fontStack, int style, int size) {
		this.fontStack = fontStack;
		this.style = style;
		this.size = size;
	}

	public static UFont serif(int size) {
		return UFont.build("Serif", Font.PLAIN, size);
	}

	public static UFont sansSerif(int size) {
		return UFont.build("SansSerif", Font.PLAIN, size);
	}

	public static UFont courier(int size) {
		return UFont.build("Courier", Font.PLAIN, size);
	}

	public static UFont byDefault(int size) {
		return sansSerif(12);
	}

	public static UFont monospaced(int size) {
		return UFont.build("Monospaced", Font.PLAIN, size);
	}

	public final Font getUnderlayingFont(String text) {
		return fontStack.getFont(text, style, size);
	}

	public UFont withSize(float size) {
		return new UFont(fontStack, this.style, (int) size);
	}

	public UFont withStyle(int style) {
		return new UFont(fontStack, style, this.size);
	}

	public UFont bold() {
		return withStyle(Font.BOLD);
	}

	public UFont italic() {
		return withStyle(Font.ITALIC);
	}

	public int getStyle() {
		return style;
	}

	public int getSize() {
		return size;
	}

	public double getSize2D() {
		return size;
	}

	public boolean isBold() {
		return (style & Font.BOLD) != 0;
	}

	public boolean isItalic() {
		return (style & Font.ITALIC) != 0;
	}

	public String getFamily(String text, UFontContext context) {
		if (context == UFontContext.EPS) {
//			if (fontStack.getFamily() == null)
//				return "Times-Roman";
			return getUnderlayingFont(text).getPSName();
		}
		if (context == UFontContext.SVG) {
			String result = fontStack.getFullDefinition().replace('\"', '\'');
			result = result.replaceAll("(?i)sansserif", "sans-serif");

			return result;
		}
		throw new IllegalArgumentException();
	}

	// Kludge for testing because font names on some machines (only macOS?) do not
	// end with <DOT><STYLE>
	// See https://github.com/plantuml/plantuml/issues/720
	private String getPortableFontName() {
		final Font font = getUnderlayingFont(null);
		final String name = font.getFontName();
		if (font.isBold() && font.isItalic())
			return name.endsWith(".bolditalic") ? name : name + ".bolditalic";
		else if (font.isBold())
			return name.endsWith(".bold") ? name : name + ".bold";
		else if (font.isItalic())
			return name.endsWith(".italic") ? name : name + ".italic";
		else
			return name.endsWith(".plain") ? name : name + ".plain";
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getPortableFontName());
		sb.append("/");
		sb.append(getSize());
		return sb.toString();
	}

	// ::comment when __HAXE__

	@Override
	public int hashCode() {
		return Objects.hash(fontStack, style, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UFont))
			return false;
		UFont other = (UFont) obj;
		return Objects.equals(fontStack, other.fontStack) && style == other.style && size == other.size;
	}

	// ::done

}
