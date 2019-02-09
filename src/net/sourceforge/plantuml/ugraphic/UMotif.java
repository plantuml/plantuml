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
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.posimo.DotPath;

public class UMotif {

	private final List<Point2D> points = new ArrayList<Point2D>();

	public UMotif(int... data) {
		assert data.length % 2 == 0;
		for (int i = 0; i < data.length; i += 2) {
			points.add(new Point2D.Double(data[i], data[i + 1]));
		}
	}

	public UMotif(String s) {
		final Point2D last = new Point2D.Double();
		for (int i = 0; i < s.length(); i++) {
			final Point2D read = convertPoint(s.charAt(i));
			last.setLocation(last.getX() + read.getX(), last.getY() + read.getY());
			points.add(new Point2D.Double(last.getX(), last.getY()));
		}
	}

	double getLength() {
		return points.get(0).distance(points.get(points.size() - 1));
	}

	List<Point2D> getPoints() {
		return Collections.unmodifiableList(points);
	}

	public DotPath getRectangle(double width, double height) {
		final double len = getLength();
		final int nb1 = (int) (width / len);
		DotPath h1 = drawHorizontal(0, 0, nb1);
		final int nb2 = (int) (height / len);
		final DotPath v1 = drawVertical(h1.getEndPoint().getX(), h1.getEndPoint().getY(), nb2);
		h1 = h1.addAfter(v1);
		return h1;
	}

	static Point2D convertPoint(char c) {
		final int v = convertFromChar(c);
		final int x = v % 7;
		final int y = v / 7;
		return new Point2D.Double(x - 3, y - 3);
	}

	static int convertFromChar(char c) {
		if (c >= 'A' && c <= 'Z') {
			return c - 'A';
		}
		if (c >= 'a' && c <= 'w') {
			return c - 'a' + 26;
		}
		throw new IllegalArgumentException();
	}

	// public void drawOld(UGraphic ug, double x, double y) {
	// final UPath path = new UPath();
	// path.add(new double[] { x, y }, USegmentType.SEG_MOVETO);
	// for (Point2D p : points) {
	// path.add(new double[] { x + p.getX(), y + p.getY() },
	// USegmentType.SEG_LINETO);
	// }
	// ug.draw(0, 0, path);
	// }

	public void drawHorizontal(UGraphic ug, double x, double y, int nb) {
		final DotPath path = drawHorizontal(x, y, nb);
		ug.draw(path);
	}

	public void drawVertical(UGraphic ug, double x, double y, int nb) {
		final DotPath path = drawVertical(x, y, nb);
		ug.draw(path);
	}

	DotPath drawHorizontal(double x, double y, int nb) {
		DotPath path = new DotPath();
		for (int i = 0; i < nb; i++) {
			path = addHorizontal(x, y, path);
			x = path.getEndPoint().getX();
			y = path.getEndPoint().getY();
		}
		return path;
	}

	DotPath drawVertical(double x, double y, int nb) {
		DotPath path = new DotPath();
		for (int i = 0; i < nb; i++) {
			path = addVertical(x, y, path);
			x = path.getEndPoint().getX();
			y = path.getEndPoint().getY();
		}
		return path;
	}

	private DotPath addHorizontal(double x, double y, DotPath path) {
		double lastx = 0;
		double lasty = 0;
		for (Point2D p : points) {
			final double x1 = lastx + x;
			final double y1 = lasty + y;
			final double x2 = p.getX() + x;
			final double y2 = p.getY() + y;
			path = path.addAfter(new CubicCurve2D.Double(x1, y1, x1, y1, x2, y2, x2, y2));
			lastx = p.getX();
			lasty = p.getY();
		}
		return path;
	}

	private DotPath addVertical(double x, double y, DotPath path) {
		double lastx = 0;
		double lasty = 0;
		for (Point2D p : points) {
			final double x1 = lastx + x;
			final double y1 = lasty + y;
			final double x2 = p.getY() + x;
			final double y2 = p.getX() + y;
			path = path.addAfter(new CubicCurve2D.Double(x1, y1, x1, y1, x2, y2, x2, y2));
			lastx = p.getY();
			lasty = p.getX();
		}
		return path;
	}

}
