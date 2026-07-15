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

import net.sourceforge.plantuml.klimt.drawing.LimitFinder;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.LinkAnchor;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.style.ISkinParam;

public class PlayingSpace implements Bordered {

	private final double startingY = 8;
	private final Real min;
	private final Real max;
	private final boolean isShowFootbox;

	private final List<Tile> tiles = new ArrayList<>();
	private final LivingSpaces livingSpaces;
	private final List<LinkAnchor> linkAnchors;
	private final ISkinParam skinParam;
	private final SequenceDiagram diagram;
	private final StringBounder stringBounder;

	public PlayingSpace(SequenceDiagram diagram, Dolls dolls, TileArguments tileArguments) {

		this.diagram = diagram;
		this.livingSpaces = tileArguments.getLivingSpaces();
		this.linkAnchors = diagram.getLinkAnchors();
		this.skinParam = diagram.getSkinParam();
		this.stringBounder = tileArguments.getStringBounder();

		final List<Real> min2 = new ArrayList<>();
		final List<Real> max2 = new ArrayList<>();

		min2.add(tileArguments.getXOrigin());
		max2.add(tileArguments.getXOrigin());

		if (dolls.size() > 0) {
			min2.add(dolls.getMinX(tileArguments.getStringBounder()));
			max2.add(dolls.getMaxX(tileArguments.getStringBounder()));
		}

		// Gauge chain origin: everything chains off this so gauge coordinates line
		// up with the diagram's own drawing space (startingY of headroom above
		// the first tile).
		final YGauge ycurrent = YGauge.create(tileArguments.getYOrigin().addFixed(startingY), 0);

		tiles.addAll(TileBuilder.buildSeveral(diagram.events().iterator(), tileArguments, null, ycurrent));

		for (Tile tile : tiles) {
			min2.add(tile.getMinX());
			max2.add(tile.getMaxX());
		}

		for (LivingSpace livingSpace : livingSpaces.values()) {
			max2.add(livingSpace.getPosD(tileArguments.getStringBounder()));
			max2.add(livingSpace.getPosC2(tileArguments.getStringBounder()));
		}

		this.min = RealUtils.min(min2);
		this.max = RealUtils.max(max2);

		this.isShowFootbox = diagram.isShowFootbox();
	}

	public void drawBackground(UGraphic ug) {
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(ug, true);
		drawUInternal(interceptor, false);
	}

	public void drawForeground(UGraphic ug) {
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(ug, false);
		drawUInternal(interceptor, false);
	}

	private double drawUInternal(UGraphic ug, boolean trace) {
		final StringBounder stringBounder = ug.getStringBounder();
		final List<CommonTile> local = new ArrayList<>();
		final List<CommonTile> full = new ArrayList<>();
		GroupingTile.fillPositionelTiles(stringBounder, tiles, local, full);
		// Each tile draws itself in ABSOLUTE coordinates, translating itself by its
		// own gauge min (the "self-translate prologue" every drawU() starts with),
		// so no external dy() translation is applied here
		for (CommonTile tile : local)
			tile.drawU(ug);
		for (LinkAnchor linkAnchor : linkAnchors) {
			final CommonTile ytile1 = getFromAnchor(full, linkAnchor.getAnchor1());
			final CommonTile ytile2 = getFromAnchor(full, linkAnchor.getAnchor2());
			if (ytile1 != null && ytile2 != null)
				linkAnchor.drawAnchor(ug, ytile1, ytile2, skinParam);

		}
		// Final height of this level: the last top-level tile's gauge already
		// dominates everything before it (see YGauge.createParallel's
		// max.ensureBiggerThan(currentY.getMax())), so its max IS the total height.
		// Falls back to startingY for an empty diagram.
		if (tiles.isEmpty())
			return startingY;

		return tiles.get(tiles.size() - 1).getYGauge().getMax().getCurrentValue();
	}

	private CommonTile getFromAnchor(List<CommonTile> positionedTiles, String anchor) {
		for (CommonTile ytile : positionedTiles)
			if (ytile.matchAnchor(anchor))
				return ytile;

		return null;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final LimitFinder limitFinder = LimitFinder.create(stringBounder, true);
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(limitFinder, false);
		final double finalY = drawUInternal(interceptor, false);
		final double result = Math.max(limitFinder.getMinMax().getDimension().getHeight(), finalY) + 10;
		// System.err.println("MainTile::getPreferredHeight=" + result);
		return result;
	}

	public void addConstraints() {
		for (Tile tile : tiles)
			tile.addConstraints();

		addParallelSiblingDisjointConstraints();
	}

	// A `&`-parallel sibling shares its YGauge with whichever top-level tile(s)
	// precede it (see YGauge.createParallel()), so the two can be visible on
	// the very same rows. Nothing else keeps their FOOTPRINTS
	// (getMinX()/getMaxX() -- for a GroupingTile this already includes its own
	// frame margin) from overlapping in X: addConstraints() above only makes
	// each tile internally consistent with its own participants. A
	// *sequential* pair never needs this (different Y rows, never both
	// visible at once), but a parallel pair can end up drawn on top of one
	// another -- exactly what happened with a self-message's loop/label
	// running into a `&`-parallel group's frame margin (see the `altpar_001`/
	// `altpar_007` investigation): the self-message correctly reserved room
	// against its own neighbour's lifeline, but had no way to know the
	// group's frame would additionally claim its own margin further left, and
	// no one else was reserving THAT space either.
	//
	// Clustering rule mirrors GroupingTile.computeBodyHeight()'s pending-list
	// grouping (a `&` pulls the whole preceding run into its own cluster) --
	// same shape, applied to X disjointness instead of Y height.
	private void addParallelSiblingDisjointConstraints() {
		final List<Tile> cluster = new ArrayList<>();
		for (Tile tile : tiles) {
			if (GroupingTile.isParallel(tile)) {
				for (Tile other : cluster)
					ensureDisjoint(other, tile);

				cluster.add(tile);
			} else {
				cluster.clear();
				cluster.add(tile);
			}
		}
	}

	// Pushes whichever footprint starts further right so it clears the other
	// entirely.
	//
	// CRITICAL: the direction check below must NEVER call getCurrentValue()
	// on a.getMinX()/getMaxX()/b.getMinX()/getMaxX() themselves when either
	// side is (or contains) a GroupingTile. Reason: GroupingTile.getMinX()/
	// getMaxX() are addFixed() wrappers around two PERSISTENT fields (`min`,
	// `max`, built once in the constructor via RealUtils.min()/max()), and
	// RealMin/RealMax cache their resolved value the FIRST time
	// getCurrentValue() is read -- permanently, with no invalidation (see
	// RealMin/RealMax's own `cache` field). Reading either one before compile
	// freezes it at its pre-push snapshot forever, silently discarding every
	// later ensureBiggerThan() -- and since GroupingTile.max's own formula
	// folds in `this.min.addFixed(...)` as one of its candidates, reading max
	// alone poisons BOTH fields at once. (The ASCII counterpart in
	// GroupingTile.getAsciiMinX()/getAsciiMaxX() doesn't have this problem: it
	// builds a brand new RealMin/RealMax on every call instead of caching one
	// in a field, so an early read there is harmless.) This is exactly what
	// broke the first cut of this fix: comparing getCurrentValue() the way
	// CommunicationTile.isReverse() safely does for two plain LivingSpace
	// posC's silently froze the enclosing group's frame in place when one
	// side of the pair was a GroupingTile.
	//
	// The safe alternative, mirrored from isReverse(): walk down to an actual
	// LivingSpace and compare its posB -- a genuine chained RealImpl/RealDelta,
	// never backed by a cached RealMin/RealMax, so always safe to read.
	// ensureBiggerThan() itself (called below, on aMin/bMin) is always safe
	// too, on any Real: it only registers a PositiveForce, it never triggers a
	// getCurrentValue() read.
	private void ensureDisjoint(Tile a, Tile b) {
		final Real aMin = a.getMinX();
		final Real aMax = a.getMaxX();
		final Real bMin = b.getMinX();
		final Real bMax = b.getMaxX();
		if (aMin == null || aMax == null || bMin == null || bMax == null)
			return;

		final LivingSpace aAnchor = findAnchorLivingSpace(a);
		final LivingSpace bAnchor = findAnchorLivingSpace(b);
		if (aAnchor == null || bAnchor == null)
			return;

		// Two "&"-parallel tiles that share a participant (e.g. `alice->alice`
		// & `alice->bob`, both anchored on alice) are NOT two independent
		// footprints that need pushing apart: they share the very point the
		// comparison would be based on, so "push whichever starts further
		// right" is meaningless here -- worse, since one side's bounds are
		// themselves DERIVED from that shared living space (e.g. a self-message
		// loop's own getMaxX() already includes alice's posC), asking for
		// disjointness can demand alice.posC >= alice.posC + something, a
		// self-contradictory constraint the solver can never satisfy
		// (RealLine.compile()'s fixed-point loop pushes forever and eventually
		// throws "Infinite Loop?"). Skip whenever the two sides share an
		// anchor; nothing needs reserving against itself.
		if (aAnchor == bAnchor)
			return;

		// Declaration order gives every participant's posB a monotonic initial
		// value (the same property isReverse() relies on), so this reliably
		// tells which side starts further left even before the RealLine solves.
		if (aAnchor.getPosB(stringBounder).getCurrentValue() <= bAnchor.getPosB(stringBounder).getCurrentValue())
			bMin.ensureBiggerThan(aMax);
		else
			aMin.ensureBiggerThan(bMax);
	}

	// Finds ANY LivingSpace this tile (or, for a GroupingTile, one of its
	// descendants) touches -- just needs to be SOME participant referenced by
	// the tile, since it's only used as a safe stand-in for "where is this
	// tile, roughly" (see the caching-safety comment on ensureDisjoint()
	// above). Returns null for tile kinds that don't carry a directly
	// resolvable participant (notes, dividers, references, ...) or an empty
	// group -- callers must treat null as "can't safely tell, skip".
	private LivingSpace findAnchorLivingSpace(Tile tile) {
		final Event event = tile.getEvent();
		if (event instanceof Message)
			return livingSpaces.get(((Message) event).getParticipant1());

		if (event instanceof MessageExo)
			return livingSpaces.get(((MessageExo) event).getParticipant());

		if (tile instanceof GroupingTile)
			for (Tile child : ((GroupingTile) tile).getChildTilesForAnchor()) {
				final LivingSpace found = findAnchorLivingSpace(child);
				if (found != null)
					return found;
			}

		return null;
	}

	public Real getMinX(StringBounder stringBounder) {
		return min;
	}

	public Real getMaxX(StringBounder stringBounder) {
		return max;
	}

	public boolean isShowFootbox() {
		return isShowFootbox;
	}

	public LivingSpaces getLivingSpaces() {
		return livingSpaces;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public SequenceDiagram getDiagram() {
		return diagram;
	}

	public double getBorder1() {
		return min.getCurrentValue();
	}

	public double getBorder2() {
		return max.getCurrentValue();
	}

	public List<NewpageTile> getNewpageTiles() {
		final List<NewpageTile> result = new ArrayList<>();
		for (Tile tile : tiles) {
			if (tile instanceof GroupingTile)
				((GroupingTile) tile).addNewpageTiles(result);

			if (tile instanceof NewpageTile)
				result.add((NewpageTile) tile);
		}
		return result;
	}

	public List<Double> yNewPages() {
		final List<Double> yNewPages = new ArrayList<>();
		yNewPages.add(0.0);
		for (NewpageTile newpageTile : getNewpageTiles())
			yNewPages.add(newpageTile.getYGauge().getMin().getCurrentValue());

		yNewPages.add(Double.MAX_VALUE);
		return yNewPages;
	}

	public int getNbPages() {
		return yNewPages().size() - 1;
	}

}
