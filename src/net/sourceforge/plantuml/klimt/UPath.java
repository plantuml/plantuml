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
 * Contribution:  Miguel Esteves
 * 
 *
 */
package net.sourceforge.plantuml.klimt;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.klimt.compress.CompressionMode;
import net.sourceforge.plantuml.klimt.compress.UShapeIgnorableForCompression;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.USegment;
import net.sourceforge.plantuml.klimt.geom.USegmentType;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;

public class UPath extends AbstractShadowable implements Iterable<USegment>, UShapeIgnorableForCompression {

	private final String comment;
	private final String codeLine;
	private final List<USegment> segments = new ArrayList<>();
	private MinMax minmax = MinMax.getEmpty(false);

	private boolean isOpenIconic;
	private boolean ignoreForCompressionOnX;
	private boolean ignoreForCompressionOnY;

	public UPath(String comment, String codeLine) {
		this.comment = comment;
		this.codeLine = codeLine;
	}

	public static UPath none() {
		return new UPath(null, null);
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
		final UPath result = new UPath(comment, codeLine);
		for (USegment seg : segments)
			result.addInternal(seg.translate(dx, dy));

		return result;
	}

	// ::comment when __HAXE__
	public UPath rotate(double theta) {
		final UPath result = new UPath(comment, codeLine);
		for (USegment seg : segments)
			result.addInternal(seg.rotate(theta));

		return result;
	}

	public UPath affine(AffineTransform transform, double angle, double scale) {
		final UPath result = new UPath(comment, codeLine);
		for (USegment seg : segments)
			result.addInternal(seg.affine(transform, angle, scale));

		return result;
	}
	// ::done

	public void moveTo(XPoint2D pt) {
		moveTo(pt.getX(), pt.getY());
	}

	public void lineTo(XPoint2D pt) {
		lineTo(pt.getX(), pt.getY());
	}

	public void moveTo(double x, double y) {
		add(new double[] { x, y }, USegmentType.SEG_MOVETO);
	}

	public void lineTo(double x, double y) {
		add(new double[] { x, y }, USegmentType.SEG_LINETO);
	}

	public void cubicTo(XPoint2D p1, XPoint2D p2, XPoint2D p) {
		cubicTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p.getX(), p.getY());
	}

	public void cubicTo(double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
		add(new double[] { ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2 }, USegmentType.SEG_CUBICTO);
	}

	public void quadTo(double ctrlx, double ctrly, double x2, double y2) {
		add(new double[] { ctrlx, ctrly, ctrlx, ctrly, x2, y2 }, USegmentType.SEG_CUBICTO);
	}

	public void quadTo(XPoint2D ctrl, XPoint2D pt) {
		quadTo(ctrl.getX(), ctrl.getY(), pt.getX(), pt.getY());
	}

	public void arcTo(double rx, double ry, double x_axis_rotation, double large_arc_flag, double sweep_flag, double x,
			double y) {
		add(new double[] { rx, ry, x_axis_rotation, large_arc_flag, sweep_flag, x, y }, USegmentType.SEG_ARCTO);
		// lineTo(x, y);
	}

	public void arcTo(XPoint2D pt, double radius, double large_arc_flag, double sweep_flag) {
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

	// ::comment when __HAXE__
	@Override
	public String toString() {
		return segments.toString();
	}
	// ::done

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

	public final String getCodeLine() {
		return codeLine;
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
