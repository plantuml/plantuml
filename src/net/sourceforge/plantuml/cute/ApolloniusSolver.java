package net.sourceforge.plantuml.cute;

// http://rosettacode.org/wiki/Problem_of_Apollonius#Java
public class ApolloniusSolver {

	static class Circle {
		public double[] center;
		public double radius;

		public Circle(double[] center, double radius) {
			this.center = center;
			this.radius = radius;
		}

		public String toString() {
			return String.format("Circle[x=%.2f,y=%.2f,r=%.2f]", center[0], center[1], radius);
		}
	}

	/**
	 * Solves the Problem of Apollonius (finding a circle tangent to three other circles in the plane). The method uses
	 * approximately 68 heavy operations (multiplication, division, square-roots).
	 * 
	 * @param c1
	 *            One of the circles in the problem
	 * @param c2
	 *            One of the circles in the problem
	 * @param c3
	 *            One of the circles in the problem
	 * @param s1
	 *            An indication if the solution should be externally or internally tangent (+1/-1) to c1
	 * @param s2
	 *            An indication if the solution should be externally or internally tangent (+1/-1) to c2
	 * @param s3
	 *            An indication if the solution should be externally or internally tangent (+1/-1) to c3
	 * @return The circle that is tangent to c1, c2 and c3.
	 */
	public static Circle solveApollonius(Circle c1, Circle c2, Circle c3, int s1, int s2, int s3) {
		double x1 = c1.center[0];
		double y1 = c1.center[1];
		double r1 = c1.radius;
		double x2 = c2.center[0];
		double y2 = c2.center[1];
		double r2 = c2.radius;
		double x3 = c3.center[0];
		double y3 = c3.center[1];
		double r3 = c3.radius;

		// Currently optimized for fewest multiplications. Should be optimized for
		// readability
		double v11 = 2 * x2 - 2 * x1;
		double v12 = 2 * y2 - 2 * y1;
		double v13 = x1 * x1 - x2 * x2 + y1 * y1 - y2 * y2 - r1 * r1 + r2 * r2;
		double v14 = 2 * s2 * r2 - 2 * s1 * r1;

		double v21 = 2 * x3 - 2 * x2;
		double v22 = 2 * y3 - 2 * y2;
		double v23 = x2 * x2 - x3 * x3 + y2 * y2 - y3 * y3 - r2 * r2 + r3 * r3;
		double v24 = 2 * s3 * r3 - 2 * s2 * r2;

		double w12 = v12 / v11;
		double w13 = v13 / v11;
		double w14 = v14 / v11;

		double w22 = v22 / v21 - w12;
		double w23 = v23 / v21 - w13;
		double w24 = v24 / v21 - w14;

		double P = -w23 / w22;
		double Q = w24 / w22;
		double M = -w12 * P - w13;
		double N = w14 - w12 * Q;

		double a = N * N + Q * Q - 1;
		double b = 2 * M * N - 2 * N * x1 + 2 * P * Q - 2 * Q * y1 + 2 * s1 * r1;
		double c = x1 * x1 + M * M - 2 * M * x1 + P * P + y1 * y1 - 2 * P * y1 - r1 * r1;

		// Find a root of a quadratic equation. This requires the circle centers not
		// to be e.g. colinear
		double D = b * b - 4 * a * c;
		double rs = (-b - Math.sqrt(D)) / (2 * a);
		double xs = M + N * rs;
		double ys = P + Q * rs;
		return new Circle(new double[] { xs, ys }, rs);
	}

	public static void main(final String[] args) {
		Circle c1 = new Circle(new double[] { 0, 0 }, 1);
		Circle c2 = new Circle(new double[] { 4, 0 }, 1);
		Circle c3 = new Circle(new double[] { 2, 4 }, 2);
		// Expects "Circle[x=2.00,y=2.10,r=3.90]" (green circle in image)
		System.out.println(solveApollonius(c1, c2, c3, 1, 1, 1));
		// Expects "Circle[x=2.00,y=0.83,r=1.17]" (red circle in image)
		System.out.println(solveApollonius(c1, c2, c3, -1, -1, -1));
	}

}
