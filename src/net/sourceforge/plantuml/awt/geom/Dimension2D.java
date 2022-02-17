package net.sourceforge.plantuml.awt.geom;

public class Dimension2D {

	private final double width;
	private final double height;

	public Dimension2D() {
		this(0, 0);
	}

	public Dimension2D(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
	public void setSize(double width, double height) {
		throw new UnsupportedOperationException();
	}


}
