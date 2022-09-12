package net.sourceforge.plantuml.awt.geom;

import net.sourceforge.plantuml.awt.XShape;

public class XLine2D implements XShape {

	public double x1;
	public double y1;
	public double x2;
	public double y2;

	public XLine2D() {
		this(0, 0, 0, 0);
	}

	public XLine2D(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

	}

	public XLine2D(XPoint2D p1, XPoint2D p2) {
		this(p1.getX(), p1.getY(), p2.getX(), p2.getY());
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

}
