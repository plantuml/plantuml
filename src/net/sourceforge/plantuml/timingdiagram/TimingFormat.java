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
 */
package net.sourceforge.plantuml.timingdiagram;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class TimingFormat {
	private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	private static final GregorianCalendar gc = new GregorianCalendar(TimingFormat.GMT);

	public static final TimingFormat DECIMAL = new TimingFormat(null);
	public static final TimingFormat HOUR = new TimingFormat(null);
	public static final TimingFormat DATE = new TimingFormat(null);

	private final SimpleDateFormat sdf;

	private TimingFormat(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static TimingFormat create(SimpleDateFormat sdf) {
		return new TimingFormat(sdf);
	}

	public String formatTime(BigDecimal time) {
		if (this == HOUR || this == DATE || sdf != null)
			return formatTime(time.longValueExact());

		return time.toPlainString();
	}

	public String formatTime(long time) {
		if (sdf != null)
			return sdf.format(time * 1000L);

		if (this == HOUR) {
			final int s = (int) time % 60;
			final int m = (int) (time / 60) % 60;
			final int h = (int) (time / 3600);
			return String.format("%d:%02d:%02d", h, m, s);
		}
		if (this == DATE) {
			final int yyyy;
			final int mm;
			final int dd;
			synchronized (gc) {
				gc.setTimeInMillis(time * 1000L);
				yyyy = gc.get(Calendar.YEAR);
				mm = gc.get(Calendar.MONTH) + 1;
				dd = gc.get(Calendar.DAY_OF_MONTH);
			}
			// return String.format("%04d/%02d/%02d", yyyy, mm, dd);
			return String.format("%02d/%02d", mm, dd);
		}
		return "" + time;
	}

	public static TimeTick createDate(int yyyy, int mm, int dd, TimingFormat format) {
		final long timeInMillis;
		synchronized (gc) {
			gc.setTimeInMillis(0);
			gc.set(yyyy, mm - 1, dd);
			timeInMillis = gc.getTimeInMillis() / 1000L;
		}
		return new TimeTick(new BigDecimal(timeInMillis), format);
	}

}
