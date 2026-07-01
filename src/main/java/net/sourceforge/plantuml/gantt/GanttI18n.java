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
import java.util.Locale;

public final class GanttI18n {

	private GanttI18n() {
	}

	public static String task(Locale locale) {
		final String lang = locale.getLanguage();
		switch (lang) {
		case "fr":
			return "Tâche";
		case "es":
			return "Tarea";
		case "de":
			return "Aufgabe";
		case "ja":
			return "タスク";
		case "ko":
			return "작업";
		case "ru":
			return "Задача";
		case "zh":
			return "任务";
		}
		return "Task";
	}

	public static String start(Locale locale) {
		final String lang = locale.getLanguage();
		switch (lang) {
		case "fr":
			return "Début";
		case "es":
			return "Inicio";
		case "de":
			return "Start";
		case "ja":
			return "開始";
		case "ko":
			return "시작";
		case "ru":
			return "Начало";
		case "zh":
			return "开始";
		}
		return "Start";
	}

	public static String end(Locale locale) {
		final String lang = locale.getLanguage();
		switch (lang) {
		case "fr":
			return "Fin";
		case "es":
			return "Fin";
		case "de":
			return "Ende";
		case "ja":
			return "終了";
		case "ko":
			return "끝";
		case "ru":
			return "Окончание";
		case "zh":
			return "结束";
		}
		return "End";
	}

	public static String duration(Locale locale) {
		final String lang = locale.getLanguage();
		switch (lang) {
		case "fr":
			return "Durée";
		case "es":
			return "Duración";
		case "de":
			return "Dauer";
		case "ja":
			return "期間";
		case "ko":
			return "기간";
		case "ru":
			return "Длительность";
		case "zh":
			return "持续时间";
		}
		return "Duration";
	}

	public static String durationHumanReadable(Locale locale, Duration duration) {
		long seconds = duration.getSeconds();
		final Units units = Units.of(locale.getLanguage());

		if (seconds == 0)
			return units.none;

		final long days = seconds / (24 * 3600);
		seconds %= (24 * 3600);
		final long hours = seconds / 3600;
		seconds %= 3600;
		final long minutes = seconds / 60;
		seconds %= 60;

		final StringBuilder result = new StringBuilder();
		units.append(result, days, Unit.DAY);
		units.append(result, hours, Unit.HOUR);
		units.append(result, minutes, Unit.MINUTE);
		units.append(result, seconds, Unit.SECOND);
		return result.toString();
	}

	private enum Unit {
		DAY, HOUR, MINUTE, SECOND
	}

	// Holds the localized unit labels and formatting rules for one language.
	private static final class Units {
		private final String none;
		private final String separator;
		// labels[unit.ordinal()] = { singular, plural } or, for languages with a
		// single invariable form (ja/ko/zh), just { form }. Russian uses three
		// forms: { singular, genitive-plural, paucal (2-4) }.
		private final String[][] labels;
		// true for CJK languages where the number and unit are written with no
		// space in between (e.g. "3日").
		private final boolean suffixNoSpace;

		private Units(String none, String separator, boolean suffixNoSpace, String[][] labels) {
			this.none = none;
			this.separator = separator;
			this.suffixNoSpace = suffixNoSpace;
			this.labels = labels;
		}

		void append(StringBuilder sb, long value, Unit unit) {
			if (value <= 0)
				return;
			if (sb.length() > 0)
				sb.append(separator);
			sb.append(value);
			if (suffixNoSpace == false)
				sb.append(' ');
			sb.append(label(unit.ordinal(), value));
		}

		private String label(int unit, long value) {
			final String[] forms = labels[unit];
			if (forms.length == 1)
				// Invariable form (CJK).
				return forms[0];
			if (forms.length == 3) {
				// Russian plural rules.
				final long mod100 = value % 100;
				final long mod10 = value % 10;
				if (mod10 == 1 && mod100 != 11)
					return forms[0];
				if (mod10 >= 2 && mod10 <= 4 && (mod100 < 12 || mod100 > 14))
					return forms[2];
				return forms[1];
			}
			// Western singular/plural.
			return value > 1 ? forms[1] : forms[0];
		}

		static Units of(String lang) {
			switch (lang) {
			case "fr":
				return new Units("aucune", ", ", false, new String[][] { //
						{ "jour", "jours" }, { "heure", "heures" }, //
						{ "minute", "minutes" }, { "seconde", "secondes" } });
			case "es":
				return new Units("ninguna", ", ", false, new String[][] { //
						{ "día", "días" }, { "hora", "horas" }, //
						{ "minuto", "minutos" }, { "segundo", "segundos" } });
			case "de":
				return new Units("keine", ", ", false, new String[][] { //
						{ "Tag", "Tage" }, { "Stunde", "Stunden" }, //
						{ "Minute", "Minuten" }, { "Sekunde", "Sekunden" } });
			case "ja":
				return new Units("なし", "", true, new String[][] { //
						{ "日" }, { "時間" }, { "分" }, { "秒" } });
			case "ko":
				return new Units("없음", ", ", true, new String[][] { //
						{ "일" }, { "시간" }, { "분" }, { "초" } });
			case "ru":
				return new Units("нет", ", ", false, new String[][] { //
						{ "день", "дней", "дня" }, //
						{ "час", "часов", "часа" }, //
						{ "минута", "минут", "минуты" }, //
						{ "секунда", "секунд", "секунды" } });
			case "zh":
				return new Units("无", "", true, new String[][] { //
						{ "天" }, { "小时" }, { "分钟" }, { "秒" } });
			default:
				return new Units("none", ", ", false, new String[][] { //
						{ "day", "days" }, { "hour", "hours" }, //
						{ "minute", "minutes" }, { "second", "seconds" } });
			}
		}
	}

	// Used for date-less (relative) Gantt diagrams, where columns show a day index
	// ("Day 1", "Day 2", ...) instead of a calendar date.
	public static String dayNumber(Locale locale, int dayNum) {
		final String lang = locale.getLanguage();
		switch (lang) {
		case "fr":
			return "Jour " + dayNum;
		case "es":
			return "Día " + dayNum;
		case "de":
			return "Tag " + dayNum;
		case "ja":
			return dayNum + "日目";
		case "ko":
			return dayNum + "일째";
		case "ru":
			return "День " + dayNum;
		case "zh":
			return "第" + dayNum + "天";
		}
		return "Day " + dayNum;
	}

}
