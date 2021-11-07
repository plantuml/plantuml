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
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GtileWithMargin extends AbstractGtileRoot implements Gtile {

	protected final AbstractGtileRoot orig;
	protected final double north;
	protected final double south;
	private final double east;

	public GtileWithMargin(AbstractGtileRoot orig, double north, double south, double east) {
		super(orig.stringBounder, orig.skinParam());
		this.orig = orig;
		this.north = north;
		this.south = south;
		this.east = east;
	}

	@Override
	public Set<Swimlane> getSwimlanes() {
		return orig.getSwimlanes();
	}

	@Override
	public Swimlane getSwimlane(String point) {
		return orig.getSwimlane(point);
	}

	@Override
	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D result = orig.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(result, east, north + south);
	}

	private UTranslate getTranslate() {
		return new UTranslate(east, north);
	}

	@Override
	protected void drawUInternal(UGraphic ug) {
		orig.drawU(ug.apply(getTranslate()));
	}

	@Override
	protected UTranslate getCoordImpl(String name) {
		return orig.getCoordImpl(name).compose(getTranslate());
	}

	@Override
	public Collection<GConnection> getInnerConnections() {
		return Collections.emptyList();
	}

}
