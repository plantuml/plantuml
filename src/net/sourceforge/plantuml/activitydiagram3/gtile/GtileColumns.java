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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class GtileColumns extends AbstractGtile {

	protected final List<Gtile> gtiles;
	private final List<Dimension2D> dims = new ArrayList<>();
	protected final List<UTranslate> positions = new ArrayList<>();

	@Override
	public String toString() {
		return "GtileIfSimple " + gtiles;
	}

	public Gtile first() {
		return gtiles.get(0);
	}

	public GtileColumns(List<Gtile> gtiles, Swimlane singleSwimlane) {
		super(gtiles.get(0).getStringBounder(), gtiles.get(0).skinParam(), singleSwimlane);
		this.gtiles = gtiles;

		double dx = 0;
		for (Gtile tile : gtiles) {
			final Dimension2D dim = tile.calculateDimension(getStringBounder());
			final UTranslate pos = UTranslate.dx(dx);
			dx += dim.getWidth() + getMargin();
			dims.add(dim);
			positions.add(pos);
		}
	}

	private double getMargin() {
		return 20;
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		for (int i = 0; i < gtiles.size(); i++) {
			final Gtile tile = gtiles.get(i);
			final UTranslate pos = positions.get(i);
			ug.apply(pos).draw(tile);
		}
	}

	@Override
	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D result = new Dimension2DDouble(0, 0);
		for (int i = 0; i < dims.size(); i++) {
			final Dimension2D dim = dims.get(i);
			final UTranslate pos = positions.get(i);
			final Dimension2D corner = pos.getTranslated(dim);
			result = MathUtils.max(result, corner);
		}
		return result;
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<>();
		for (Gtile tile : gtiles)
			result.addAll(tile.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

//	public Collection<Gtile> getMyChildren() {
//		return Collections.unmodifiableCollection(gtiles);
//	}

}
