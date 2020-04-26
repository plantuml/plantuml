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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;

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

	public List<Point2D.Double> extractList(final String searched) {
		final int p2 = this.indexOf(searched, 0);
		if (p2 == -1) {
			return Collections.emptyList();
		}
		final int p3 = this.indexOf("\"", p2 + searched.length());
		if (p3 == -1) {
			return Collections.emptyList();
		}
		return this.substring(p2 + searched.length(), p3).getPoints(" MC");
	}

	public int getIndexFromColor(int color) {
		String s = "stroke=\"" + StringUtils.goLowerCase(DotStringFactory.sharp000000(color)) + "\"";
		int idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		s = ";stroke:" + StringUtils.goLowerCase(DotStringFactory.sharp000000(color)) + ";";
		idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		s = "fill=\"" + StringUtils.goLowerCase(DotStringFactory.sharp000000(color)) + "\"";
		idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		// Log.info("Cannot find color=" + color + " " + StringUtils.goLowerCase(StringUtils.getAsHtml(color)));
		return -1;

	}

	public List<Point2D.Double> getPoints(String separator) {
		try {
			final StringTokenizer st = new StringTokenizer(svg, separator);
			final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
			while (st.hasMoreTokens()) {
				result.add(getFirstPoint(st.nextToken()));
			}
			return result;
		} catch (NumberFormatException e) {
			return Collections.emptyList();
		}
	}

	public Point2D.Double getNextPoint() {
		return getFirstPoint(svg);
	}

	private Point2D.Double getFirstPoint(final String tmp) {
		final StringTokenizer st = new StringTokenizer(tmp, ",");
		final double startX = Double.parseDouble(st.nextToken());
		final double startY = Double.parseDouble(st.nextToken());
		return function.apply(new Point2D.Double(startX, startY));
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
}
