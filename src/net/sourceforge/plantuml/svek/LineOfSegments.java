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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LineOfSegments {

	static private class Segment implements Comparable<Segment> {
		private final int idx;
		private double middle;
		private final double halfSize;

		private Segment(int idx, double x1, double x2) {
			this.idx = idx;
			this.middle = (x1 + x2) / 2;
			this.halfSize = (x2 - x1) / 2;
		}

		@Override
		public int compareTo(Segment other) {
			return Double.compare(this.middle, other.middle);
		}

		private double overlap(Segment other) {
			final double distance = other.middle - this.middle;
			if (distance < 0)
				throw new IllegalArgumentException();
			final double diff = distance - this.halfSize - other.halfSize;
			if (diff > 0)
				return 0;
			return -diff;
		}

		private void push(double delta) {
			middle += delta;
		}
	}

	private final List<Segment> all = new ArrayList<>();

	public void addSegment(double x1, double x2) {
		all.add(new Segment(all.size(), x1, x2));
	}

	public double getMean() {
		double sum = 0;
		for (Segment seg : all)
			sum += seg.middle;

		return sum / all.size();
	}

	void solveOverlapsInternal() {
		if (all.size() < 2)
			return;
		Collections.sort(all);
		for (int i = 0; i < all.size(); i++)
			if (oneLoop() == false)
				return;
	}

	private boolean oneLoop() {
		for (int i = all.size() - 2; i >= 0; i--) {
			final Segment seg1 = all.get(i);
			final Segment seg2 = all.get(i + 1);
			final double overlap = seg1.overlap(seg2);
			if (overlap > 0) {
				for (int k = i + 1; k < all.size(); k++)
					all.get(k).push(overlap);
				return true;
			}
		}
		return false;

	}

	public double[] solveOverlaps() {
		final double mean1 = getMean();
		solveOverlapsInternal();
		final double mean2 = getMean();
		final double diff = mean1 - mean2;
		if (diff != 0)
			for (Segment seg : all)
				seg.push(diff);
		final double[] result = new double[all.size()];
		for (Segment seg : all)
			result[seg.idx] = seg.middle - seg.halfSize;
		return result;

	}

}
