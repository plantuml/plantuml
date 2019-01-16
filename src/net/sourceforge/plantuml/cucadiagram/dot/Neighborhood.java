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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.svek.Bibliotekon;
import net.sourceforge.plantuml.svek.Line;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Neighborhood {

	private final ILeaf leaf;
	private final List<Link> sametailLinks;
	private final List<Link> allButSametails;

	public Neighborhood(ILeaf leaf, List<Link> sametailLinks, List<Link> all) {
		this.leaf = leaf;
		this.sametailLinks = sametailLinks;
		this.allButSametails = new ArrayList<Link>(all);
		allButSametails.removeAll(sametailLinks);
	}

	public void drawU(UGraphic ug, double minX, double minY, Bibliotekon bibliotekon, Dimension2D shapeDim) {
		final Set<Point2D> contactPoints = new HashSet<Point2D>();
		for (Link link : sametailLinks) {
			final Line line = bibliotekon.getLine(link);
			final Point2D contact = line.getStartContactPoint();
			contactPoints.add(contact);
		}
		final Rectangle2D rect = new Rectangle2D.Double(minX, minY, shapeDim.getWidth(), shapeDim.getHeight());
		final Point2D center = new Point2D.Double(rect.getCenterX(), rect.getCenterY());

		for (Point2D pt : contactPoints) {
			final Point2D inter = intersection(rect, center, pt);
			if (inter == null) {
				// System.err.println("rect=" + rect);
				// System.err.println("center=" + center);
				// System.err.println("pt=" + pt);
				assert false;
				continue;
			}
			final double theta = Math.atan2(center.getX() - pt.getX(), -(center.getY() - pt.getY()));
			final Point2D middle = drawExtends(ug, inter, theta);
			drawLine(ug, middle, pt);
		}

		for (Link link : allButSametails) {
			final Line line = bibliotekon.getLine(link);
			final Point2D contact = link.getEntity1() == leaf ? line.getStartContactPoint() : line.getEndContactPoint();
			if (contact == null) {
				assert false;
				continue;
			}
			final Point2D inter = intersection(rect, center, contact);
			if (inter == null) {
				assert false;
				continue;
			}
			drawLine(ug, inter, contact);
		}
	}

	private Point2D drawExtends(UGraphic ug, Point2D contact, double theta) {
		final UPolygon poly = new UPolygon();
		poly.addPoint(0, 0);
		poly.addPoint(7, 20);
		poly.addPoint(-7, 20);
		poly.rotate(theta);
		final UTranslate translate = new UTranslate(contact);
		ug.apply(translate).draw(poly);
		final Point2D p1 = translate.getTranslated(poly.getPoints().get(1));
		final Point2D p2 = translate.getTranslated(poly.getPoints().get(2));
		return new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
	}

	static Point2D intersection(Rectangle2D rect, Point2D pt1, Point2D pt2) {
		Point2D p;
		p = intersection(new Point2D.Double(rect.getMinX(), rect.getMinY()),
				new Point2D.Double(rect.getMaxX(), rect.getMinY()), pt1, pt2);
		if (p != null) {
			return p;
		}
		p = intersection(new Point2D.Double(rect.getMinX(), rect.getMaxY()),
				new Point2D.Double(rect.getMaxX(), rect.getMaxY()), pt1, pt2);
		if (p != null) {
			return p;
		}
		p = intersection(new Point2D.Double(rect.getMinX(), rect.getMinY()),
				new Point2D.Double(rect.getMinX(), rect.getMaxY()), pt1, pt2);
		if (p != null) {
			return p;
		}
		p = intersection(new Point2D.Double(rect.getMaxX(), rect.getMinY()),
				new Point2D.Double(rect.getMaxX(), rect.getMaxY()), pt1, pt2);
		if (p != null) {
			return p;
		}
		return null;
	}

	static private Point2D intersection(Point2D pt1, Point2D pt2, Point2D pt3, Point2D pt4) {
		// System.err.println("Checking intersection of " + pt1 + "-" + pt2 + " and " + pt3 + "-" + pt4);
		return intersection(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY(), pt3.getX(), pt3.getY(), pt4.getX(),
				pt4.getY());
	}

	private static final double epsilon = .001;

	static private Point2D intersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4,
			double y4) {
		final double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		if (d == 0) {
			return null;

		}
		final double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
		final double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

		final Point2D.Double p = new Point2D.Double(xi, yi);
		if (xi + epsilon < Math.min(x1, x2) || xi - epsilon > Math.max(x1, x2)) {
			return null;
		}
		if (xi + epsilon < Math.min(x3, x4) || xi - epsilon > Math.max(x3, x4)) {
			return null;
		}
		if (yi + epsilon < Math.min(y1, y2) || yi - epsilon > Math.max(y1, y2)) {
			return null;
		}
		if (yi + epsilon < Math.min(y3, y4) || yi - epsilon > Math.max(y3, y4)) {
			return null;
		}
		return p;
	}

	private void drawLine(UGraphic ug, Point2D pt1, Point2D pt2) {
		drawLine(ug, pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY());
	}

	private void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		final ULine line = new ULine(x2 - x1, y2 - y1);
		ug.apply(new UTranslate(x1, y1)).draw(line);
	}

}
