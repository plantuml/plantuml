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

import java.util.Arrays;
import java.util.Collection;
import java.util.function.DoubleSupplier;

public class RealUtils {

	public static RealOrigin createOrigin() {
		final RealLine line = new RealLine();
		final RealImpl result = new RealImpl("O", line, 0);
		return result;
	}

	// Like base.addFixed(...), except the offset is supplied lazily and can
	// still change after this call returns: it is re-read every time the
	// resulting Real's value is resolved (including through further
	// addFixed()/ensureBiggerThan() chaining). Use this instead of
	// addFixed() when the offset comes from a mutable field that may not
	// have reached its final value yet at the moment this is called -- see
	// RealDeltaLive for the full reasoning.
	public static Real withLiveOffset(Real base, DoubleSupplier offset) {
		return new RealDeltaLive(base, offset);
	}

	public static Real middle(Real r1, Real r2) {
		return new RealMiddle2((RealMoveable) r1, (RealMoveable) r2);
	}

	public static Real max(Real... reals) {
		return new RealMax(Arrays.asList(reals));
	}

	public static Real max(Collection<Real> reals) {
		return new RealMax(reals);
	}

	public static Real min(Real... reals) {
		return new RealMin(Arrays.asList(reals));
	}

	public static Real min(Collection<Real> reals) {
		return new RealMin(reals);
	}
}
