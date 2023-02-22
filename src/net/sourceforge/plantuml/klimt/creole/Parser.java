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
package net.sourceforge.plantuml.klimt.creole;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	public static final String MONOSPACED = "monospaced";

	public static boolean isLatexStart(String line) {
		return line.equals("<latex>");
	}

	public static boolean isLatexEnd(String line) {
		return line.equals("</latex>");
	}

	public static boolean isCodeStart(String line) {
		return line.equals("<code>");
	}

	public static boolean isCodeEnd(String line) {
		return line.equals("</code>");
	}

	public static boolean isTreeStart(String line) {
		return line.startsWith("|_");
	}

	public static double getScale(String s, double def) {
		if (s == null)
			return def;

		final Pattern p = Pattern.compile("(?:scale=|\\*)([0-9.]+)");
		final Matcher m = p.matcher(s);
		if (m.find())
			return Double.parseDouble(m.group(1));

		return def;
	}

	public static String getColor(String s) {
		if (s == null)
			return null;

		final Pattern p = Pattern.compile("color[= :](#[0-9a-fA-F]{6}|\\w+)");
		final Matcher m = p.matcher(s);
		if (m.find())
			return m.group(1);

		return null;
	}

}
