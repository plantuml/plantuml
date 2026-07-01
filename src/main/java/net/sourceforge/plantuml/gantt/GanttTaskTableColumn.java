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
package net.sourceforge.plantuml.gantt;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.gantt.core.Task;
import net.sourceforge.plantuml.gantt.time.TimePoint;

/**
 * Describes a single column of the textual task table drawn on the left side of
 * a Gantt diagram. Each constant knows both how to label its header and how to
 * extract the matching value from a {@link Task}, so that
 * {@link GanttTaskTable} only has to iterate over the columns it wants to
 * display.
 */
public enum GanttTaskTableColumn {

	TASK {
		@Override
		String header(Locale locale) {
			return GanttI18n.task(locale);
		}

		@Override
		String valueOf(Task task, Context context) {
			return task.getCode().getDisplay();
		}
	},

	START {
		@Override
		String header(Locale locale) {
			return GanttI18n.start(locale);
		}

		@Override
		String valueOf(Task task, Context context) {
			return context.formatDayWithTime(task.getStart());
		}
	},

	END {
		@Override
		String header(Locale locale) {
			return GanttI18n.end(locale);
		}

		@Override
		String valueOf(Task task, Context context) {
			return context.formatEnd(task.getEnd());
		}
	},

	DURATION {
		@Override
		String header(Locale locale) {
			return GanttI18n.duration(locale);
		}

		@Override
		String valueOf(Task task, Context context) {
			final LocalDateTime start = task.getStart().toLocalDateTime();
			final LocalDateTime end = task.getEnd().toLocalDateTime();

			final Duration duration = Duration.between(start, end);

			return GanttI18n.durationHumanReadable(context.getLocale(), duration);

		}
	};

	static final long SECONDS_IN_A_DAY = ChronoUnit.DAYS.getDuration().getSeconds(); // 86400

	/**
	 * Localized header label shown on the first row of the table.
	 */
	abstract String header(Locale locale);

	/**
	 * Textual value of this column for the given task.
	 */
	abstract String valueOf(Task task, Context context);

	/**
	 * Carries the table-wide state that columns need to format their values (locale
	 * and absolute-vs-relative day rendering), keeping the enum itself free of any
	 * reference to the surrounding drawing code.
	 */
	static final class Context {

		private final Locale locale;
		private final boolean relativeMode;
		private final LocalDate minDay;

		Context(Locale locale, boolean relativeMode, LocalDate minDay) {
			this.locale = locale;
			this.relativeMode = relativeMode;
			this.minDay = minDay;
		}

		Locale getLocale() {
			return locale;
		}

		String formatDay(TimePoint point) {
			if (relativeMode)
				return GanttI18n.dayNumber(locale, relativeDayNum(point));

			return point.toStringShort(locale);
		}

		// Same as formatDay, but appends the time of day when the point does not fall
		// exactly at midnight. Seconds are shown only when they are not zero, so a
		// point at 14:30:00 renders as "14:30" while 14:30:45 renders as "14:30:45".
		String formatDayWithTime(TimePoint point) {
			final String day = formatDay(point);

			final LocalDateTime dateTime = point.toLocalDateTime();
			final int hour = dateTime.getHour();
			final int minute = dateTime.getMinute();
			final int second = dateTime.getSecond();

			if (hour == 0 && minute == 0 && second == 0)
				return day;

			final String time;
			if (second == 0)
				time = String.format("%02d:%02d", hour, minute);
			else
				time = String.format("%02d:%02d:%02d", hour, minute, second);

			return day + " " + time;
		}

		// The task end is stored as an exclusive bound. When it falls exactly at
		// midnight the task spans whole days, so we step back one second to render
		// the last included day (e.g. an end at "6 00:00:00" shows as "5"). When the
		// end carries an explicit time of day, it is already the point to display and
		// must be shown as-is, down to the minute or second.
		String formatEnd(TimePoint end) {
			final LocalDateTime dateTime = end.toLocalDateTime();
			final boolean atMidnight = dateTime.getHour() == 0 && dateTime.getMinute() == 0
					&& dateTime.getSecond() == 0;

			if (atMidnight)
				return formatDay(end.minusOneSecond());

			return formatDayWithTime(end);
		}

		private int relativeDayNum(TimePoint point) {
			final int minAbs = TimePoint.ofStartOfDay(minDay).getAbsoluteDayNum();
			return point.getAbsoluteDayNum() - minAbs + 1;
		}

	}

	public static GanttTaskTableColumn of(String what) {
		try {
			return valueOf(what.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

	public static UBrexPart getUbrex() {
		final Collection<UBrexPart> definition = new ArrayList<>();
		for (GanttTaskTableColumn column : GanttTaskTableColumn.values()) {
			definition.add(new UBrexLeaf(column.toString()));
			definition.add(new UBrexLeaf(column.toString().toLowerCase()));
		}
		return new UBrexOr(definition);
	}

}
