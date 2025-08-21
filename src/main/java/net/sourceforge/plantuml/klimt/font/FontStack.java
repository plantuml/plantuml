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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FontStack {

	private final List<Font> fonts = new ArrayList<>();
	private final String fullDefinition;

//	private static final Set<String> availableFontFamilyNames = new TreeSet<>();
//
//	static {
//		for (String name : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
//			availableFontFamilyNames.add(name.toLowerCase());
//	}

	public FontStack(String fullDefinition) {
		this.fullDefinition = fullDefinition;
	}
	

	private List<Font> getFonts() {
		if (fonts.size() == 0)
			for (String name : fullDefinition.split(",")) {
				name = trimWhitespaceOrDoubleQuote(name);
				// final Font font = new Font(name, Font.PLAIN, 12);
				final Font font = Font.decode(name);
				fonts.add(font);
			}

		return fonts;
	}

	public int canDisplayUpTo(int index, String text) {
		return getFonts().get(index).canDisplayUpTo(text);
	}

	public Font getFont(String text, int style, int size) {
		if (getFonts().size() > 1 && text != null)
			for (Font font : getFonts())
				if (font.canDisplayUpTo(text) == -1)
					return font.deriveFont(style, (float) size);

		return getFonts().get(0).deriveFont(style, (float) size);
	}

	private static boolean isWhitespaceOrDoubleQuote(char c) {
		return Character.isWhitespace(c) || c == '"';
	}

	private static String trimWhitespaceOrDoubleQuote(String s) {
		int start = 0;
		int end = s.length();

		while (start < end && isWhitespaceOrDoubleQuote(s.charAt(start)))
			start++;

		while (end > start && isWhitespaceOrDoubleQuote(s.charAt(end - 1)))
			end--;
		return s.substring(start, end);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof FontStack))
			return false;
		FontStack other = (FontStack) obj;
		return Objects.equals(fullDefinition, other.fullDefinition);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fullDefinition);
	}

	@Override
	public String toString() {
		return "FontStack[" + fullDefinition + "]";
	}

	public String getFullDefinition() {
		return fullDefinition;
	}
}
