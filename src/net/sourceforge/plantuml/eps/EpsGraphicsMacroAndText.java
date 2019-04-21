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
package net.sourceforge.plantuml.eps;

import java.io.UnsupportedEncodingException;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UFontContext;

public class EpsGraphicsMacroAndText extends EpsGraphicsMacro {

	public void drawText(String text, FontConfiguration fontConfiguration, double x, double y) {
		append(format(x) + " " + format(y) + " moveto", true);
		appendColor(getColor());
		final UFont font = fontConfiguration.getFont();
		final int size = font.getSize();
		append("/" + getPSName(fontConfiguration) + " findfont " + size + " scalefont setfont", true);
		append("100 -100 scale", true);
		append("(" + getTextAsEps(text) + ") show", false);
		append(".01 -.01 scale", true);
	}

	private String getPSName(FontConfiguration fontConfiguration) {
		final UFont font = fontConfiguration.getFont();
		final StringBuilder sb = new StringBuilder(font.getFamily(UFontContext.EPS));
		// final int style = fontConfiguration.getFont().getStyle();
		// final boolean bold = (style & Font.BOLD) != 0 || fontConfiguration.containsStyle(FontStyle.BOLD);
		// final boolean italic = (style & Font.ITALIC) != 0 || fontConfiguration.containsStyle(FontStyle.ITALIC);
		// if (bold && italic) {
		// sb.append("-BoldItalic");
		// } else if (bold) {
		// sb.append("-Bold");
		// } else if (italic) {
		// sb.append("-Italic");
		// }
		return sb.toString();
	}

	private String getTextAsEps(String text) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);
			if (c == '\\') {
				sb.append("\\\\");
			} else if (c == '(') {
				sb.append("\\(");
			} else if (c == ')') {
				sb.append("\\)");
			} else if (c < ' ') {
				sb.append("?");
			} else if (c >= ' ' && c <= 127) {
				sb.append(c);
			} else {
				final String s = "" + c;
				try {
					final byte b[] = s.getBytes("ISO-8859-1");
					if (b.length == 1) {
						final int code = b[0] & 0xFF;
						sb.append("\\" + Integer.toOctalString(code));
					} else {
						sb.append('?');
					}
				} catch (UnsupportedEncodingException e) {
					sb.append('?');
				}
			}
		}
		return sb.toString();
	}

}
