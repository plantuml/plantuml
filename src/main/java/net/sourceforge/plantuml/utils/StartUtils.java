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
package net.sourceforge.plantuml.utils;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.text.StringLocated;

public class StartUtils {
	// ::remove file when __HAXE__

	public static final Pattern2 patternFilename = Pattern2
			.cmpile("^[@\\\\]start[^%s{}%g]+[%s{][%s%g]*([^%g]*?)[%s}%g]*$");

	public static final String PAUSE_PATTERN = "((?:\\W|\\<[^<>]*\\>)*)[@\\\\]unpause";
	public static final String START_PATTERN = "((?:[^\\w~]|\\<[^<>]*\\>)*)[@\\\\]start";

	public static String beforeStartUml(final String s) {
		boolean inside = false;
		for (int i = 0; i < s.length(); i++) {
			final String tmp = s.substring(i, s.length());
			if (startsWithSymbolAnd("start", tmp))
				return s.substring(0, i);

			final String single = s.substring(i, i + 1);
			if (inside) {
				if (single.equals(">"))
					inside = false;

				continue;
			}

			if (single.equals("<"))
				inside = true;
			else if (single.matches("[\\w~]"))
				return null;

		}
		return null;
	}

	public static boolean isArobaseStartDiagram(String s) {
		return DiagramType.getTypeFromArobaseStart(s) != DiagramType.UNKNOWN;
	}

	private static boolean startsWithSymbolAnd(String keyword, String text) {
		for (int i = 0; i < text.length(); i++) {
			final char c = text.charAt(i);
			if (Character.isWhitespace(c))
				continue;

			if (c != '@' && c != '\\')
				return false;

			if (text.length() - i - 1 < keyword.length())
				return false;

			return text.regionMatches(i + 1, keyword, 0, keyword.length());
		}
		return false;
	}

	public static boolean isArobaseEndDiagram(String s) {
		return startsWithSymbolAnd("end", s);
	}

	public static boolean isArobasePauseDiagram(String s) {
		return startsWithSymbolAnd("pause", s);
	}

	public static boolean isArobaseUnpauseDiagram(String s) {
		return startsWithSymbolAnd("unpause", s);
	}

	public static boolean isExit(CharSequence s) {
		final int len = s.length();
		int start = 0;
		int end = len - 1;

		while (start < len && Character.isWhitespace(s.charAt(start)))
			start++;

		while (end >= start && Character.isWhitespace(s.charAt(end)))
			end--;

		if (end - start + 1 != 5)
			return false;

		return s.charAt(start) == '!' && //
				s.charAt(start + 1) == 'e' && //
				s.charAt(start + 2) == 'x' && //
				s.charAt(start + 3) == 'i' && //
				s.charAt(start + 4) == 't';
	}

	private static final Pattern2 append = Pattern2.cmpile("^\\W*[@\\\\](append|a)\\b");

	public static StringLocated getPossibleAppend(StringLocated cs) {
		final String s = cs.getString();
		final Matcher2 m = append.matcher(s);
		if (m.find()) {
			final String tmp = s.substring(m.group(0).length(), s.length());
			return new StringLocated(StringUtils.trin(tmp), cs.getLocation(), cs.getPreprocessorError());
		}
		return null;
	}

}
