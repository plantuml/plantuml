package net.sourceforge.plantuml.klimt.geom;

public class PathIterator {

	public static final int WIND_EVEN_ODD = java.awt.geom.PathIterator.WIND_EVEN_ODD;
	public static final int WIND_NON_ZERO = java.awt.geom.PathIterator.WIND_NON_ZERO;
	public static final int SEG_MOVETO = java.awt.geom.PathIterator.SEG_MOVETO;
	public static final int SEG_LINETO = java.awt.geom.PathIterator.SEG_LINETO;
	public static final int SEG_CUBICTO = java.awt.geom.PathIterator.SEG_CUBICTO;
	public static final int SEG_QUADTO = java.awt.geom.PathIterator.SEG_QUADTO;
	public static final int SEG_CLOSE = java.awt.geom.PathIterator.SEG_CLOSE;

	public boolean isDone() {
		throw new UnsupportedOperationException();
	}

	public void next() {
		throw new UnsupportedOperationException();
	}

	public int getWindingRule() {
		throw new UnsupportedOperationException();
	}

	public int currentSegment(double[] coord) {
		throw new UnsupportedOperationException();
	}

}
