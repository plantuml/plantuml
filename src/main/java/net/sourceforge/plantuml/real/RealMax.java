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
package net.sourceforge.plantuml.real;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class RealMax extends AbstractReal implements Real {

	// Guard against a cyclic dependency graph (a RealMax that ends up, through
	// some chain of delegation, depending on its own current value) without
	// paying for a stack trace capture on every evaluation. Recursion here
	// means getCurrentValueInternal() being re-entered on the same thread
	// before an outer call has returned, so a thread-local counter of "how
	// many RealMax evaluations are currently on this thread's call stack" is
	// an exact, cheap substitute for inspecting the real stack depth.
	//
	// This used to be `new Throwable().fillInStackTrace().length > 1000`,
	// re-checked on every element of every RealMax on the diagram (thousands
	// of captures for a large sequence diagram). That is affordable on the
	// JVM, where fillInStackTrace() is a fast native call, but not on targets
	// compiled through TeaVM: walking a native JavaScript stack is far more
	// expensive there, and got worse still once sequence diagram Y positioning
	// switched to real call recursion instead of a shallow state machine
	// (commit d087fc0), which is what turned this guard into a measurable
	// slowdown (plantuml/plantuml#2834).
	private static final int MAX_RECURSION_DEPTH = 1000;
	private static final ThreadLocal<int[]> RECURSION_DEPTH = new ThreadLocal<int[]>() {
		@Override
		protected int[] initialValue() {
			return new int[1];
		}
	};

	private final List<Real> all = new ArrayList<>();
	private final RealDebug creationPoint;

	RealMax(Collection<Real> reals) {
		super(line(reals));
		this.all.addAll(reals);
		// See RealDebug: capturing a stack trace here is only useful to print
		// where a faulty RealMax was created if the recursion guard below ever
		// fires, so it is opt-in rather than paid by every RealMax instance.
		this.creationPoint = RealDebug.create();
	}

	static RealLine line(Collection<Real> reals) {
		return ((AbstractReal) reals.iterator().next()).getLine();
	}

	public String getName() {
		return "max " + all.size();
	}

	private double cache = Double.MAX_VALUE;

	@Override
	double getCurrentValueInternal() {
		if (cache != Double.MAX_VALUE)
			return cache;
		final int[] depth = RECURSION_DEPTH.get();
		depth[0]++;
		try {
			if (depth[0] > MAX_RECURSION_DEPTH) {
				System.err.println("The faulty RealMax " + getName());
				System.err.println("has been created here:");
				printCreationStackTrace();
				throw new IllegalStateException("Infinite recursion?");
			}
			double result = all.get(0).getCurrentValue();
			for (int i = 1; i < all.size(); i++) {
				final double v = all.get(i).getCurrentValue();
				if (v > result)
					result = v;

			}
			cache = result;
			return result;
		} finally {
			depth[0]--;
		}
	}

	public Real addFixed(double delta) {
		return new RealDelta(this, delta);
	}

	public Real addAtLeast(double delta) {
		throw new UnsupportedOperationException();
	}

	public void ensureBiggerThan(Real other) {
		throw new UnsupportedOperationException();
	}

	public void printCreationStackTrace() {
		RealDebug.printCreationStackTrace(creationPoint);
	}

}
