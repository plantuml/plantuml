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
package net.sourceforge.plantuml.hector;

import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.geom.LineSegmentDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

class SmartConnection {

	private final double x1;
	private final double y1;
	private final double x2;
	private final double y2;
	private final List<Box2D> forbidden;

	public SmartConnection(double x1, double y1, double x2, double y2, List<Box2D> forbidden) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.forbidden = forbidden;
	}

	public SmartConnection(Point2D p1, Point2D p2, List<Box2D> b) {
		this(p1.getX(), p1.getY(), p2.getX(), p2.getY(), b);
	}

	public void draw(UGraphic ug, HColor color) {
		final LineSegmentDouble seg = new LineSegmentDouble(x1, y1, x2, y2);
		boolean clash = intersect(seg);
		if (clash) {
			ug = ug.apply(HColorUtils.BLACK).apply(new UStroke(1.0));
		} else {
			ug = ug.apply(color).apply(new UStroke(1.5));
		}
		seg.draw(ug);
	}

	private boolean intersect(LineSegmentDouble seg) {
		for (Box2D box : forbidden) {
			if (box.doesIntersect(seg)) {
				return true;
			}
		}
		return false;
	}

	public void drawEx1(UGraphic ug, HColor color) {
		ug = ug.apply(color).apply(new UStroke(1.5));
		final double orthoX = -(y2 - y1);
		final double orthoY = x2 - x1;
		for (int i = -10; i <= 10; i++) {
			for (int j = -10; j <= 10; j++) {
				final double d1x = orthoX * i / 10.0;
				final double d1y = orthoY * i / 10.0;
				final double c1x = (x1 + x2) / 2 + d1x;
				final double c1y = (y1 + y2) / 2 + d1y;
				final double d2x = orthoX * j / 10.0;
				final double d2y = orthoY * j / 10.0;
				final double c2x = (x1 + x2) / 2 + d2x;
				final double c2y = (y1 + y2) / 2 + d2y;
				final UPath path = new UPath();
				path.moveTo(x1, y1);
				path.cubicTo(c1x, c1y, c2x, c2y, x2, y2);
				ug.draw(path);
			}
		}
	}

}
