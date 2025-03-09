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
package net.sourceforge.plantuml.project;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.project.core.GSide;
import net.sourceforge.plantuml.utils.Direction;

public class GArrows implements UDrawable {

	final static private double delta2 = 4;

	private final GSide atEnd;
	private final HColor backColor;
	private final List<XPoint2D> points = new ArrayList<>();

	public GArrows(GSide atEnd, HColor backColor) {
		this.atEnd = atEnd;
		this.backColor = backColor;
	}

	public void drawU(UGraphic ug) {
		final UPath path = UPath.none();
		path.moveTo(points.get(0));

		for (int i = 1; i < points.size() - 1; i++)
			path.lineTo(points.get(i));
		path.lineTo(lastPointPatched());

		path.closePath();

		ug.draw(path);

		ug = ug.apply(UStroke.simple()).apply(backColor.bg());
		ug.draw(getHead());
	}

	private XPoint2D lastPointPatched() {
		final XPoint2D last = points.get(points.size() - 1);
		if (atEnd == GSide.LEFT)
			return new XPoint2D(last.getX() - 3, last.getY());
		if (atEnd == GSide.RIGHT)
			return new XPoint2D(last.getX() + 3, last.getY());
		return last;
	}

	public void addPoint(double x, double y) {
		points.add(new XPoint2D(x, y));
	}

	private UPolygon getHead() {
		final XPoint2D last = points.get(points.size() - 1);
		final double x = last.getX();
		final double y = last.getY();
		if (atEnd == GSide.LEFT) {
			final UPolygon polygon = new UPolygon("asToRight");
			polygon.addPoint(x - 4, y - delta2);
			polygon.addPoint(x, y);
			polygon.addPoint(x - 4, y + delta2);
			polygon.addPoint(x - 4, y - delta2);
			return polygon;
		}
		if (atEnd == GSide.RIGHT) {
			final UPolygon polygon = new UPolygon("asToLeft");
			polygon.addPoint(x + 4, y - delta2);
			polygon.addPoint(x, y);
			polygon.addPoint(x + 4, y + delta2);
			polygon.addPoint(x + 4, y - delta2);
			return polygon;
		}
		assert false;
		return new UPolygon();
	}

//	@Override
//	public UPolygon asToUp() {
//		final UPolygon polygon = new UPolygon("asToUp");
//		polygon.addPoint(-delta2, 0);
//		polygon.addPoint(0, 0);
//		polygon.addPoint(delta2, 0);
//		polygon.addPoint(0, -4);
//		return polygon;
//	}
//
//	@Override
//	public UPolygon asToDown() {
//		final UPolygon polygon = new UPolygon("asToDown");
//		polygon.addPoint(-delta2, 0);
//		polygon.addPoint(0, 0);
//		polygon.addPoint(delta2, 0);
//		polygon.addPoint(0, 4);
//		return polygon;
//	}
//
//	@Override
//	public UPolygon asToRight() {
//		final UPolygon polygon = new UPolygon("asToRight");
//		polygon.addPoint(0, -delta2);
//		polygon.addPoint(0, 0);
//		polygon.addPoint(0, delta2);
//		polygon.addPoint(4, 0);
//		return polygon.translate(-4, 0);
//	}
//
//	@Override
//	public UPolygon asToLeft() {
//		final UPolygon polygon = new UPolygon("asToLeft");
//		polygon.addPoint(0, -delta2);
//		polygon.addPoint(0, 0);
//		polygon.addPoint(0, delta2);
//		polygon.addPoint(-4, 0);
//		return polygon.translate(4, 0);
//	}

}
