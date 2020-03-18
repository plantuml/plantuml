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
package net.sourceforge.plantuml.project;

import java.util.Iterator;

import net.sourceforge.plantuml.project.lang.Complement;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.project.time.Day;

public class DaysAsDates implements Subject, Complement, Iterable<Day> {

	private final Day date1;
	private final Day date2;

	public DaysAsDates(Day date1, Day date2) {
		this.date1 = date1;
		this.date2 = date2;
	}

	public DaysAsDates(GanttDiagram gantt, Day date1, int count) {
		this.date1 = date1;
		Day tmp = date1;
		while (count > 0) {
			if (gantt.isOpen(tmp)) {
				count--;
			}
			tmp = tmp.next();
		}
		this.date2 = tmp;
	}

	class MyIterator implements Iterator<Day> {

		private Day current;

		public MyIterator(Day current) {
			this.current = current;
		}

		public boolean hasNext() {
			return current.compareTo(date2) <= 0;
		}

		public Day next() {
			final Day result = current;
			current = current.next();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public Iterator<Day> iterator() {
		return new MyIterator(date1);
	}

}
