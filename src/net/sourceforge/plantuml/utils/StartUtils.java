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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.MyPattern;

public class StartUtils {

	public static final String PAUSE_PATTERN = "(?i)((?:\\W|\\<[^<>]*\\>)*)@unpause";
	public static final String START_PATTERN = "(?i)((?:[^\\w~]|\\<[^<>]*\\>)*)@start";
	

	public static boolean isArobaseStartDiagram(CharSequence s) {
		return StringUtils.trinNoTrace(s).startsWith("@start");
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

	private static final Pattern append = MyPattern.cmpile("^\\W*@append");

	public static CharSequence2 getPossibleAppend(CharSequence2 s) {
		final Matcher m = append.matcher(s);
		if (m.find()) {
			return s.subSequence(m.group(0).length(), s.length()).trin();
			//return StringUtils.trin(s.toString().substring(m.group(0).length()));
		}
		return null;
	}

}
