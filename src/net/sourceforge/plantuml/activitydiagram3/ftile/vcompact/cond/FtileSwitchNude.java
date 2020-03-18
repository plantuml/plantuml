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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.cond;

import java.awt.geom.Dimension2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileGeometry;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileSwitchNude extends FtileDimensionMemoize {

	protected double xSeparation = 20;

	protected final List<Ftile> tiles;
	private final Swimlane in;

	public FtileSwitchNude(List<Ftile> tiles, Swimlane in) {
		super(tiles.get(0).skinParam());
		this.tiles = tiles;
		this.in = in;
	}

	@Override
	public Collection<Ftile> getMyChildren() {
		return Collections.unmodifiableCollection(tiles);
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		if (getSwimlaneIn() != null) {
			result.add(getSwimlaneIn());
		}
		for (Ftile tile : tiles) {
			result.addAll(tile.getSwimlanes());
		}
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return in;
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (tiles.contains(child)) {
			return getTranslateNude(child, stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	protected UTranslate getTranslateNude(Ftile tile, StringBounder stringBounder) {
		double x1 = 0;
		for (Ftile candidate : tiles) {
			final FtileGeometry dim1 = candidate.calculateDimension(stringBounder);
			if (candidate == tile) {
				return UTranslate.dx(x1);
			}
			x1 += dim1.getWidth() + xSeparation;
		}
		throw new IllegalArgumentException();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		for (Ftile tile : tiles) {
			ug.apply(getTranslateNude(tile, stringBounder)).draw(tile);
		}
	}

	@Override
	protected FtileGeometry calculateDimensionFtile(StringBounder stringBounder) {
		final FtileGeometry dimTotal = calculateDimensionInternal(stringBounder);
		for (Ftile tile : tiles)
			if (tile.calculateDimension(stringBounder).hasPointOut()) {
				return dimTotal;
			}
		return dimTotal.withoutPointOut();
	}

	@Override
	protected FtileGeometry calculateDimensionInternalSlow(StringBounder stringBounder) {
		Dimension2D result = new Dimension2DDouble(0, 0);
		for (Ftile couple : tiles) {
			result = Dimension2DDouble.mergeLR(result, couple.calculateDimension(stringBounder));
		}
		result = Dimension2DDouble.delta(result, xSeparation * (tiles.size() - 1), 100);

		return new FtileGeometry(result, result.getWidth() / 2, 0);
	}

}
