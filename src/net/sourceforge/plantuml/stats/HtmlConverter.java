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
package net.sourceforge.plantuml.stats;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.stats.api.Stats;
import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;
import net.sourceforge.plantuml.stats.api.StatsTable;

public class HtmlConverter {

	private final DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

	private final Stats stats;

	public HtmlConverter(Stats stats) {
		this.stats = stats;
	}

	public String toHtml() {
		final StringBuilder result = new StringBuilder();
		result.append("<html>");
		result.append("<style type=\"text/css\">");
		result.append("body { font-family: arial, helvetica, sans-serif; font-size: 12px; font-weight: normal; color: black; background: white;}");
		result.append("th,td { font-size: 12px;}");
		result.append("table { border-collapse: collapse; border-style: none;}");
		result.append("</style>");
		result.append("<h2>Statistics</h2>");
		printTableHtml(result, stats.getLastSessions());
		final StatsTable currentSessionByDiagramType = stats.getCurrentSessionByDiagramType();
		if (currentSessionByDiagramType.getLines().size() > 1) {
			result.append("<h2>Current session statistics</h2>");
			printTableHtml(result, currentSessionByDiagramType);
			result.append("<p>");
			printTableHtml(result, stats.getCurrentSessionByFormat());
		}
		result.append("<h2>General statistics since ever</h2>");
		printTableHtml(result, stats.getAllByDiagramType());
		result.append("<p>");
		printTableHtml(result, stats.getAllByFormat());
		result.append("</html>");
		return result.toString();
	}

	private void printTableHtml(StringBuilder result, StatsTable table) {
		final Collection<StatsColumn> headers = table.getColumnHeaders();
		result.append("<table border=1 cellspacing=0 cellpadding=2>");
		result.append(getHtmlHeader(headers));
		final List<StatsLine> lines = table.getLines();
		for (int i = 0; i < lines.size(); i++) {
			final StatsLine line = lines.get(i);
			final boolean bold = i == lines.size() - 1;
			result.append(getCreoleLine(headers, line, bold));

		}
		result.append("</table>");
	}

	private String getCreoleLine(Collection<StatsColumn> headers, StatsLine line, boolean bold) {
		final StringBuilder result = new StringBuilder();
		if (bold) {
			result.append("<tr bgcolor=#f0f0f0>");
		} else {
			result.append("<tr bgcolor=#fcfcfc>");
		}
		for (StatsColumn col : headers) {
			final Object v = line.getValue(col);
			if (v instanceof Long || v instanceof HumanDuration) {
				result.append("<td align=right>");
			} else {
				result.append("<td>");
			}
			if (bold) {
				result.append("<b>");
			}
			if (v instanceof Long) {
				result.append(String.format("%,d", v));
			} else if (v instanceof Date) {
				result.append(formatter.format(v));
			} else if (v == null || v.toString().length() == 0) {
				result.append(" ");
			} else {
				result.append(v.toString());
			}
			if (bold) {
				result.append("</b>");
			}
			result.append("</td>");
		}
		result.append("</tr>");
		return result.toString();
	}

	private String getHtmlHeader(Collection<StatsColumn> headers) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<tr bgcolor=#e0e0e0>");
		for (StatsColumn col : headers) {
			sb.append("<td><b>");
			sb.append(col.getTitle().replace(BackSlash.BS_BS_N, "<br>"));
			sb.append("</b></td>");
		}
		sb.append("</tr>");
		return sb.toString();
	}

}
