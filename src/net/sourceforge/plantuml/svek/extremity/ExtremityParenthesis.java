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
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class ExtremityParenthesis extends Extremity {

	private final Point2D dest;
	private final double radius2 = 9;
	private final double ortho;

	private final double ang = 70;

	public ExtremityParenthesis(Point2D p1, double ortho) {
		this.dest = new Point2D.Double(p1.getX(), p1.getY());
		this.ortho = ortho;
	}
	
	@Override
	public Point2D somePoint() {
		return dest;
	}


	public void drawU(UGraphic ug) {
		final double deg = -ortho * 180 / Math.PI + 90 - ang;
		final UEllipse arc1 = new UEllipse(2 * radius2, 2 * radius2, deg, 2 * ang);
		ug.apply(new UStroke(1.5)).apply(new UTranslate(dest.getX() - radius2, dest.getY() - radius2)).draw(arc1);
	}

}
