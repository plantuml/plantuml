package net.sourceforge.plantuml.klimt.geom;

// ::comment when __HAXE__

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.activitydiagram3.ftile.RectangleCoordinates;
// ::done

public class XPoint2D {

	final public double x;
	final public double y;

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public XPoint2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// ::comment when __HAXE__
	@Override
	public boolean equals(Object obj) {
		final XPoint2D other = (XPoint2D) obj;
		return this.x == other.x && this.y == other.y;
	}

	@Override
	public int hashCode() {
		return Double.valueOf(x).hashCode() + Double.valueOf(y).hashCode();
	}

	public XPoint2D transform(AffineTransform rotate) {
		final Point2D.Double tmp = new Point2D.Double(x, y);
		rotate.transform(tmp, tmp);
		return new XPoint2D(tmp.x, tmp.y);
	}
	// ::done

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

	public static double distance(RectangleCoordinates rectangleCoordinates) {
		rectangleCoordinates.setX1(rectangleCoordinates.getX1() - rectangleCoordinates.getX2());
		rectangleCoordinates.setY1(rectangleCoordinates.getY1() - rectangleCoordinates.getY2());
		return Math.sqrt(rectangleCoordinates.getX1() * rectangleCoordinates.getX1() + rectangleCoordinates.getY1() * rectangleCoordinates.getY1());
	}

	public double distance(double px, double py) {
		px -= getX();
		py -= getY();
		return Math.sqrt(px * px + py * py);
	}

	public XPoint2D move(double dx, double dy) {
		return new XPoint2D(x + dx, y + dy);
	}

	public XPoint2D move(XPoint2D delta) {
		return new XPoint2D(x + delta.x, y + delta.y);
	}

}
