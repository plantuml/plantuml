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

import java.util.function.DoubleSupplier;

// Like RealDelta, except the offset is read live, via a DoubleSupplier,
// instead of being baked in as a fixed double at construction time. Used
// (through RealUtils.withLiveOffset()) wherever the offset comes from a
// mutable field that can still change after this Real is constructed --
// e.g. a margin grown by a self-message loop discovered later during
// layout -- so that every getCurrentValue()/ensureBiggerThan() caller
// always sees the offset's FINAL value: getCurrentValue() is only ever
// read during/after RealLine.compile(), which itself runs once, strictly
// after every such mutation has already happened, regardless of when this
// Real itself was constructed or when its constraint was registered.
class RealDeltaLive extends RealMoveable {

	private final Real delegated;
	private final DoubleSupplier delta;

	RealDeltaLive(Real delegated, DoubleSupplier delta) {
		super(((AbstractReal) delegated).getLine(), "[DelegatedLive]");
		this.delegated = delegated;
		this.delta = delta;
	}

	@Override
	double getCurrentValueInternal() {
		return delegated.getCurrentValue() + delta.getAsDouble();
	}

	public Real addAtLeast(double d) {
		return new RealDeltaLive(delegated.addAtLeast(d), delta);
	}

	public void ensureBiggerThan(Real other) {
		delegated.ensureBiggerThan(new RealDeltaLive(other, () -> -delta.getAsDouble()));
	}

	void move(double d) {
		((RealMoveable) delegated).move(d);
	}

}
