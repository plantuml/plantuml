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

class RealDelta extends RealMoveable {

	private final Real delegated;
	private final double diff;

	RealDelta(Real delegated, double diff) {
		// The debug name is intentionally NOT built from delegated.getName():
		// concatenating the full ancestor name at every wrap makes the name
		// length grow O(depth), and since addFixed()/addAtLeast() are called
		// once per tile along the Y gauge chain (now spanning the whole
		// document, not just a handful of participant columns like the X
		// chain), that is O(n) wraps each paying O(depth) for the string copy:
		// O(n^2) characters retained forever by RealLine's `all` set. This is
		// what caused the OutOfMemoryError on large diagrams.
		// The identity/uniqueness of a Real for debugging comes from
		// RealMoveable's own atomic counter ("#123_..."), not from this name,
		// so dropping the ancestry from the string is purely cosmetic.
		super(((AbstractReal) delegated).getLine(), "[Delegated d=" + diff + "]");
		this.delegated = delegated;
		this.diff = diff;
	}

	@Override
	double getCurrentValueInternal() {
		return delegated.getCurrentValue() + diff;
	}

	public Real addAtLeast(double delta) {
		return new RealDelta(delegated.addAtLeast(delta), diff);
	}

	public void ensureBiggerThan(Real other) {
		delegated.ensureBiggerThan(new RealDelta(other, -diff));
	}

	void move(double delta) {
		((RealMoveable) delegated).move(delta);
	}

}
