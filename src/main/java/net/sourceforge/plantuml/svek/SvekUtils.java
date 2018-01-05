/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringUtils;

public class SvekUtils {

	static public void traceString(final File f, String text) throws IOException {
		PrintWriter pw = null;
		try {
			Log.info("Creating intermediate file " + f.getAbsolutePath());
			pw = new PrintWriter(new FileWriter(f));
			pw.print(text);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	static class PointListIterator implements Iterator<List<Point2D.Double>> {

		private final String text;
		private final double yDelta;
		private int pos = 0;

		public static PointListIterator create(String text, double yDelta, int lineColor) {
			final PointListIterator result = new PointListIterator(text, yDelta);
			final int idx = getIndexFromColor(text, lineColor);
			if (idx == -1) {
				result.pos = -1;
			}
			return result;
		}

		public PointListIterator cloneMe() {
			final PointListIterator result = new PointListIterator(text, yDelta);
			result.pos = this.pos;
			return result;
		}

		private PointListIterator(String text, double yDelta) {
			this.text = text;
			this.yDelta = yDelta;
		}

		public boolean hasNext() {
			return true;
		}

		public List<Point2D.Double> next() {
			if (pos == -1) {
				return Collections.emptyList();
			}
			try {
				final List<Point2D.Double> result = extractPointsList(text, pos, yDelta);
				pos = text.indexOf(pointsString, pos) + pointsString.length() + 1;
				return result;
			} catch (StringIndexOutOfBoundsException e) {
				Log.error("Error " + e);
				return Collections.emptyList();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	final private static String pointsString = "points=\"";

	public static List<Point2D.Double> extractPointsList(final String svg, final int starting, double yDelta) {
		final int p2 = svg.indexOf(pointsString, starting);
		final int p3 = svg.indexOf("\"", p2 + pointsString.length());
		final String points = svg.substring(p2 + pointsString.length(), p3);
		final List<Point2D.Double> pointsList = getPoints(points, yDelta);
		return pointsList;
	}

	public static List<Point2D.Double> extractD(final String svg, final int starting, double yDelta) {
		final int p2 = svg.indexOf("d=\"", starting);
		final int p3 = svg.indexOf("\"", p2 + "d=\"".length());
		final String points = svg.substring(p2 + "d=\"".length(), p3);
		final List<Point2D.Double> pointsList = getPoints(points, yDelta);
		return pointsList;
	}

	public static int getIndexFromColor(String svg, int color) {
		String s = "stroke=\"" + StringUtils.goLowerCase(StringUtils.getAsHtml(color)) + "\"";
		int idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		s = ";stroke:" + StringUtils.goLowerCase(StringUtils.getAsHtml(color)) + ";";
		idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		s = "fill=\"" + StringUtils.goLowerCase(StringUtils.getAsHtml(color)) + "\"";
		idx = svg.indexOf(s);
		if (idx != -1) {
			return idx;
		}
		// Log.info("Cannot find color=" + color + " " + StringUtils.goLowerCase(StringUtils.getAsHtml(color)));
		return -1;

	}

	static public double getValue(String svg, int starting, String varName) {
		final String varNameString = varName + "=\"";
		int p1 = svg.indexOf(varNameString, starting);
		if (p1 == -1) {
			throw new IllegalStateException();
		}
		p1 += varNameString.length();
		final int p2 = svg.indexOf('\"', p1);
		return Double.parseDouble(svg.substring(p1, p2));

	}

	public static double getMaxX(List<Point2D.Double> points) {
		double result = points.get(0).x;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x > result) {
				result = points.get(i).x;
			}
		}
		return result;
	}

	public static double getMinX(List<Point2D.Double> points) {
		double result = points.get(0).x;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x < result) {
				result = points.get(i).x;
			}
		}
		return result;
	}

	public static Point2D.Double getMinXY(List<Point2D.Double> points) {
		return new Point2D.Double(getMinX(points), getMinY(points));
	}

	public static double getMaxY(List<Point2D.Double> points) {
		double result = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).y > result) {
				result = points.get(i).y;
			}
		}
		return result;
	}

	public static double getMinY(List<Point2D.Double> points) {
		double result = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).y < result) {
				result = points.get(i).y;
			}
		}
		return result;
	}

	static private List<Point2D.Double> getPoints(String points, double yDelta) {
		try {
			final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
			final StringTokenizer st = new StringTokenizer(points, " MC");
			while (st.hasMoreTokens()) {
				final String t = st.nextToken();
				final StringTokenizer st2 = new StringTokenizer(t, ",");
				final double x = Double.parseDouble(st2.nextToken());
				final double y = Double.parseDouble(st2.nextToken()) + yDelta;
				result.add(new Point2D.Double(x, y));
			}
			return result;
		} catch (NumberFormatException e) {
			return Collections.emptyList();
		}
	}

	public static void println(StringBuilder sb) {
		sb.append('\n');
	}

	public static String pixelToInches(double pixel) {
		final double v = pixel / 72.0;
		return String.format(Locale.US, "%6.6f", v);
	}

}
