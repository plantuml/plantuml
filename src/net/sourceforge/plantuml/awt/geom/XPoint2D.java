package net.sourceforge.plantuml.awt.geom;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class XPoint2D {

	final public double x;
	final public double y;

	public XPoint2D() {
		this(0, 0);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public XPoint2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		final XPoint2D other = (XPoint2D) obj;
		return this.x == other.x && this.y == other.y;
	}

	@Override
	public int hashCode() {
		return new Double(x).hashCode() + new Double(y).hashCode();
	}

	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public double distance(XPoint2D other) {
		final double px = other.getX() - this.getX();
		final double py = other.getY() - this.getY();
		return Math.sqrt(px * px + py * py);
	}

	public double distanceSq(XPoint2D other) {
		final double px = other.getX() - this.getX();
		final double py = other.getY() - this.getY();
		return px * px + py * py;
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		x1 -= x2;
		y1 -= y2;
		return Math.sqrt(x1 * x1 + y1 * y1);
	}

	public double distance(double px, double py) {
		px -= getX();
		py -= getY();
		return Math.sqrt(px * px + py * py);
	}

	public XPoint2D move(double dx, double dy) {
		return new XPoint2D(x + dx, y + dy);
	}

	public XPoint2D transform(AffineTransform rotate) {
		final Point2D.Double tmp = new Point2D.Double(x, y);
		rotate.transform(tmp, tmp);
		return new XPoint2D(tmp.x, tmp.y);
	}

	public XPoint2D move(XPoint2D delta) {
		return new XPoint2D(delta.x, delta.y);
	}

}
