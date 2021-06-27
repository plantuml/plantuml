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
package net.sourceforge.plantuml.project.time;

import java.text.SimpleDateFormat;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import net.sourceforge.plantuml.StringUtils;

public enum DayOfWeek {

	MONDAY(Calendar.MONDAY), TUESDAY(Calendar.TUESDAY), WEDNESDAY(Calendar.WEDNESDAY), THURSDAY(Calendar.THURSDAY),
	FRIDAY(Calendar.FRIDAY), SATURDAY(Calendar.SATURDAY), SUNDAY(Calendar.SUNDAY);

	static final private Calendar gmt = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	static final private SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS", Locale.US);
	static {
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	private final int legacy;

	private DayOfWeek(int legacy) {
		this.legacy = legacy;
	}

	public int getLegacyJavaValue() {
		return legacy;
	}

	public static synchronized DayOfWeek fromTime(long time) {
		gmt.setTimeInMillis(time);
		final int result = gmt.get(Calendar.DAY_OF_WEEK);
		if (result == Calendar.SUNDAY) {
			return SUNDAY;
		}
		return DayOfWeek.values()[result - 2];
	}

//	private static synchronized String timeToString(Locale locale, long value) {
//		gmt.setTimeInMillis(value);
//		return fromTime(value).shortName(locale) + " " + dateFormatGmt.format(gmt.getTime());
//	}

	static public String getRegexString() {
		final StringBuilder sb = new StringBuilder();
		for (DayOfWeek day : DayOfWeek.values()) {
			if (sb.length() > 0) {
				sb.append("|");
			}
			sb.append(day.name().substring(0, 3) + "[a-z]*");
		}
		return sb.toString();
	}

	public static DayOfWeek fromString(String value) {
		value = StringUtils.goUpperCase(value).substring(0, 3);
		for (DayOfWeek day : DayOfWeek.values()) {
			if (day.name().startsWith(value)) {
				return day;
			}
		}
		throw new IllegalArgumentException();
	}

	public DayOfWeek next() {
		return DayOfWeek.values()[(ordinal() + 1) % 7];
	}

	public static DayOfWeek fromH(int h) {
		return DayOfWeek.values()[(h + 5) % 7];
	}

	public String shortName(Locale locale) {
		if (locale == Locale.ENGLISH)
			return StringUtils.capitalize(name().substring(0, 2));
		final String s = StringUtils.capitalize(
				java.time.DayOfWeek.valueOf(this.toString()).getDisplayName(TextStyle.SHORT_STANDALONE, locale));
		if (s.length() > 2)
			return s.substring(0, 2);
		return s;
	}
}
