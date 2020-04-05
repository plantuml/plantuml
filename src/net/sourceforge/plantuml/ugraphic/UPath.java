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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.comp.CompressionMode;

public class UPath extends AbstractShadowable implements Iterable<USegment>, UShapeIgnorableForCompression {

	private final String comment;
	private final List<USegment> segments = new ArrayList<USegment>();
	private MinMax minmax = MinMax.getEmpty(false);

	private boolean isOpenIconic;
	private boolean ignoreForCompressionOnX;
	private boolean ignoreForCompressionOnY;

	public UPath(String comment) {
		this.comment = comment;
	}

	public UPath() {
		this(null);
	}

	public void add(double[] coord, USegmentType pathType) {
		addInternal(new USegment(coord, pathType));
	}

	public boolean isEmpty() {
		return segments.size() == 0;
	}

	private void addInternal(USegment segment) {
		segments.add(segment);
		final double coord[] = segment.getCoord();
		if (segment.getSegmentType() == USegmentType.SEG_ARCTO) {
			minmax = minmax.addPoint(coord[5], coord[6]);
			// minmax = minmax.addPoint(coord[5] + coord[0], coord[6] + coord[1]);
			// minmax = minmax.addPoint(coord[5] - coord[0], coord[6] - coord[1]);
		} else {
			for (int i = 0; i < coord.length; i += 2) {
				minmax = minmax.addPoint(coord[i], coord[i + 1]);
			}
		}
	}

	public UPath translate(double dx, double dy) {
		final UPath result = new UPath(comment);
		for (USegment seg : segments) {
			result.addInternal(seg.translate(dx, dy));
		}
		return result;
	}

	public UPath rotate(double theta) {
		final UPath result = new UPath(comment);
		for (USegment seg : segments) {
			result.addInternal(seg.rotate(theta));
		}
		return result;
	}

	public void moveTo(Point2D pt) {
		moveTo(pt.getX(), pt.getY());
	}

	public void lineTo(Point2D pt) {
		lineTo(pt.getX(), pt.getY());
	}

	public void moveTo(double x, double y) {
		add(new double[] { x, y }, USegmentType.SEG_MOVETO);
	}

	public void lineTo(double x, double y) {
		add(new double[] { x, y }, USegmentType.SEG_LINETO);
	}

	public void cubicTo(Point2D p1, Point2D p2, Point2D p) {
		cubicTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p.getX(), p.getY());
	}

	public void cubicTo(double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
		add(new double[] { ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2 }, USegmentType.SEG_CUBICTO);
	}

	public void quadTo(double ctrlx, double ctrly, double x2, double y2) {
		add(new double[] { ctrlx, ctrly, ctrlx, ctrly, x2, y2 }, USegmentType.SEG_CUBICTO);
	}

	public void quadTo(Point2D ctrl, Point2D pt) {
		quadTo(ctrl.getX(), ctrl.getY(), pt.getX(), pt.getY());
	}

	public void arcTo(double rx, double ry, double x_axis_rotation, double large_arc_flag, double sweep_flag, double x,
			double y) {
		add(new double[] { rx, ry, x_axis_rotation, large_arc_flag, sweep_flag, x, y }, USegmentType.SEG_ARCTO);
		// lineTo(x, y);
	}

	public void arcTo(Point2D pt, double radius, double large_arc_flag, double sweep_flag) {
		add(new double[] { radius, radius, 0, large_arc_flag, sweep_flag, pt.getX(), pt.getY() },
				USegmentType.SEG_ARCTO);
		// lineTo(x, y);
	}

	public void closePath() {
		// System.err.println("CLOSE_PATH");
	}

	public double getMaxX() {
		return minmax.getMaxX();
	}

	public double getMaxY() {
		return minmax.getMaxY();
	}

	public double getMinX() {
		return minmax.getMinX();
	}

	public double getMinY() {
		return minmax.getMinY();
	}

	@Override
	public String toString() {
		return segments.toString();
	}

	public Iterator<USegment> iterator() {
		return segments.iterator();
	}

	public boolean isOpenIconic() {
		return isOpenIconic;
	}

	public void setOpenIconic(boolean isOpenIconic) {
		this.isOpenIconic = isOpenIconic;
	}

	public final String getComment() {
		return comment;
	}

	public void setIgnoreForCompressionOnX() {
		this.ignoreForCompressionOnX = true;
	}

	public void setIgnoreForCompressionOnY() {
		this.ignoreForCompressionOnY = true;
	}

	public void drawWhenCompressed(UGraphic ug, CompressionMode mode) {
	}

	public boolean isIgnoreForCompressionOn(CompressionMode mode) {
		if (mode == CompressionMode.ON_X) {
			return ignoreForCompressionOnX;
		}
		if (mode == CompressionMode.ON_Y) {
			return ignoreForCompressionOnY;
		}
		throw new IllegalArgumentException();
	}

	// public boolean isEmpty() {
	// return segments.size() == 0;
	// }

}
