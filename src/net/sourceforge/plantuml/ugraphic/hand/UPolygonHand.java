/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Adrian Vogt
 *
 */
package net.sourceforge.plantuml.ugraphic.hand;

import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class UPolygonHand {

	private final UPolygon poly;

	public UPolygonHand(UPolygon source) {
		final List<Point2D.Double> pt = source.getPoints();
		if (pt.size() == 0) {
			poly = new UPolygon();
			return;
		}
		final HandJiggle jiggle = new HandJiggle(pt.get(0), 1.5);
		for (int i = 1; i < pt.size(); i++) {
			jiggle.lineTo(pt.get(i));
		}
		jiggle.lineTo(pt.get(0));

		this.poly = jiggle.toUPolygon();
		this.poly.setDeltaShadow(source.getDeltaShadow());
	}

	public Shadowable getHanddrawn() {
		return this.poly;
	}

}
