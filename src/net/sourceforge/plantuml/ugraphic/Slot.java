/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 7696 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

public class Slot implements Comparable<Slot> {

	private final double start;
	private final double end;

	public Slot(double start, double end) {
		if (start >= end) {
			throw new IllegalArgumentException();
		}
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		return "(" + start + "," + end + ")";
	}

	public double getStart() {
		return start;
	}

	public double getEnd() {
		return end;
	}

	public double size() {
		return end - start;
	}

	public boolean contains(double v) {
		return v >= start && v <= end;
	}

	public boolean intersect(Slot other) {
		return contains(other.start) || contains(other.end) || other.contains(start) || other.contains(end);
	}

	public Slot merge(Slot other) {
		return new Slot(Math.min(start, other.start), Math.max(end, other.end));
	}

	public int compareTo(Slot other) {
		if (this.start < other.start) {
			return -1;
		}
		if (this.start > other.start) {
			return 1;
		}
		return 0;
	}

}
