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

	public static String durationInDays(Locale locale, int days) {
		final String lang = locale.getLanguage();
		switch (lang) {
		case "fr":
			if (days <= 1)
				return "1 jour";
			return days + " jours";
		case "es":
			if (days <= 1)
				return "1 día";
			return days + " días";
		case "de":
			if (days <= 1)
				return "1 Tag";
			return days + " Tage";
		case "ja":
			return days + "日";
		case "ko":
			return days + "일";
		case "ru":
			if (days == 1)
				return "1 день";
			else if (days >= 2 && days <= 4)
				return days + " дня";
			else
				return days + " дней";
		case "zh":
			return days + "天";
		}
		if (days <= 1)
			return "1 day";
		return days + " days";
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
