/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.project2;

import java.util.HashMap;
import java.util.Map;

public class TimeConverterDay implements TimeConverter {

	private Day biggest;
	private Day smallest;
	private final double dayWith;
	private final Map<Day, Integer> map1 = new HashMap<Day, Integer>();
	private final Map<Integer, Day> map2 = new HashMap<Integer, Day>();
	private final TimeLine timeLine;

	public TimeConverterDay(TimeLine timeLine, Day start, double dayWith) {
		this.timeLine = timeLine;
		this.dayWith = dayWith;
		this.biggest = start;
		this.smallest = start;
		putDay(start, 0);
	}

//	private boolean isClosed(Day d) {
//		WeekDay wd = d.getWeekDay();
//		if (wd == WeekDay.SAT || wd == WeekDay.SUN) {
//			return true;
//		}
//		return false;
//	}

	private int getPosition(Day d) {
		Integer result = map1.get(d);
		if (result != null) {
			return result.intValue();
		}
		while (d.compareTo(biggest) > 0) {
			int n = getPosition(biggest);
			biggest = biggest.next();
			if (timeLine.isClosed(biggest) == false) {
				n++;
			}
			putDay(biggest, n);
		}
		while (d.compareTo(smallest) < 0) {
			int n = getPosition(smallest);
			smallest = smallest.previous();
			if (timeLine.isClosed(smallest) == false) {
				n--;
			}
			putDay(smallest, n);
		}
		result = map1.get(d);
		if (result != null) {
			return result.intValue();
		}
		throw new UnsupportedOperationException();
	}

	private void putDay(Day d, int n) {
		map1.put(d, n);
		map2.put(n, d);

	}

	public Day getCorrespondingElement(long position) {
		throw new UnsupportedOperationException();
	}

	public double getStartPosition(TimeElement timeElement) {
		return getPosition((Day) timeElement) * dayWith;
	}

	public double getEndPosition(TimeElement timeElement) {
		return (getPosition((Day) timeElement) + 1) * dayWith;
	}

}
