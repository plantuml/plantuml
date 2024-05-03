package net.sourceforge.plantuml.klimt.geom;

import net.sourceforge.plantuml.activitydiagram3.ftile.RectangleCoordinates;

public class XCubicCurve2D {

	public double ctrlx1;
	public double ctrly1;
	public double ctrlx2;
	public double ctrly2;
	public RectangleCoordinates rectangleCoordinates = new RectangleCoordinates(0.0, 0.0, 0.0, 0.0);

	public static XCubicCurve2D none() {
		return new XCubicCurve2D(0, 0, 0, 0, 0, 0, 0, 0);
	}

	public XCubicCurve2D(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2,
					double y2) {
		this.rectangleCoordinates.setX1(x1);
		this.rectangleCoordinates.setY1(y1);
		this.ctrlx1 = ctrlx1;
		this.ctrly1 = ctrly1;
		this.ctrlx2 = ctrlx2;
		this.ctrly2 = ctrly2;
		this.rectangleCoordinates.setX2(x2);
		this.rectangleCoordinates.setY2(y2);

	}

	public XPoint2D getP1() {
		return new XPoint2D(rectangleCoordinates.getX1(), rectangleCoordinates.getY1());
	}

	public XPoint2D getP2() {
		return new XPoint2D(rectangleCoordinates.getX2(), rectangleCoordinates.getY2());
	}

	public void setCurve(double ctrlx1, double ctrly1, double ctrlx2, double ctrly2,
					RectangleCoordinates rectangleCoordinates) {
		this.rectangleCoordinates.setX1(rectangleCoordinates.getX1());
		this.rectangleCoordinates.setY1(rectangleCoordinates.getY1());
		this.ctrlx1 = ctrlx1;
		this.ctrly1 = ctrly1;
		this.ctrlx2 = ctrlx2;
		this.ctrly2 = ctrly2;
		this.rectangleCoordinates.setX2(rectangleCoordinates.getX2());
		this.rectangleCoordinates.setY2(rectangleCoordinates.getY2());

	}

	public double getLength() {
		final double dx = this.rectangleCoordinates.getX2() - this.rectangleCoordinates.getX1();
		final double dy = this.rectangleCoordinates.getY2() - this.rectangleCoordinates.getY1();
		return Math.sqrt(dx * dx + dy * dy);
	}

	public void setCurve(XCubicCurve2D other) {
		setCurve(other.ctrlx1, other.ctrly1, other.ctrlx2, other.ctrly2, new RectangleCoordinates(
						other.rectangleCoordinates.getX1(),
						other.rectangleCoordinates.getY1(),
						other.ctrlx2,
						other.ctrly2)
		);

	}

	public void subdivide(XCubicCurve2D left, XCubicCurve2D right) {
		double x1 = this.getX1();
		double y1 = this.getY1();
		double ctrlx1 = this.getCtrlX1();
		double ctrly1 = this.getCtrlY1();
		double ctrlx2 = this.getCtrlX2();
		double ctrly2 = this.getCtrlY2();
		double x2 = this.getX2();
		double y2 = this.getY2();
		double centerx = (ctrlx1 + ctrlx2) / 2.0;
		double centery = (ctrly1 + ctrly2) / 2.0;
		ctrlx1 = (x1 + ctrlx1) / 2.0;
		ctrly1 = (y1 + ctrly1) / 2.0;
		ctrlx2 = (x2 + ctrlx2) / 2.0;
		ctrly2 = (y2 + ctrly2) / 2.0;
		double ctrlx12 = (ctrlx1 + centerx) / 2.0;
		double ctrly12 = (ctrly1 + centery) / 2.0;
		double ctrlx21 = (ctrlx2 + centerx) / 2.0;
		double ctrly21 = (ctrly2 + centery) / 2.0;
		centerx = (ctrlx12 + ctrlx21) / 2.0;
		centery = (ctrly12 + ctrly21) / 2.0;
		if (left != null)
			left.setCurve(ctrlx1, ctrly1, ctrlx12, ctrly12, new RectangleCoordinates(x1, y1, centerx, centery));

		if (right != null)
			right.setCurve(ctrlx21, ctrly21, ctrlx2, ctrly2, new RectangleCoordinates(centerx, centery, x2, y2));

	}

	public final double getX1() {
		return rectangleCoordinates.getX1();
	}

	public final double getY1() {
		return rectangleCoordinates.getY1();
	}

	public final double getCtrlX1() {
		return ctrlx1;
	}

	public final double getCtrlY1() {
		return ctrly1;
	}

	public final double getCtrlX2() {
		return ctrlx2;
	}

	public final double getCtrlY2() {
		return ctrly2;
	}

	public final double getX2() {
		return rectangleCoordinates.getX2();
	}

	public final double getY2() {
		return rectangleCoordinates.getY2();
	}

	public XPoint2D getCtrlP1() {
		return new XPoint2D(ctrlx1, ctrly1);
	}

	public XPoint2D getCtrlP2() {
		return new XPoint2D(ctrlx2, ctrly2);
	}

	public double getFlatnessSq() {
		return Math.max(XLine2D.ptSegDistSq(ctrlx1, ctrly1, new RectangleCoordinates(
										rectangleCoordinates.getX1(),
										rectangleCoordinates.getY1(),
										rectangleCoordinates.getX2(),
										rectangleCoordinates.getY2())),
						XLine2D.ptSegDistSq(ctrlx2, ctrly2, new RectangleCoordinates(
										rectangleCoordinates.getX1(),
										rectangleCoordinates.getY1(),
										rectangleCoordinates.getX2(),
										rectangleCoordinates.getY2())));
	}

	public double getFlatness() {
		return getFlatness(getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), new RectangleCoordinates(
						getX1(),
						getY1(),
						getX2(),
						getY2()));
	}

	private static double getFlatness(double ctrlx1, double ctrly1, double ctrlx2, double ctrly2,
					RectangleCoordinates rectangleCoordinates) {
		return Math.sqrt(getFlatnessSq(ctrlx1, ctrly1, ctrlx2, ctrly2, new RectangleCoordinates(
						rectangleCoordinates.getX1(),
						rectangleCoordinates.getY1(),
						rectangleCoordinates.getX2(),
						rectangleCoordinates.getY2())));
	}

	private static double getFlatnessSq(double ctrlx1, double ctrly1, double ctrlx2,
					double ctrly2, RectangleCoordinates rectangleCoordinates) {
		return Math.max(XLine2D.ptSegDistSq(ctrlx1, ctrly1, new RectangleCoordinates(
										rectangleCoordinates.getX1(),
										rectangleCoordinates.getY1(),
										rectangleCoordinates.getX2(),
										rectangleCoordinates.getY2())),
						XLine2D.ptSegDistSq(ctrlx2, ctrly2, new RectangleCoordinates(
										rectangleCoordinates.getX1(),
										rectangleCoordinates.getY1(),
										rectangleCoordinates.getX2(),
										rectangleCoordinates.getY2())));

	}

}
