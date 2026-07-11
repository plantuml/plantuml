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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.real.Real;

public final class YGauge {

	public static final boolean USE_ME = true;

	private final Real min;
	private final Real max;
	// Contact line shared by the members of a parallel (&) group: the
	// absolute Y of the arrow line. Null for tiles without a contact line.
	private final Real contact;
	// Chaining origin of the parallel group (the max of the gauge preceding
	// the group head): no member of the group may start above it. It mirrors
	// the legacy TileParallel behavior where the whole group is anchored at
	// the group top and members are shifted DOWN to align contact points.
	private final Real origin;

	public YGauge(Real min, Real max) {
		this(min, max, null, null);
	}

	public YGauge(Real min, Real max, Real contact, Real origin) {
		this.min = min;
		this.max = max;
		this.contact = contact;
		this.origin = origin;
	}

	@Override
	public String toString() {
		return min.getCurrentValue() + " " + max.getCurrentValue();
	}

	public static YGauge create(Real min, double height) {
		if (height < 0)
			throw new IllegalArgumentException();
		// addAtLeast (not addFixed): see the note on chain anchoring below
		return new YGauge(min, min.addAtLeast(height));
	}

	// CHAIN ANCHORING (performance-critical, do not "simplify" back to
	// addFixed):
	//
	// RealDelta.addAtLeast() delegates DOWNWARD --
	// `new RealDelta(delegated.addAtLeast(delta), diff)` -- so calling
	// addAtLeast on a RealDelta recurses through every layer below it and
	// rebuilds one RealDelta per layer. RealImpl.addAtLeast(), in contrast,
	// creates a single new RealImpl in O(1).
	//
	// The Y gauge chain links each tile to the previous one, so the link
	// carried from gauge to gauge (the `max`, which becomes the next tile's
	// `origin`) must ALWAYS be an anchored moveable (RealImpl), never a
	// RealDelta. Otherwise every tile stacks one more RealDelta layer and
	// tile n pays O(n) to chain: O(n^2) objects, O(n) recursion depth, and
	// the OutOfMemoryError / deep `RealDelta.addAtLeast` stack seen on large
	// diagrams (see YGAUGE.md).
	//
	// addFixed remains fine for LEAF values that nothing chains onto (e.g.
	// the `min` below, read only for drawing).

	// For tiles owning a contact line (message arrows). The min FLOATS below
	// the chaining origin: contact = origin.addAtLeast(contactRelative), so
	// that a later parallel (&) member with a taller label can push this tile
	// down through an ensureBiggerThan on the shared contact line. When no
	// parallel member follows, the solver minimization gives
	// contact = origin + contactRelative, that is min = origin: exactly the
	// plain sequential chaining.
	public static YGauge createWithContact(YGauge currentY, double contactRelative, double height) {
		if (height < 0 || contactRelative < 0)
			throw new IllegalArgumentException();
		final Real origin = currentY.getMax();
		final Real contact = origin.addAtLeast(contactRelative);
		// min is a leaf (drawing only): addFixed is safe and keeps it exactly
		// contactRelative above the contact line
		final Real min = contact.addFixed(-contactRelative);
		// max is the chain link: it MUST be an anchored moveable, so it is
		// built with addAtLeast on the contact (a RealImpl), not with addFixed
		// on min (a RealDelta)
		final Real max = contact.addAtLeast(height - contactRelative);
		return new YGauge(min, max, contact, origin);
	}

	// For a parallel (&) tile: aligns its contact line with the shared
	// contact line of the group, and pushes that line down if this member's
	// label is taller (contact >= groupTop + contactRelative). The max
	// accumulates over the members so that the next sequential tile chains
	// below the WHOLE group, like the legacy
	// TileParallel.getPreferredHeight().
	public static YGauge createParallel(YGauge currentY, double contactRelative, double height) {
		final Real contact = currentY.getContact();
		final Real origin = currentY.getOrigin();
		if (contact == null || origin == null)
			// The previous tile has no contact line: fall back to top alignment
			return create(currentY.getMin(), height);

		contact.ensureBiggerThan(origin.addFixed(contactRelative));
		final Real min = contact.addFixed(-contactRelative);
		// CAUTION: do NOT use RealUtils.max() here. RealMax is a post-compile,
		// read-only combinator: addAtLeast/ensureBiggerThan are unsupported
		// (would crash on the next chained tile) and its getCurrentValue is
		// cached on first read, so using it as a PositiveForce source would
		// freeze a stale value in the middle of RealLine.compile(). The group
		// max is expressed instead as a moveable Real with two constraints:
		// the solver minimization computes the same maximum.
		// It is built from the contact (a RealImpl) so that the chain link
		// stays anchored -- see the CHAIN ANCHORING note above.
		final Real max = contact.addAtLeast(height - contactRelative);
		max.ensureBiggerThan(currentY.getMax());
		return new YGauge(min, max, contact, origin);
	}

	// For zero-side tiles (life events, ...) placed between the members of a
	// parallel group: the contact line and origin are propagated so that a
	// following & tile still finds them, mirroring the legacy
	// moveRecentParallelTilesToPending which skips through LifeEventTiles.
	public static YGauge createPropagating(YGauge currentY, double height) {
		if (height < 0)
			throw new IllegalArgumentException();
		final Real min = currentY.getMax();
		// addAtLeast: the max is a chain link (see CHAIN ANCHORING above)
		return new YGauge(min, min.addAtLeast(height), currentY.getContact(), currentY.getOrigin());
	}

	public final Real getMin() {
		return min;
	}

	public final Real getMax() {
		return max;
	}

	public final Real getContact() {
		return contact;
	}

	public final Real getOrigin() {
		return origin;
	}

}
