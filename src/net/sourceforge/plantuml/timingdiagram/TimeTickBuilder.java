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
 */
package net.sourceforge.plantuml.timingdiagram;

import java.math.BigDecimal;

import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;

public class TimeTickBuilder {

	private static final String WITHOUT_AROBASE = "(\\+?)(-?\\d+\\.?\\d*)";
	private static final String WITH_AROBASE = "@" + WITHOUT_AROBASE;

	public static RegexLeaf expressionAtWithoutArobase(String name) {
		return new RegexLeaf(name, WITHOUT_AROBASE);
	}

	public static RegexLeaf expressionAtWithArobase(String name) {
		return new RegexLeaf(name, WITH_AROBASE);
	}

	public static RegexLeaf optionalExpressionAtWithArobase(String name) {
		return new RegexLeaf(name, "(?:" + WITH_AROBASE + ")?");
	}

	public static TimeTick parseTimeTick(String name, RegexResult arg, Clock clock) {
		final String number = arg.get(name, 1);
		if (number == null) {
			return clock.getNow();
		}
		final boolean isRelative = "+".equals(arg.get(name, 0));
		BigDecimal value = new BigDecimal(number);
		if (isRelative) {
			value = clock.getNow().getTime().add(value);
		}
		return new TimeTick(value);
	}

}
