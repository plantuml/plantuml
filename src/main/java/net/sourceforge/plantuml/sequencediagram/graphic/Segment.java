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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Segment {

	final private double pos1;
	final private double pos2;

	public Segment(double pos1, double pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		if (pos2 < pos1) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean equals(Object obj) {
		final Segment this2 = (Segment) obj;
		return pos1 == this2.pos1 && pos2 == this2.pos2;
	}

	@Override
	public int hashCode() {
		return Double.valueOf(pos1).hashCode() + Double.valueOf(pos2).hashCode();
	}

	final public boolean contains(double y) {
		return y >= pos1 && y <= pos2;
	}

	final public boolean contains(Segment other) {
		return contains(other.pos1) && contains(other.pos2);
	}

	@Override
	public String toString() {
		return "" + pos1 + " - " + pos2;
	}

	final public double getLength() {
		return pos2 - pos1;
	}

	final public double getPos1() {
		return pos1;
	}

	final public double getPos2() {
		return pos2;
	}

	public Segment merge(Segment this2) {
		return new Segment(Math.min(this.pos1, this2.pos1), Math.max(this.pos2, this2.pos2));
	}

	public Collection<Segment> cutSegmentIfNeed(Collection<Segment> allDelays) {
		final List<Segment> sortedDelay = new ArrayList<>(allDelays);
		Collections.sort(sortedDelay, new SortPos1());
		final List<Segment> result2 = new ArrayList<>();
		double pendingStart = pos1;
		for (Segment pause : sortedDelay) {
			if (Math.abs(pause.pos1 - pendingStart) < 0.001) {
				pendingStart = pause.pos2;
				continue;
			}
			if (pause.pos1 < pendingStart)
				continue;

			if (pause.pos1 > this.pos2) {
				if (pendingStart < this.pos2)
					result2.add(new Segment(pendingStart, this.pos2));
				return Collections.unmodifiableCollection(result2);
			}
			if (this.contains(pause)) {
				assert pendingStart < pause.pos1;
				result2.add(new Segment(pendingStart, pause.pos1));
				pendingStart = pause.pos2;
			}
		}
		if (pendingStart < this.pos2)
			result2.add(new Segment(pendingStart, this.pos2));

		return Collections.unmodifiableCollection(result2);
	}

	static class SortPos1 implements Comparator<Segment> {
		public int compare(Segment segA, Segment segB) {
			return (int) Math.signum(segA.pos1 - segB.pos1);
		}
	}

}
