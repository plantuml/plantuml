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
package net.sourceforge.plantuml.graphic;

import java.awt.Font;
import java.util.EnumSet;

import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public enum FontStyle {
	PLAIN, ITALIC, BOLD, UNDERLINE, STRIKE, WAVE, BACKCOLOR;

	public UFont mutateFont(UFont font) {
		if (this == PLAIN) {
			return font.withStyle(Font.PLAIN);
		}
		if (this == ITALIC) {
			return font.withStyle(font.getStyle() | Font.ITALIC);
		}
		if (this == BOLD) {
			return font.withStyle(font.getStyle() | Font.BOLD);
		}
		return font;
	}

	public String getActivationPattern() {
		if (this == PLAIN) {
			return "\\<[pP][lL][aA][iI][nN]\\>";
		}
		if (this == ITALIC) {
			return "\\<[iI]\\>";
		}
		if (this == BOLD) {
			return "\\<[bB]\\>";
		}
		if (this == UNDERLINE) {
			return "\\<[uU](?::(#[0-9a-fA-F]{6}|\\w+))?\\>";
		}
		if (this == WAVE) {
			return "\\<[wW](?::(#[0-9a-fA-F]{6}|\\w+))?\\>";
		}
		if (this == BACKCOLOR) {
			// return "\\<[bB][aA][cC][kK](?::(#[0-9a-fA-F]{6}|\\w+))?\\>";
			return "\\<[bB][aA][cC][kK](?::(#?\\w+(?:[-\\\\|/]#?\\w+)?))?\\>";
		}
		if (this == STRIKE) {
			return "\\<(?:s|S|strike|STRIKE|del|DEL)(?::(#[0-9a-fA-F]{6}|\\w+))?\\>";
		}
		return null;
	}

	public boolean canHaveExtendedColor() {
		if (this == UNDERLINE) {
			return true;
		}
		if (this == WAVE) {
			return true;
		}
		if (this == BACKCOLOR) {
			return true;
		}
		if (this == STRIKE) {
			return true;
		}
		return false;
	}

	public String getCreoleSyntax() {
		if (this == ITALIC) {
			return "//";
		}
		if (this == BOLD) {
			return "\\*\\*";
		}
		if (this == UNDERLINE) {
			return "__";
		}
		if (this == WAVE) {
			return "~~";
		}
		if (this == STRIKE) {
			return "--";
		}
		throw new UnsupportedOperationException();
	}

	public HColor getExtendedColor(String s) {
		final Matcher2 m = MyPattern.cmpile(getActivationPattern()).matcher(s);
		if (m.find() == false || m.groupCount() != 1) {
			return null;
		}
		final String color = m.group(1);
		if (HColorSet.instance().getColorIfValid(color) != null) {
			return HColorSet.instance().getColorIfValid(color);
		}
		return null;
	}

	public String getDeactivationPattern() {
		if (this == PLAIN) {
			return "\\</[pP][lL][aA][iI][nN]\\>";
		}
		if (this == ITALIC) {
			return "\\</[iI]\\>";
		}
		if (this == BOLD) {
			return "\\</[bB]\\>";
		}
		if (this == UNDERLINE) {
			return "\\</[uU]\\>";
		}
		if (this == WAVE) {
			return "\\</[wW]\\>";
		}
		if (this == BACKCOLOR) {
			return "\\</[bB][aA][cC][kK]\\>";
		}
		if (this == STRIKE) {
			return "\\</(?:s|S|strike|STRIKE|del|DEL)\\>";
		}
		return null;
	}

	public static FontStyle getStyle(String line) {
		for (FontStyle style : EnumSet.allOf(FontStyle.class)) {
			if (line.matches(style.getActivationPattern()) || line.matches(style.getDeactivationPattern())) {
				return style;
			}
		}
		throw new IllegalArgumentException(line);
	}

}
