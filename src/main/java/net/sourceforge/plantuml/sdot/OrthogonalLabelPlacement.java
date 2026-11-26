/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.sdot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;

/**
 * Places edge labels in diagram coordinates. The caller registers the painted
 * connector widths and decorations before placing labels. Each placed label
 * reserves its bounds for subsequent labels.
 */
final class OrthogonalLabelPlacement {
	private static final double LABEL_GAP = 2;
	private static final double[] LABEL_OFFSETS = { 4, 8, 12, 18, 26, 38 };
	private final List<RectangleArea> obstacles = new ArrayList<>();
	private final Map<DotPath, PathStroke> strokes = new IdentityHashMap<>();

	OrthogonalLabelPlacement(List<RectangleArea> nodes) {
		for (RectangleArea node : nodes)
			obstacles.add(OrthogonalPath.expand(node, LABEL_GAP));
	}

	void addPath(DotPath path, double strokeWidth) {
		final PathStroke stroke = new PathStroke(strokeWidth / 2);
		for (XCubicCurve2D curve : path.getBeziers())
			addSegment(curve, 0, LABEL_GAP + stroke.radius, stroke.bounds);
		strokes.put(path, stroke);
		obstacles.addAll(stroke.bounds);
	}

	void addDecoration(RectangleArea bounds) {
		reserve(bounds);
	}

	void addClusterBorder(RectangleArea area) {
		for (RectangleArea side : OrthogonalPath.sides(area))
			reserve(side);
	}

	private void addSegment(XCubicCurve2D curve, int depth, double clearance, List<RectangleArea> stroke) {
		// Subdivision gives curved fallback edges tighter bounds and leaves
		// more room for labels next to the stroke.
		if (depth < 3 && isAxisAligned(curve) == false) {
			final XCubicCurve2D left = XCubicCurve2D.none();
			final XCubicCurve2D right = XCubicCurve2D.none();
			curve.subdivide(left, right);
			addSegment(left, depth + 1, clearance, stroke);
			addSegment(right, depth + 1, clearance, stroke);
			return;
		}
		stroke.add(strokeBounds(curve, clearance));
	}

	private static boolean isAxisAligned(XCubicCurve2D curve) {
		return (curve.x1 == curve.x2 && curve.x1 == curve.ctrlx1 && curve.x1 == curve.ctrlx2)
				|| (curve.y1 == curve.y2 && curve.y1 == curve.ctrly1 && curve.y1 == curve.ctrly2);
	}

	private static RectangleArea strokeBounds(XCubicCurve2D curve, double padding) {
		final RectangleArea bounds = RectangleArea.build(curve.getP1(), curve.getP2())
				.merge(new XPoint2D(curve.ctrlx1, curve.ctrly1)).merge(new XPoint2D(curve.ctrlx2, curve.ctrly2));
		return OrthogonalPath.expand(bounds, padding);
	}

	private UTranslate reserve(RectangleArea area) {
		obstacles.add(OrthogonalPath.expand(area, LABEL_GAP));
		return area.getPosition();
	}

	UTranslate placeMidpoint(DotPath path, XDimension2D dimension) {
		final List<XCubicCurve2D> segments = path.getBeziers();
		final double extraClearance = Math.max(0, LABEL_GAP + strokes.get(path).radius - LABEL_OFFSETS[0]);
		double length = 0;
		for (XCubicCurve2D segment : segments)
			length += segment.getP1().distance(segment.getP2());
		final double preferred = length / 2;
		XPoint2D midpoint = path.getStartPoint();
		RectangleArea best = null;
		double bestCost = Double.POSITIVE_INFINITY;
		double offset = 0;
		for (int i = 0; i < segments.size(); i++) {
			final XCubicCurve2D segment = segments.get(i);
			final boolean horizontal = segment.y1 == segment.y2;
			final double start = horizontal ? segment.x1 : segment.y1;
			final double end = horizontal ? segment.x2 : segment.y2;
			final double axis = horizontal ? segment.y1 : segment.x1;
			final double along = (horizontal ? dimension.getWidth() : dimension.getHeight()) / 2;
			final double across = (horizontal ? dimension.getHeight() : dimension.getWidth()) / 2;
			final double direction = Math.copySign(1, end - start);
			final double target = start + direction * (preferred - offset);
			final double runLength = Math.abs(end - start);
			if (offset <= preferred && preferred <= offset + runLength)
				midpoint = horizontal ? new XPoint2D(target, axis) : new XPoint2D(axis, target);

			// Keep labels from extending past the attachment points into an endpoint.
			final double startInset = i == 0 ? along + LABEL_GAP : 0;
			final double endInset = i == segments.size() - 1 ? along + LABEL_GAP : 0;
			offset += runLength;
			if (runLength < startInset + endInset)
				continue;
			final double first = start + direction * startInset;
			final double last = end - direction * endInset;
			// Compare positions on both sides of the run. Distance from the
			// stroke counts more than sliding along it, keeping labels close.
			for (double offsetFromStroke : LABEL_OFFSETS) {
				final double gap = offsetFromStroke + extraClearance;
				if (gap * 2 >= bestCost)
					break;
				for (int side = -1; side <= 1; side += 2) {
					final double center = axis + side * (gap + across);
					final double position = findCenter(horizontal, center - across, center + across, along,
							Math.min(first, last), Math.max(first, last), target);
					if (Double.isNaN(position))
						continue;
					final double cost = Math.abs(position - target) + 2 * gap;
					if (cost < bestCost) {
						bestCost = cost;
						best = horizontal
								? new RectangleArea(position - along, center - across, position + along, center + across)
								: new RectangleArea(center - across, position - along, center + across, position + along);
					}
				}
			}
		}
		return best == null ? placeNear(midpoint, dimension, null) : reserve(best);
	}

	// Keeps the position assigned by the layout, and other labels off it.
	UTranslate keepLabel(UTranslate original, XDimension2D dimension) {
		return reserve(new RectangleArea(original.getDx(), original.getDy(), original.getDx() + dimension.getWidth(),
				original.getDy() + dimension.getHeight()));
	}

	UTranslate placeSplineLabel(DotPath path, UTranslate original, XDimension2D dimension) {
		final RectangleArea area = new RectangleArea(original.getDx(), original.getDy(),
				original.getDx() + dimension.getWidth(), original.getDy() + dimension.getHeight());
		final PathStroke stroke = strokes.get(path);
		// Refine the bounds of its own curve before moving a label away
		// from the position assigned by the layout.
		if (isClear(area, stroke.bounds) && path.getBeziers().stream()
				.noneMatch(curve -> intersectsStroke(area, curve, stroke.radius, 8)))
			return reserve(area);
		return placeNear(area.getPointCenter(), dimension, null);
	}

	private boolean isClear(RectangleArea area, List<RectangleArea> ignored) {
		for (RectangleArea obstacle : obstacles)
			if (intersects(area, obstacle) && ignored.contains(obstacle) == false)
				return false;
		return true;
	}

	private static boolean intersectsStroke(RectangleArea area, XCubicCurve2D curve, double radius, int subdivisions) {
		if (intersects(area, strokeBounds(curve, radius)) == false)
			return false;
		if (subdivisions == 0 || isAxisAligned(curve))
			return true;

		final XCubicCurve2D left = XCubicCurve2D.none();
		final XCubicCurve2D right = XCubicCurve2D.none();
		curve.subdivide(left, right);
		return intersectsStroke(area, left, radius, subdivisions - 1)
				|| intersectsStroke(area, right, radius, subdivisions - 1);
	}

	private static boolean intersects(RectangleArea a, RectangleArea b) {
		return a.getMinX() < b.getMaxX() && a.getMaxX() > b.getMinX()
				&& a.getMinY() < b.getMaxY() && a.getMaxY() > b.getMinY();
	}

	/**
	 * Places an endpoint label with both row centers on the attachment's side
	 * of the perpendicular bisector. The row offset measures how far the row
	 * nearer to the opposite endpoint sits vertically from the block center.
	 */
	UTranslate placeEndpoint(XPoint2D endpoint, XPoint2D otherEndpoint, XDimension2D dimension, double rowOffset) {
		if (endpoint.equals(otherEndpoint))
			return placeNear(endpoint, dimension, null);

		final double dx = otherEndpoint.x - endpoint.x;
		final double dy = otherEndpoint.y - endpoint.y;
		final double halfSpan = (dx * dx + dy * dy) / 2;
		final double extent = Math.abs(dx) * (dimension.getWidth() / 2 + LABEL_GAP)
				+ Math.abs(dy) * (dimension.getHeight() / 2 + LABEL_GAP);
		// Leave room to place the block just past its own node. Keep
		// the center of each stacked row on its side of the bisector.
		final double centerLimit = halfSpan - Math.abs(dy) * rowOffset - LABEL_GAP * Math.hypot(dx, dy);
		final double limit = Math.min(Math.max(halfSpan - extent, extent), centerLimit);
		return placeNear(endpoint, dimension, new EndpointHalfPlane(dx, dy, limit));
	}

	private UTranslate placeNear(XPoint2D anchor, XDimension2D dimension, EndpointHalfPlane halfPlane) {
		final double halfWidth = dimension.getWidth() / 2;
		final double halfHeight = dimension.getHeight() / 2;
		// The axes through the anchor reach free space even when all nearby positions are blocked.
		XPoint2D best = closestOnAxis(anchor, dimension, halfPlane, true, anchor.y, null);
		best = closestOnAxis(anchor, dimension, halfPlane, false, anchor.x, best);
		// A closer label can touch an obstacle's edge off both axes, for example
		// just below a node and beside the connector leaving its bottom port.
		for (RectangleArea obstacle : obstacles) {
			best = closestOnAxis(anchor, dimension, halfPlane, true, obstacle.getMinY() - halfHeight, best);
			best = closestOnAxis(anchor, dimension, halfPlane, true, obstacle.getMaxY() + halfHeight, best);
			best = closestOnAxis(anchor, dimension, halfPlane, false, obstacle.getMinX() - halfWidth, best);
			best = closestOnAxis(anchor, dimension, halfPlane, false, obstacle.getMaxX() + halfWidth, best);
		}
		return reserve(new RectangleArea(best.x - halfWidth, best.y - halfHeight, best.x + halfWidth, best.y + halfHeight));
	}

	private XPoint2D closestOnAxis(XPoint2D anchor, XDimension2D dimension, EndpointHalfPlane halfPlane,
			boolean horizontal, double across, XPoint2D best) {
		final double along = horizontal ? anchor.x : anchor.y;
		final double crossOffset = across - (horizontal ? anchor.y : anchor.x);
		final double bestDistance = best == null ? Double.POSITIVE_INFINITY : anchor.distanceSq(best);
		if (crossOffset * crossOffset >= bestDistance)
			return best;

		final double halfWidth = dimension.getWidth() / 2;
		final double halfHeight = dimension.getHeight() / 2;
		double from = Double.NEGATIVE_INFINITY;
		double to = Double.POSITIVE_INFINITY;
		if (halfPlane != null) {
			final double available = halfPlane.limit - crossOffset * (horizontal ? halfPlane.dy : halfPlane.dx);
			final double delta = horizontal ? halfPlane.dx : halfPlane.dy;
			if (delta > 0)
				to = along + available / delta;
			else if (delta < 0)
				from = along + available / delta;
			else if (available < 0)
				return best;
		}
		final double halfAcross = horizontal ? halfHeight : halfWidth;
		final double halfAlong = horizontal ? halfWidth : halfHeight;
		final double position = findCenter(horizontal, across - halfAcross, across + halfAcross, halfAlong, from, to,
				along);
		final double alongOffset = position - along;
		if (alongOffset * alongOffset + crossOffset * crossOffset < bestDistance)
			return horizontal ? new XPoint2D(position, across) : new XPoint2D(across, position);
		return best;
	}

	private double findCenter(boolean horizontal, double low, double high, double halfWidth, double from, double to,
			double target) {
		final double preferred = Math.max(from, Math.min(to, target));
		final List<BlockedInterval> blockers = new ArrayList<>();
		boolean preferredBlocked = false;
		// Each obstacle overlapping this strip rules out an interval of label centers.
		for (RectangleArea obstacle : obstacles) {
			final double minAcross = horizontal ? obstacle.getMinY() : obstacle.getMinX();
			final double maxAcross = horizontal ? obstacle.getMaxY() : obstacle.getMaxX();
			final double start = (horizontal ? obstacle.getMinX() : obstacle.getMinY()) - halfWidth;
			final double end = (horizontal ? obstacle.getMaxX() : obstacle.getMaxY()) + halfWidth;
			if (minAcross < high && maxAcross > low && start < to && end > from) {
				blockers.add(new BlockedInterval(start, end));
				if (start < preferred && preferred < end)
					preferredBlocked = true;
			}
		}
		if (preferredBlocked == false)
			return preferred;

		// The nearest free center is at a boundary of the merged interval
		// containing the preferred center. Touching intervals leave a free point.
		blockers.sort(Comparator.comparingDouble(interval -> interval.start));
		double left = Double.NEGATIVE_INFINITY;
		double right = Double.NEGATIVE_INFINITY;
		for (BlockedInterval interval : blockers) {
			if (interval.start >= right) {
				if (preferred < right)
					break;
				left = interval.start;
			}
			right = Math.max(right, interval.end);
		}

		if (left < from)
			return right <= to ? right : Double.NaN;
		if (right > to)
			return left;
		return preferred - left <= right - preferred ? left : right;
	}

	private static final class BlockedInterval {
		private final double start;
		private final double end;

		BlockedInterval(double start, double end) {
			this.start = start;
			this.end = end;
		}
	}

	private static final class PathStroke {
		private final double radius;
		private final List<RectangleArea> bounds = new ArrayList<>();

		PathStroke(double radius) {
			this.radius = radius;
		}
	}

	// A point is admissible if its displacement from the attachment,
	// projected onto the endpoint-to-endpoint vector, is within the limit.
	private static final class EndpointHalfPlane {
		private final double dx;
		private final double dy;
		private final double limit;

		EndpointHalfPlane(double dx, double dy, double limit) {
			this.dx = dx;
			this.dy = dy;
			this.limit = limit;
		}
	}
}
