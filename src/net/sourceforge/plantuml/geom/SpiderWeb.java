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
package net.sourceforge.plantuml.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.Log;

public class SpiderWeb {

	private final int pointsInCircle = 16;
	private int nbRow;
	private int nbCol;

	final private int widthCell;
	final private int heightCell;

	final private int xMargin = 50;
	final private int yMargin = 50;

	private final List<PolylineBreakeable> lines = new ArrayList<PolylineBreakeable>();

	public SpiderWeb(int widthCell, int heightCell) {
		Log.info("widthCell=" + widthCell + " heightCell=" + heightCell);
		this.widthCell = widthCell;
		this.heightCell = heightCell;
	}

	public Point2DInt getMainPoint(int row, int col) {
		return new Point2DInt(col * (widthCell + xMargin), row * (heightCell + yMargin));
	}

	public Collection<Point2DInt> getHangPoints(int row, int col) {
		// final double dist = Math.pow(1.6, -row - 10) + Math.pow(1.5, -col -
		// 10);
		assert pointsInCircle % 4 == 0;
		final List<Point2DInt> result = new ArrayList<Point2DInt>();
		final int dist = (int) Math.round(Math.sqrt(widthCell * widthCell + heightCell * heightCell) / 10);
		for (int i = 0; i < pointsInCircle; i++) {
			final Point2DInt main = getMainPoint(row, col);
			final int x = main.getXint();
			final int y = main.getYint();
			if (i == 0) {
				result.add(new Point2DInt(x + dist, y));
			} else if (i == pointsInCircle / 4) {
				result.add(new Point2DInt(x, y + dist));
			} else if (i == 2 * pointsInCircle / 4) {
				result.add(new Point2DInt(x - dist, y));
			} else if (i == 3 * pointsInCircle / 4) {
				result.add(new Point2DInt(x, y - dist));
			} else {
				final double angle = Math.PI * 2.0 * i / pointsInCircle;
				final double x1 = x + dist * Math.cos(angle);
				final double y1 = y + dist * Math.sin(angle);
				result.add(new Point2DInt((int) Math.round(x1), (int) Math.round(y1)));
			}
		}
		// Log.println("getHangPoints="+result);
		return result;
	}

	public PolylineBreakeable addPolyline(int row1, int col1, int row2, int col2) {
		// Log.println("SpiderWeb : adding " + row1 + "," + col1 + " - "
		// + row2 + "," + col2);
		final PolylineBreakeable result = computePolyline(row1, col1, row2, col2);
		// Log.println("SpiderWeb : adding " + result);
		if (result != null) {
			lines.add(result);
		}
		return result;
	}

	private PolylineBreakeable computePolyline(int row1, int col1, int row2, int col2) {
		if (row1 > nbRow) {
			nbRow = row1;
		}
		if (row2 > nbRow) {
			nbRow = row2;
		}
		if (col1 > nbCol) {
			nbCol = col1;
		}
		if (col2 > nbCol) {
			nbCol = col2;
		}
		if (directLinkPossibleForGeometry(row1, col1, row2, col2)) {
			// Log.println("Geom OK");
			final PolylineBreakeable direct = new PolylineBreakeable(getMainPoint(row1, col1), getMainPoint(row2, col2));
			if (isCompatible(direct)) {
				// Log.println("Direct OK");
				return direct;
			}
		}
		return bestLevel1Line(row1, col1, row2, col2);
	}

	private boolean isCompatible(PolylineBreakeable toTest) {
		for (PolylineBreakeable p : lines) {
			if (p.doesTouch(toTest)) {
				return false;
			}
		}
		return true;
	}

	private PolylineBreakeable bestLevel1Line(int row1, int col1, int row2, int col2) {
		PolylineBreakeable result = null;
		for (int u = 5; u <= 95; u += 5) {
			for (int d = -200; d <= 200; d += 5) {
				final PolylineBreakeable cur = new PolylineBreakeable(getMainPoint(row1, col1),
						getMainPoint(row2, col2));
				cur.insertBetweenPoint(u, d);
				if ((result == null || cur.getLength() < result.getLength()) && isCompatible(cur)) {
					result = cur;
				}

			}
		}
		return result;
	}

	boolean directLinkPossibleForGeometry(int row1, int col1, int row2, int col2) {
		final int rowMin = Math.min(row1, row2);
		final int rowMax = Math.max(row1, row2);
		final int colMin = Math.min(col1, col2);
		final int colMax = Math.max(col1, col2);
		final LineSegmentInt seg = new LineSegmentInt(col1, row1, col2, row2);
		for (int r = rowMin; r <= rowMax; r++) {
			for (int c = colMin; c <= colMax; c++) {
				if (r == row1 && c == col1) {
					continue;
				}
				if (r == row2 && c == col2) {
					continue;
				}
				if (seg.containsPoint(new Point2DInt(c, r))) {
					return false;
				}
			}
		}
		return true;
	}

	final int getPointsInCircle() {
		return pointsInCircle;
	}

}