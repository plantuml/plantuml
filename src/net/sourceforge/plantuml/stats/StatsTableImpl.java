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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;
import net.sourceforge.plantuml.stats.api.StatsTable;

public class StatsTableImpl implements StatsTable {

	private final String name;
	private final Collection<StatsColumn> columnHeaders;
	private final List<StatsLine> lines = new ArrayList<StatsLine>();

	public StatsTableImpl(String name) {
		this.name = name;
		this.columnHeaders = EnumSet.noneOf(StatsColumn.class);
	}

	public String getName() {
		return name;
	}

	public Collection<StatsColumn> getColumnHeaders() {
		return Collections.unmodifiableCollection(columnHeaders);
	}

	public List<StatsLine> getLines() {
		return Collections.unmodifiableList(lines);
	}

	public void addLine(StatsLine line) {
		this.columnHeaders.addAll(line.getColumnHeaders());
		lines.add(line);
	}

}
