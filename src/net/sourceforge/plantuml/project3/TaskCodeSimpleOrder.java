/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.project3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TaskCodeSimpleOrder implements Comparator<TaskCode> {

	private final List<TaskCode> order;
	private final int hierarchySize;

	public TaskCodeSimpleOrder(Collection<TaskCode> order, int hierarchySize) {
		this.order = new ArrayList<TaskCode>(order);
		this.hierarchySize = hierarchySize;
		for (TaskCode code : order) {
			if (code.getHierarchySize() != hierarchySize) {
				throw new IllegalArgumentException();
			}
		}
	}

	public int compare(TaskCode code1, TaskCode code2) {
		if (code1.getHierarchySize() < hierarchySize) {
			throw new IllegalArgumentException();
		}
		if (code2.getHierarchySize() < hierarchySize) {
			throw new IllegalArgumentException();
		}
		code1 = code1.truncateHierarchy(hierarchySize);
		code2 = code2.truncateHierarchy(hierarchySize);
		final int idx1 = order.indexOf(code1);
		final int idx2 = order.indexOf(code2);
		return idx1 - idx2;
	}
}
