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
package net.sourceforge.plantuml.timingdiagram.command;

import java.math.BigDecimal;

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.timingdiagram.Clocks;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingFormat;

public class TimeTickBuilder {

	public static IRegex expressionAtWithoutArobase(String name) {
		return new RegexOr( //
				new RegexLeaf(name + "CODE", ":([\\p{L}0-9_.]+)([-+]\\d+)?"), //
				new RegexLeaf(name + "DATE", "(\\d+)/(\\d+)/(\\d+)"), //
				new RegexLeaf(name + "HOUR", "(\\d+):(\\d+):(\\d+)"), //
				new RegexLeaf(name + "DIGIT", "(\\+?)(-?\\d+\\.?\\d*)"), //
				new RegexLeaf(name + "CLOCK", "([\\p{L}0-9_.@]+)\\*(\\d+)"));
	}

	public static IRegex expressionAtWithArobase(String name) {
		return new RegexConcat( //
				new RegexLeaf("@"), //
				expressionAtWithoutArobase(name));
	}

	public static IRegex optionalExpressionAtWithArobase(String name) {
		return new RegexOptional(expressionAtWithArobase(name));
	}

	public static TimeTick parseTimeTick(String name, RegexResult arg, Clocks clock) {
		final String code = arg.get(name + "CODE", 0);
		if (code != null) {
			final String delta = arg.get(name + "CODE", 1);
			TimeTick result = clock.getCodeValue(code);
			if (delta == null) {
				return result;
			}
			final BigDecimal value = result.getTime().add(new BigDecimal(delta));
			return new TimeTick(value, TimingFormat.DECIMAL);
		}
		final String clockName = arg.get(name + "CLOCK", 0);
		if (clockName != null) {
			final int number = Integer.parseInt(arg.get(name + "CLOCK", 1));
			return clock.getClockValue(clockName, number);
		}
		final String hour = arg.get(name + "HOUR", 0);
		if (hour != null) {
			final int h = Integer.parseInt(arg.get(name + "HOUR", 0));
			final int m = Integer.parseInt(arg.get(name + "HOUR", 1));
			final int s = Integer.parseInt(arg.get(name + "HOUR", 2));
			final BigDecimal value = new BigDecimal(3600 * h + 60 * m + s);
			return new TimeTick(value, TimingFormat.HOUR);
		}
		final String date = arg.get(name + "DATE", 0);
		if (date != null) {
			final int yy = Integer.parseInt(arg.get(name + "DATE", 0));
			final int mm = Integer.parseInt(arg.get(name + "DATE", 1));
			final int dd = Integer.parseInt(arg.get(name + "DATE", 2));

			return TimingFormat.createDate(yy, mm, dd);
		}
		final String number = arg.get(name + "DIGIT", 1);
		if (number == null) {
			return clock.getNow();
		}
		final boolean isRelative = "+".equals(arg.get(name + "DIGIT", 0));
		BigDecimal value = new BigDecimal(number);
		if (isRelative && clock.getNow() != null) {
			value = clock.getNow().getTime().add(value);
		}
		return new TimeTick(value, TimingFormat.DECIMAL);
	}

}
