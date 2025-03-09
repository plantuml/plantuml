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
package net.sourceforge.plantuml.activitydiagram3.gtile;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.utils.MathUtils;

public class GtileColumns extends AbstractGtile {

	protected final List<Gtile> gtiles;

	private double margin;
	private double dy;

	protected final UTranslate getPosition(int pos) {
		double dx = 0;
		for (int i = 0; i < pos; i++) {
			final XDimension2D dim = gtiles.get(i).calculateDimension(getStringBounder());
			dx += dim.getWidth() + margin;
		}
		return new UTranslate(dx, dy);
	}

	protected final void setMargin(double margin) {
		if (margin < 0)
			throw new IllegalArgumentException("margin=" + margin);
		this.margin = margin;
	}

	protected final void pushDown(double height) {
		this.dy += height;
	}

	@Override
	public String toString() {
		return "GtileIfSimple " + gtiles;
	}

	public Gtile first() {
		return gtiles.get(0);
	}

	public GtileColumns(List<Gtile> gtiles, Swimlane singleSwimlane, double margin) {
		super(gtiles.get(0).getStringBounder(), gtiles.get(0).skinParam(), singleSwimlane);
		this.gtiles = gtiles;
		this.margin = margin;
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		for (int i = 0; i < gtiles.size(); i++) {
			final Gtile tile = gtiles.get(i);
			final UTranslate pos = getPosition(i);
			ug.apply(pos).draw(tile);
		}
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		XDimension2D result = new XDimension2D(0, 0);
		for (int i = 0; i < gtiles.size(); i++) {
			final XDimension2D dim = gtiles.get(i).calculateDimension(stringBounder);
			final UTranslate pos = getPosition(i);
			final XDimension2D corner = dim.applyTranslate(pos);
			result = MathUtils.maxDim(result, corner);
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
