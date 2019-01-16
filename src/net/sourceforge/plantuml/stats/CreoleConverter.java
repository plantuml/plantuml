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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.plantuml.stats.api.Stats;
import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;
import net.sourceforge.plantuml.stats.api.StatsTable;

public class CreoleConverter {

	private final DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

	private final Stats stats;

	public CreoleConverter(Stats stats) {
		this.stats = stats;
	}

	public List<String> toCreole() {
		final List<String> result = new ArrayList<String>();
		result.add("<b><size:16>Statistics</b>");
		printTableCreole(result, stats.getLastSessions());
		result.add(" ");
		result.add("<b><size:16>Current session statistics</b>");
		printTableCreole(result, stats.getCurrentSessionByDiagramType());
		result.add(" ");
		printTableCreole(result, stats.getCurrentSessionByFormat());
		result.add(" ");
		result.add("<b><size:16>General statistics since ever</b>");
		printTableCreole(result, stats.getAllByDiagramType());
		result.add(" ");
		printTableCreole(result, stats.getAllByFormat());
		return result;
	}

	private void printTableCreole(List<String> strings, StatsTable table) {
		final Collection<StatsColumn> headers = table.getColumnHeaders();
		strings.add(getCreoleHeader(headers));
		final List<StatsLine> lines = table.getLines();
		for (int i = 0; i < lines.size(); i++) {
			final StatsLine line = lines.get(i);
			final boolean bold = i == lines.size() - 1;
			strings.add(getCreoleLine(headers, line, bold));

		}
	}

	private String getCreoleLine(Collection<StatsColumn> headers, StatsLine line, boolean bold) {
		final StringBuilder result = new StringBuilder();
		for (StatsColumn col : headers) {
			final Object v = line.getValue(col);
			result.append("|");
			if (v instanceof Long || v instanceof HumanDuration) {
				result.append("<r> ");
			} else {
				result.append(" ");
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
			result.append(" ");
		}
		result.append("|");
		return result.toString();
	}

	private String getCreoleHeader(Collection<StatsColumn> headers) {
		final StringBuilder sb = new StringBuilder();
		for (StatsColumn col : headers) {
			sb.append("| ");
			sb.append(col.getTitle());
			sb.append(" ");
		}
		sb.append("|");
		return sb.toString();
	}

}
