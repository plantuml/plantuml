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
package net.sourceforge.plantuml.project.time;

import java.time.DayOfWeek;
import java.util.Locale;

import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.utils.I18nTimeData;

public abstract class DayOfWeekUtils {

	public static String getRegexString() {
		final StringBuilder sb = new StringBuilder();
		for (DayOfWeek day : DayOfWeek.values()) {
			if (sb.length() > 0)
				sb.append("|");

			sb.append(day.name().substring(0, 3) + "[a-z]*");
		}
		return sb.toString();
	}

	public static UBrexPart getUbrex() {
		final UBrexPart[] alternatives = new UBrexPart[DayOfWeek.values().length];
		int i = 0;
		for (DayOfWeek day : DayOfWeek.values()) {
			final String prefix = day.name().substring(0, 3);
			final StringBuilder sb = new StringBuilder();
			for (char c : prefix.toCharArray()) {
				final char upper = Character.toUpperCase(c);
				final char lower = Character.toLowerCase(c);
				sb.append('「');
				sb.append(upper);
				sb.append(lower);
				sb.append('」');
			}
			sb.append("〇*〴le");
			alternatives[i++] = new UBrexLeaf(sb.toString());
		}
		return new UBrexOr(alternatives);
	}

	public static DayOfWeek fromString(String value) {
		value = StringUtils.goUpperCase(value).substring(0, 3);
		for (DayOfWeek day : DayOfWeek.values()) {
			if (day.name().startsWith(value))
				return day;

		}
		throw new IllegalArgumentException();
	}

	public static String shortName(DayOfWeek dayOfWeek, Locale locale) {
		return I18nTimeData.shortName(dayOfWeek, locale);
	}

}
