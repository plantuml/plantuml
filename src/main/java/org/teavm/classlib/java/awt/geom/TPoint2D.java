/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for java.awt.geom.Point2D.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.teavm.classlib.java.awt.geom;

public abstract class TPoint2D {
	public abstract double getX();

	public abstract double getY();

	public abstract void setLocation(double x, double y);

	// Nested class: java.awt.geom.Point2D$Double
	public static class Double extends TPoint2D {
		public double x;
		public double y;

		public Double() {
		}

		public Double(double x, double y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public double getX() {
			return x;
		}

		@Override
		public double getY() {
			return y;
		}

		@Override
		public void setLocation(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
}
