package net.sourceforge.plantuml.awt.geom;

public class Point2D {

	public double x;
	public double y;

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public static class Double extends Point2D {

		public Double(double x, double y) {
			super(x, y);
		}

		public Double() {
			this(0, 0);
		}

	}

	public double distance(Point2D other) {
		final double px = other.getX() - this.getX();
		final double py = other.getY() - this.getY();
		return Math.sqrt(px * px + py * py);
	}

	public double distanceSq(Point2D other) {
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

	public void setLocation(double px, double py) {
		this.x = px;
		this.y = py;
	}

}
