package net.sourceforge.plantuml.awt.geom;

import net.sourceforge.plantuml.awt.XShape;

public class XRectangle2D implements XShape {

	public final double x;
	public final double y;
	public final double width;
	public final double height;

	public XRectangle2D(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

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
		return x + width / 2;
	}

	public double getCenterY() {
		return y + height / 2;
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

	public boolean intersects(XRectangle2D other) {
		return intersects(other.getX(), other.getY(), other.getWidth(), other.getHeight());
	}

	private boolean intersects(double x, double y, double w, double h) {
		return w > 0 && h > 0 && getWidth() > 0 && getHeight() > 0 && x < getX() + getWidth() && x + w > getX()
				&& y < getY() + getHeight() && y + h > getY();
	}

	public boolean contains(XPoint2D point) {
		throw new UnsupportedOperationException();
	}

	public boolean contains(double xp, double yp) {
		if (width <= 0 || height <= 0)
			throw new IllegalStateException();
		return xp >= getMinX() && xp < getMaxX() && yp >= getMinY() && yp < getMaxY();
	}

}
