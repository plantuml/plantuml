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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.real;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RealLine {

	private final List<PositiveForce> forces = new ArrayList<PositiveForce>();

	private double min;
	private double max;

	void register(double v) {
		min = Math.min(min, v);
		max = Math.max(max, v);
	}

	public double getAbsoluteMin() {
		return min;
	}

	public double getAbsoluteMax() {
		return max;
	}

	public void addForce(PositiveForce force) {
		this.forces.add(force);
	}

	static private int CPT;

	public void compile() {
		int cpt = 0;
		final Map<PositiveForce, Integer> counter = new HashMap<PositiveForce, Integer>();
		do {
			min = 0;
			max = 0;
			boolean done = true;
			for (PositiveForce f : forces) {
				// System.err.println("force=" + f);
				final boolean change = f.apply();
				if (change) {
					incCounter(counter, f);
					// System.err.println("changed! " + f);
					done = false;
				}
			}
			if (done) {
				// System.err.println("cpt=" + cpt + " size=" + forces.size());
				CPT += cpt;
				// System.err.println("CPT=" + CPT);
				return;
			}
			cpt++;
			if (cpt > 99999) {
				printCounter(counter);
				throw new IllegalStateException("Inifinite Loop?");
			}
		} while (true);

	}

	private void printCounter(Map<PositiveForce, Integer> counter) {
		for (Map.Entry<PositiveForce, Integer> ent : counter.entrySet()) {
			System.err.println("count=" + ent.getValue() + " for " + ent.getKey());
		}
	}

	private static void incCounter(Map<PositiveForce, Integer> counter, PositiveForce f) {
		final Integer v = counter.get(f);
		counter.put(f, v == null ? 1 : v + 1);
	}

	Real asMaxAbsolute() {
		return new MaxAbsolute();
	}

	Real asMinAbsolute() {
		return new MinAbsolute();
	}

	class MaxAbsolute extends AbstractAbsolute {
		public double getCurrentValue() {
			return max;
		}
	}

	class MinAbsolute extends AbstractAbsolute {
		public double getCurrentValue() {
			return min;
		}
	}

	abstract class AbstractAbsolute implements Real {

		public void printCreationStackTrace() {
		}

		public String getName() {
			return getClass().getName();
		}

		public Real addFixed(double delta) {
			throw new UnsupportedOperationException();
		}

		public Real addAtLeast(double delta) {
			throw new UnsupportedOperationException();
		}

		public void ensureBiggerThan(Real other) {
			throw new UnsupportedOperationException();
		}

		public Real getMaxAbsolute() {
			return asMaxAbsolute();
		}

		public Real getMinAbsolute() {
			return asMinAbsolute();
		}

	}

}
