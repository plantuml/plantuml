package net.sourceforge.plantuml.klimt.geom;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.ULine;

public class XLine2D implements UDrawable {

	final public double x1;
	final public double y1;
	final public double x2;
	final public double y2;

	public XLine2D(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public static XLine2D line(XPoint2D p1, XPoint2D p2) {
		return new XLine2D(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public XPoint2D getMiddle() {
		final double mx = (this.x1 + this.x2) / 2;
		final double my = (this.y1 + this.y2) / 2;
		return new XPoint2D(mx, my);
	}

	public final double getX1() {
		return x1;
	}

	public final double getY1() {
		return y1;
	}

	public final double getX2() {
		return x2;
	}

	public final double getY2() {
		return y2;
	}

	public XPoint2D getP1() {
		return new XPoint2D(x1, y1);
	}

	public XPoint2D getP2() {
		return new XPoint2D(x2, y2);
	}

	public XLine2D withPoint1(XPoint2D other) {
		return new XLine2D(other.x, other.y, x2, y2);
	}

	public XLine2D withPoint2(XPoint2D other) {
		return new XLine2D(x1, y1, other.x, other.y);
	}

	/**
	 * Returns the square of the distance from a point to a line segment. The
	 * distance measured is the distance between the specified point and the closest
	 * point between the specified end points. If the specified point intersects the
	 * line segment in between the end points, this method returns 0.0.
	 *
	 * @param x1 the X coordinate of the start point of the specified line segment
	 * @param y1 the Y coordinate of the start point of the specified line segment
	 * @param x2 the X coordinate of the end point of the specified line segment
	 * @param y2 the Y coordinate of the end point of the specified line segment
	 * @param px the X coordinate of the specified point being measured against the
	 *           specified line segment
	 * @param py the Y coordinate of the specified point being measured against the
	 *           specified line segment
	 * @return a double value that is the square of the distance from the specified
	 *         point to the specified line segment.
	 *
	 * @since 1.2
	 */
	public static double ptSegDistSq(double x1, double y1, double x2, double y2, double px, double py) {
		// Adjust vectors relative to x1,y1
		// x2,y2 becomes relative vector from x1,y1 to end of segment
		x2 -= x1;
		y2 -= y1;
		// px,py becomes relative vector from x1,y1 to test point
		px -= x1;
		py -= y1;
		double dotprod = px * x2 + py * y2;
		double projlenSq;
		if (dotprod <= 0.0) {
			// px,py is on the side of x1,y1 away from x2,y2
			// distance to segment is length of px,py vector
			// "length of its (clipped) projection" is now 0.0
			projlenSq = 0.0;
		} else {
			// switch to backwards vectors relative to x2,y2
			// x2,y2 are already the negative of x1,y1=>x2,y2
			// to get px,py to be the negative of px,py=>x2,y2
			// the dot product of two negated vectors is the same
			// as the dot product of the two normal vectors
			px = x2 - px;
			py = y2 - py;
			dotprod = px * x2 + py * y2;
			if (dotprod <= 0.0) {
				// px,py is on the side of x2,y2 away from x1,y1
				// distance to segment is length of (backwards) px,py vector
				// "length of its (clipped) projection" is now 0.0
				projlenSq = 0.0;
			} else {
				// px,py is between x1,y1 and x2,y2
				// dotprod is the length of the px,py vector
				// projected on the x2,y2=>x1,y1 vector times the
				// length of the x2,y2=>x1,y1 vector
				projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
			}
		}
		// Distance to line is now the length of the relative point
		// vector minus the length of its projection onto the line
		// (which is zero if the projection falls outside the range
		// of the line segment).
		double lenSq = px * px + py * py - projlenSq;
		if (lenSq < 0) {
			lenSq = 0;
		}
		return lenSq;
	}

	public XPoint2D intersect(XLine2D line2) {

		final double s1x = this.x2 - this.x1;
		final double s1y = this.y2 - this.y1;

		final double s2x = line2.x2 - line2.x1;
		final double s2y = line2.y2 - line2.y1;

		final double s = (-s1y * (this.x1 - line2.x1) + s1x * (this.y1 - line2.y1)) / (-s2x * s1y + s1x * s2y);
		final double t = (s2x * (this.y1 - line2.y1) - s2y * (this.x1 - line2.x1)) / (-s2x * s1y + s1x * s2y);

		if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
			return new XPoint2D(this.x1 + (t * s1x), this.y1 + (t * s1y));

		return null;
	}

	// ::comment when __HAXE__
	public void drawU(UGraphic ug) {
		ug = ug.apply(new UTranslate(x1, y1));
		final ULine line = new ULine(x2 - x1, y2 - y1);
		ug.draw(line);
	}
	// ::done

	public double getAngle() {
		return Math.atan2(y2 - y1, x2 - x1);
	}
}
