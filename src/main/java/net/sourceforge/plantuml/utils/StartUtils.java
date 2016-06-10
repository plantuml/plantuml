/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 6110 $
 *
 */
package net.sourceforge.plantuml.utils;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class StartUtils {

	public static final String PAUSE_PATTERN = "(?i)((?:\\W|\\<[^<>]*\\>)*)@unpause";
	public static final String START_PATTERN = "(?i)((?:[^\\w~]|\\<[^<>]*\\>)*)@start";

	public static boolean isArobaseStartDiagram(CharSequence s) {
		return StringUtils.trinNoTrace(s).startsWith("@start");
	}

	public static String beforeStartUml(final CharSequence2 result) {
		boolean inside = false;
		for (int i = 0; i < result.length(); i++) {
			final String single = result.subSequence(i, i + 1).toString();
			if (inside) {
				if (single.equals(">")) {
					inside = false;
				}
				continue;
			}
			if (result.subSequence(i, result.length()).startsWith("@start")) {
				return result.subSequence(0, i).toString();
			}
			if (single.equals("<")) {
				inside = true;
			} else if (single.matches("[\\w~]")) {
				return null;
			}
		}
		return null;
		// final Matcher m = MyPattern.cmpile(START_PATTERN).matcher(result);
		// if (m.find()) {
		// return m.group(1);
		// }
		// return null;
	}

	public static boolean isArobaseEndDiagram(CharSequence s) {
		return StringUtils.trinNoTrace(s).startsWith("@end");
	}

	public static boolean isArobasePauseDiagram(CharSequence s) {
		return StringUtils.trinNoTrace(s).startsWith("@pause");
	}

	public static boolean isArobaseUnpauseDiagram(CharSequence s) {
		return StringUtils.trinNoTrace(s).startsWith("@unpause");
	}

	private static final Pattern2 append = MyPattern.cmpile("^\\W*@append");

	public static CharSequence2 getPossibleAppend(CharSequence2 s) {
		final Matcher2 m = append.matcher(s);
		if (m.find()) {
			return s.subSequence(m.group(0).length(), s.length()).trin();
			// return StringUtils.trin(s.toString().substring(m.group(0).length()));
		}
		return null;
	}

}
