package net.sourceforge.plantuml.awt.geom;

import net.sourceforge.plantuml.awt.Shape;

public class Rectangle2D implements Shape {

	public final double x;
	public final double y;
	public final double width;
	public final double height;

	public Rectangle2D(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	public static class Double extends Rectangle2D {

		public Double(double x, double y, double width, double height) {
			super(x, y, width, height);

		}

	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getCenterX() {
		return x;
	}

	public double getCenterY() {
		return y;
	}

	public double getMinX() {
		return x;
	}

	public double getMaxX() {
		return x + width;
	}

	public double getMinY() {
		return y;
	}

	public double getMaxY() {
		return y + height;
	}

	public boolean intersects(Rectangle2D rectangle) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(Point2D point) {
		// TODO Auto-generated method stub
		return false;
	}

}
