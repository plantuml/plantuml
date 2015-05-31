/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.MyPattern;

public class StartUtils {

	public static boolean isArobaseStartDiagram(String s) {
		s = StringUtils.trinNoTrace(s);
		return s.startsWith("@start");
	}

	public static boolean isArobaseEndDiagram(String s) {
		s = StringUtils.trinNoTrace(s);
		return s.startsWith("@end");
	}

	public static boolean isArobasePauseDiagram(String s) {
		s = StringUtils.trinNoTrace(s);
		return s.startsWith("@pause");
	}

	public static boolean isArobaseUnpauseDiagram(String s) {
		s = StringUtils.trinNoTrace(s);
		return s.startsWith("@unpause");
	}

	private static final Pattern append = MyPattern.cmpile("^\\W*@append");

	public static String getPossibleAppend(String s) {
		final Matcher m = append.matcher(s);
		if (m.find()) {
			return StringUtils.trin(s.substring(m.group(0).length()));
		}
		return null;
	}

}
