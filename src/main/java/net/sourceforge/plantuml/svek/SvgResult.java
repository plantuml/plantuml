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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;

public class SvgResult {

	public static final String D_EQUALS = "d=\"";
	public static final String POINTS_EQUALS = "points=\"";

	private final String svg;
	private final Point2DFunction function;

	public SvgResult(String svg, Point2DFunction function) {
		this.svg = svg;
		this.function = function;
	}

	public PointListIterator getPointsWithThisColor(int lineColor) {
		return PointListIteratorImpl.create(this, lineColor);
	}

	public List<XPoint2D> extractList(final String searched) {
		final int p2 = this.indexOf(searched, 0);
		if (p2 == -1)
			return Collections.emptyList();

		final int p3 = this.indexOf("\"", p2 + searched.length());
		if (p3 == -1)
			return Collections.emptyList();

		return this.substring(p2 + searched.length(), p3).getPoints(" MC");
	}

	public int getIndexFromColor(int color) {
		String s = "stroke=\"" + StringUtils.goLowerCase(StringUtils.sharp000000(color)) + "\"";
		int idx = svg.indexOf(s);
		if (idx != -1)
			return idx;

		s = ";stroke:" + StringUtils.goLowerCase(StringUtils.sharp000000(color)) + ";";
		idx = svg.indexOf(s);
		if (idx != -1)
			return idx;

		s = "fill=\"" + StringUtils.goLowerCase(StringUtils.sharp000000(color)) + "\"";
		idx = svg.indexOf(s);
		if (idx != -1)
			return idx;

		// Log.info("Cannot find color=" + color + " " +
		// StringUtils.goLowerCase(StringUtils.getAsHtml(color)));
		return -1;

	}

	public List<XPoint2D> getPoints(String separator) {
		try {
			final StringTokenizer st = new StringTokenizer(svg, separator);
			final List<XPoint2D> result = new ArrayList<XPoint2D>();
			while (st.hasMoreTokens())
				result.add(getFirstPoint(st.nextToken()));

			return result;
		} catch (NumberFormatException e) {
			return Collections.emptyList();
		}
	}

	public XPoint2D getNextPoint() {
		return getFirstPoint(svg);
	}

	private XPoint2D getFirstPoint(final String tmp) {
		final StringTokenizer st = new StringTokenizer(tmp, ",");
		final double startX = Double.parseDouble(st.nextToken());
		final double startY = Double.parseDouble(st.nextToken());
		return function.apply(new XPoint2D(startX, startY));
	}

	public int indexOf(String s, int pos) {
		return svg.indexOf(s, pos);
	}

	public SvgResult substring(int pos) {
		return new SvgResult(svg.substring(pos), function);
	}

	public SvgResult substring(int start, int end) {
		return new SvgResult(svg.substring(start, end), function);
	}

	public final String getSvg() {
		return svg;
	}

	public DotPath toDotPath() {
		if (isPathConsistent() == false)
			throw new IllegalArgumentException();

		final List<XCubicCurve2D> beziers = new ArrayList<>();

		final int posC = this.indexOf("C", 0);
		if (posC == -1)
			throw new IllegalArgumentException();

		final XPoint2D start = this.substring(1, posC).getNextPoint();

		final List<DotPath.TriPoints> triPoints = new ArrayList<>();
		for (Iterator<XPoint2D> it = this.substring(posC + 1).getPoints(" ").iterator(); it.hasNext();) {
			final XPoint2D p1 = it.next();
			final XPoint2D p2 = it.next();
			final XPoint2D p = it.next();
			triPoints.add(new DotPath.TriPoints(p1, p2, p));
		}
		double x = start.getX();
		double y = start.getY();
		for (DotPath.TriPoints p : triPoints) {
			final XCubicCurve2D bezier = new XCubicCurve2D(x, y, p.x1, p.y1, p.x2, p.y2, p.x, p.y);
			beziers.add(bezier);
			x = p.x;
			y = p.y;
		}

		return DotPath.fromBeziers(beziers);
	}

	public boolean isPathConsistent() {
		if (this.getSvg().startsWith("M") == false)
			return false;

		return true;
	}

}
