package net.sourceforge.plantuml.klimt.geom;

public class XCubicCurve2D {

	public double x1;
	public double y1;
	public double ctrlx1;
	public double ctrly1;
	public double ctrlx2;
	public double ctrly2;
	public double x2;
	public double y2;

	public static XCubicCurve2D none() {
		return new XCubicCurve2D(0, 0, 0, 0, 0, 0, 0, 0);
	}

	public XCubicCurve2D(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2,
			double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.ctrlx1 = ctrlx1;
		this.ctrly1 = ctrly1;
		this.ctrlx2 = ctrlx2;
		this.ctrly2 = ctrly2;
		this.x2 = x2;
		this.y2 = y2;

	}

	public XPoint2D getP1() {
		return new XPoint2D(x1, y1);
	}

	public XPoint2D getP2() {
		return new XPoint2D(x2, y2);
	}

	public void setCurve(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2,
			double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.ctrlx1 = ctrlx1;
		this.ctrly1 = ctrly1;
		this.ctrlx2 = ctrlx2;
		this.ctrly2 = ctrly2;
		this.x2 = x2;
		this.y2 = y2;

	}

	public double getLength() {
		final double dx = this.x2 - this.x1;
		final double dy = this.y2 - this.y1;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public void setCurve(XCubicCurve2D other) {
		setCurve(other.x1, other.y1, other.ctrlx1, other.ctrly1, other.ctrlx2, other.ctrly2, other.ctrlx2,
				other.ctrly2);

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
			left.setCurve(x1, y1, ctrlx1, ctrly1, ctrlx12, ctrly12, centerx, centery);

		if (right != null)
			right.setCurve(centerx, centery, ctrlx21, ctrly21, ctrlx2, ctrly2, x2, y2);

	}

	public final double getX1() {
		return x1;
	}

	public final double getY1() {
		return y1;
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
		return x2;
	}

	public final double getY2() {
		return y2;
	}

	public XPoint2D getCtrlP1() {
		return new XPoint2D(ctrlx1, ctrly1);
	}

	public XPoint2D getCtrlP2() {
		return new XPoint2D(ctrlx2, ctrly2);
	}

	public double getFlatnessSq() {
		return Math.max(XLine2D.ptSegDistSq(x1, y1, x2, y2, ctrlx1, ctrly1),
				XLine2D.ptSegDistSq(x1, y1, x2, y2, ctrlx2, ctrly2));
	}

	public double getFlatness() {
		return getFlatness(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), getX2(), getY2());
	}

	private static double getFlatness(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2,
			double x2, double y2) {
		return Math.sqrt(getFlatnessSq(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2));
	}

	private static double getFlatnessSq(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2,
			double ctrly2, double x2, double y2) {
		return Math.max(XLine2D.ptSegDistSq(x1, y1, x2, y2, ctrlx1, ctrly1),
				XLine2D.ptSegDistSq(x1, y1, x2, y2, ctrlx2, ctrly2));

	}

}
