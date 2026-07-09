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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;

// A throwaway CLUSTER of parallel (&) tiles, built only to answer one question:
// how tall is this run of & tiles TAKEN TOGETHER? (Its members overlap: they
// share one contact line, so their combined extent is tallest-before-contact +
// tallest-after-contact, not the sum of their heights.)
//
// It no longer POSITIONS or DRAWS anything -- the YGauge chain does that, at
// construction time, and the tile lists used for drawing stay flat. So this is
// never a member of any drawn tile list; it exists only inside
// GroupingTile.mergeParallelCore()'s height-only projection, where the sole
// method called on it is getPreferredHeight().
public class TileParallel extends CommonTile {

	public TileParallel(StringBounder stringBounder, Real currentY) {
		super(stringBounder);
	}

	private final List<Tile> tiles = new ArrayList<>();

	@Override
	public YGauge getYGauge() {
		// Never called: a TileParallel is never drawn and never chained onto
		throw new UnsupportedOperationException();
	}

	public void add(Tile tile) {
		this.tiles.add(tile);
	}

	public void drawU(UGraphic ug) {
		// Never called: the members draw themselves, from their own gauges
		throw new UnsupportedOperationException();
	}

	public double getContactPointRelative() {
		double result = 0;
		for (Tile tile : tiles)
			result = Math.max(result, tile.getContactPointRelative());

		return result;
	}

	public double getZZZ() {
		double result = 0;
		for (Tile tile : tiles)
			result = Math.max(result, tile.getZZZ());

		return result;
	}

	// The ONLY method this class exists for: the cluster's real vertical extent.
	// Its members share a contact line, so they overlap -- summing their heights
	// would count the overlap twice (this used to make groups with & messages
	// too tall).
	public double getPreferredHeight() {
		return getContactPointRelative() + getZZZ();
	}

	// Everything below is unreachable: a TileParallel never reaches a tile list
	// that gets drawn, constrained, or anchored. Kept only to satisfy the Tile
	// contract inherited from CommonTile.

	public void addConstraints() {
		throw new UnsupportedOperationException();
	}

	public Real getMinX() {
		throw new UnsupportedOperationException();
	}

	public Real getMaxX() {
		throw new UnsupportedOperationException();
	}

	public Event getEvent() {
		return null;
	}

	public boolean matchAnchor(String anchor) {
		return false;
	}

}
