/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.ugraphic;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class UFont {

	private final Font font;
	private final String family;

	private static final Set<String> names = new HashSet<String>();

	static {
		for (String name : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
			names.add(name.toLowerCase());
		}
	}

	public UFont(String fontFamily, int fontStyle, int fontSize) {
		this(buildFont(fontFamily, fontStyle, fontSize), fontFamily);
	}

	private static Font buildFont(String fontFamily, int fontStyle, int fontSize) {
		if (fontFamily.contains(",")) {
			for (String name : fontFamily.split(",")) {
				name = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(name).trim();
				if (doesFamilyExists(name)) {
					return new Font(fontFamily, fontStyle, fontSize);
				}
			}
		}
		return new Font(fontFamily, fontStyle, fontSize);
	}

	private static boolean doesFamilyExists(String name) {
		return names.contains(name.toLowerCase());
	}

	public static UFont serif(int size) {
		return new UFont("Serif", Font.PLAIN, size);
	}

	public static UFont sansSerif(int size) {
		return new UFont("SansSerif", Font.PLAIN, size);
	}

	public static UFont courier(int size) {
		return new UFont("Courier", Font.PLAIN, size);
	}

	public static UFont byDefault(int size) {
		return sansSerif(12);
	}

	public UFont goTikz(int delta) {
		return new UFont(new Font("Serif", getStyle(), getSize() + delta), "Serif");
	}

	public static UFont monospaced(int size) {
		return new UFont("Monospaced", Font.PLAIN, size);
	}

	private UFont(Font font, String family) {
		this.font = font;
		this.family = family;
	}

	public final Font getFont() {
		return font;
	}

	@Deprecated
	public FontConfiguration toFont2(HColor color, boolean useUnderlineForHyperlink, HColor hyperlinkColor,
			int tabSize) {
		return new FontConfiguration(this, color, hyperlinkColor, useUnderlineForHyperlink, tabSize);
	}

	public UFont scaled(double scale) {
		if (scale == 1) {
			return this;
		}
		final float current = font.getSize2D();
		return withSize((float) (current * scale));
	}

	public UFont withSize(float size) {
		return new UFont(font.deriveFont(size), family);
	}

	public UFont withStyle(int style) {
		return new UFont(font.deriveFont(style), family);
	}

	public UFont bold() {
		return withStyle(Font.BOLD);
	}

	public UFont italic() {
		return withStyle(Font.ITALIC);
	}

	public int getStyle() {
		return font.getStyle();
	}

	public int getSize() {
		return font.getSize();
	}

	public double getSize2D() {
		return font.getSize2D();
	}

	public boolean isBold() {
		return font.isBold();
	}

	public boolean isItalic() {
		return font.isItalic();
	}

	public String getFamily(UFontContext context) {
		if (context == UFontContext.EPS) {
			if (family == null) {
				return "Times-Roman";
			}
			return font.getPSName();
		}
		if (context == UFontContext.SVG) {
			if (family.equalsIgnoreCase("sansserif")) {
				return "sans-serif";
			}
			return family;
		}
		return family;
	}

	@Override
	public String toString() {
		return font.toString()/* + " " + font.getPSName() */;
	}

	@Override
	public int hashCode() {
		return font.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UFont == false) {
			return false;
		}
		return this.font.equals(((UFont) obj).font);
	}

	public LineMetrics getLineMetrics(Graphics2D gg, String text) {
		final FontRenderContext frc = gg.getFontRenderContext();
		return font.getLineMetrics(text, frc);
	}

	public FontMetrics getFontMetrics() {
		return TextBlockUtils.getFontMetrics(getFont());
	}

}
