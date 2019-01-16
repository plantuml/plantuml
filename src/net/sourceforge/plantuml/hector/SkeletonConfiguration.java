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
package net.sourceforge.plantuml.hector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SkeletonConfiguration {

	private final int position[];
	private final Skeleton skeleton;

	public static SkeletonConfiguration getDefault(Skeleton skeleton) {
		final Collection<Integer> rows = skeleton.getRows();
		final Map<Integer, Integer> free = new HashMap<Integer, Integer>();
		for (Integer i : rows) {
			free.put(i, 0);
		}
		final Collection<Pin> pins = skeleton.getPins();
		final int position[] = new int[pins.size()];
		for (Pin pin : pins) {
			final int f = free.get(pin.getRow());
			position[pin.getUid()] = f;
			free.put(pin.getRow(), f + 1);
		}
		return new SkeletonConfiguration(skeleton, position);
	}

	@Override
	public int hashCode() {
		int result = 0;
		for (int v : position) {
			result = result * 37 + v;
		}
		return result;
	}

	@Override
	public boolean equals(Object other) {
		final SkeletonConfiguration this2 = (SkeletonConfiguration) other;
		if (this.skeleton != this2.skeleton) {
			throw new IllegalArgumentException();
		}
		if (this.position.length != this2.position.length) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < position.length; i++) {
			if (this.position[i] != this2.position[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		final int minRow = skeleton.getRows().first();
		final int maxRow = skeleton.getRows().last();
		int minCol = Integer.MAX_VALUE;
		int maxCol = Integer.MIN_VALUE;
		for (int c : position) {
			if (c > maxCol) {
				maxCol = c;
			}
			if (c < minCol) {
				minCol = c;
			}
		}
		final StringBuilder result = new StringBuilder();
		for (int r = minRow; r <= maxRow; r++) {
			appendRow(result, r, minCol, maxCol);
			if (r < maxRow) {
				result.append(" ");
			}
		}
		return result.toString();
	}

	private void appendRow(StringBuilder result, int row, int minCol, int maxCol) {
		result.append("(");
		boolean first = true;
		for (int c = minCol; c <= maxCol; c++) {
			if (first == false) {
				result.append("-");
			}
			final Pin pin = getPin(row, c);
			if (pin == null) {
				result.append(".");
			} else {
				result.append(pin.getUid());
			}
			first = false;
		}
		result.append(")");

	}

	private Pin getPin(int row, int col) {
		for (Pin p : skeleton.getPinsOfRow(row)) {
			if (getCol(p) == col) {
				return p;
			}
		}
		return null;
	}

	public int getCol(Pin pin) {
		return position[pin.getUid()];
	}

	private SkeletonConfiguration(Skeleton skeleton, int position[]) {
		this.position = position;
		this.skeleton = skeleton;
	}

	class Switch implements SkeletonMutation {
		private final SkeletonConfiguration newConfiguration;

		public Switch(Pin pin1, Pin pin2) {
			if (pin1 == pin2) {
				throw new IllegalArgumentException();
			}
			final int copy[] = new int[position.length];
			for (int i = 0; i < position.length; i++) {
				if (i == pin1.getUid()) {
					copy[i] = position[pin2.getUid()];
				} else if (i == pin2.getUid()) {
					copy[i] = position[pin1.getUid()];
				} else {
					copy[i] = position[i];
				}
			}
			this.newConfiguration = new SkeletonConfiguration(skeleton, copy);
		}

		public SkeletonConfiguration mutate() {
			return newConfiguration;
		}
	}

	class Move implements SkeletonMutation {
		private final SkeletonConfiguration newConfiguration;

		public Move(Pin pin, int deltaX) {
			final int copy[] = new int[position.length];
			for (int i = 0; i < position.length; i++) {
				if (i == pin.getUid()) {
					copy[i] = position[i] + deltaX;
				} else {
					copy[i] = position[i];
				}
			}
			this.newConfiguration = new SkeletonConfiguration(skeleton, copy);
		}

		public SkeletonConfiguration mutate() {
			return newConfiguration;
		}
	}

	private Collection<SkeletonMutation> getMutationForRow(int row) {
		final Collection<Pin> pins = skeleton.getPinsOfRow(row);
		final Collection<Integer> usedCols = new HashSet<Integer>();
		for (Pin pin : pins) {
			usedCols.add(getCol(pin));
		}
		final Collection<SkeletonMutation> result = new ArrayList<SkeletonMutation>();
		for (Pin pin1 : pins) {
			final int c = getCol(pin1);
			if (usedCols.contains(c + 1) == false) {
				result.add(new Move(pin1, 1));
			}
			if (usedCols.contains(c - 1) == false) {
				result.add(new Move(pin1, -1));
			}
			for (Pin pin2 : pins) {
				if (pin1 == pin2) {
					continue;
				}
				if (getCol(pin1) > getCol(pin2)) {
					continue;
				}
				if (getCol(pin1) == getCol(pin2)) {
					throw new IllegalStateException();
				}
				result.add(new Switch(pin1, pin2));
			}
		}
		return result;
	}

	public Set<SkeletonConfiguration> getSomeMuteds() {
		final Set<SkeletonConfiguration> result = new HashSet<SkeletonConfiguration>();
		for (Integer row : skeleton.getRows()) {
			for (SkeletonMutation mutation : getMutationForRow(row)) {
				result.add(mutation.mutate());
			}

		}
		return result;
	}

	public List<PinLink> getPinLinks() {
		return skeleton.getPinLinks();
	}

	public double getLength(PinLink link) {
		final double x1 = getCol(link.getPin1());
		final double y1 = link.getPin1().getRow();
		final double x2 = getCol(link.getPin2());
		final double y2 = link.getPin2().getRow();
		final double dx = x2 - x1;
		final double dy = y2 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}


}
