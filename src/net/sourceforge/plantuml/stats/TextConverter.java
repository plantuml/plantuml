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

import java.io.PrintStream;
import java.util.Date;

import net.sourceforge.plantuml.stats.api.Stats;
import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;

public class TextConverter {

	private final Stats stats;
	private int linesUsed;

	public TextConverter(Stats stats) {
		this.stats = stats;
	}

	public void printMe(PrintStream ps) {
		final TextTable table = new TextTable();
		table.addSeparator();
		table.addLine("ID", "Start", "Duration", "Generated", "Mean(ms)");
		// table.addLine("ID", "Start", "Last", "Parsed", "Mean(ms)", "Generated", "Mean(ms)");
		table.addSeparator();
		for (StatsLine line : stats.getLastSessions().getLines()) {
			Object id = (Long) line.getValue(StatsColumn.SESSION_ID);
			if (id == null) {
				id = "";
			}
			final Date start = (Date) line.getValue(StatsColumn.STARTING);
			// final Date end = (Date) line.getValue(StatsColumn.LAST);
			// final Long parsed = (Long) line.getValue(StatsColumn.PARSED_COUNT);
			final String duration = line.getValue(StatsColumn.DURATION_STRING).toString();
			final Long generated = (Long) line.getValue(StatsColumn.GENERATED_COUNT);
			final Long generated_ms = (Long) line.getValue(StatsColumn.GENERATED_MEAN_TIME);
			table.addLine(id, start, duration, generated, generated_ms);

		}
		table.addSeparator();
		linesUsed = table.getLines();
		table.printMe(ps);
	}

	public int getLinesUsed() {
		return linesUsed;
	}

	public static void main(String[] args) {
		StatsUtils.dumpStats();

	}

}
