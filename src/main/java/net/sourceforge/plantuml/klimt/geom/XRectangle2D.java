package net.sourceforge.plantuml.klimt.geom;

public class XRectangle2D {

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

	public XPoint2D intersect(XLine2D line) {
		final XPoint2D a = new XPoint2D(x, y);
		final XPoint2D b = new XPoint2D(x + width, y);
		final XPoint2D c = new XPoint2D(x + width, y + height);
		final XPoint2D d = new XPoint2D(x, y + height);
		final XLine2D line1 = XLine2D.line(a, b);
		final XLine2D line2 = XLine2D.line(b, c);
		final XLine2D line3 = XLine2D.line(c, d);
		final XLine2D line4 = XLine2D.line(d, a);

		XPoint2D result = line.intersect(line1);
		if (result != null)
			return result;
		result = line.intersect(line2);
		if (result != null)
			return result;
		result = line.intersect(line3);
		if (result != null)
			return result;
		result = line.intersect(line4);
		if (result != null)
			return result;

		return null;
	}

}
