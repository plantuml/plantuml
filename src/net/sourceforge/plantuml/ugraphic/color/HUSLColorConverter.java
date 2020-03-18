package net.sourceforge.plantuml.ugraphic.color;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Taken from
 * https://github.com/hsluv/hsluv-java/blob/master/src/main/java/org/hsluv/HUSLColorConverter.java
 * 
 * Some other pointer:
 * https://twitter.com/kuon_orochi/ https://www.hsluv.org/
 * https://www.kuon.ch/post/2020-03-08-hsluv/
 * 
 * 
 * @author Alexei Boronine
 *
 */
public class HUSLColorConverter {
	private static double[][] m = new double[][] {
			new double[] { 3.240969941904521, -1.537383177570093, -0.498610760293 },
			new double[] { -0.96924363628087, 1.87596750150772, 0.041555057407175 },
			new double[] { 0.055630079696993, -0.20397695888897, 1.056971514242878 }, };

	private static double[][] minv = new double[][] {
			new double[] { 0.41239079926595, 0.35758433938387, 0.18048078840183 },
			new double[] { 0.21263900587151, 0.71516867876775, 0.072192315360733 },
			new double[] { 0.019330818715591, 0.11919477979462, 0.95053215224966 }, };

	private static double refY = 1.0;

	private static double refU = 0.19783000664283;
	private static double refV = 0.46831999493879;

	private static double kappa = 903.2962962;
	private static double epsilon = 0.0088564516;

	private static List<double[]> getBounds(double L) {
		ArrayList<double[]> result = new ArrayList<double[]>();

		double sub1 = Math.pow(L + 16, 3) / 1560896;
		double sub2 = sub1 > epsilon ? sub1 : L / kappa;

		for (int c = 0; c < 3; ++c) {
			double m1 = m[c][0];
			double m2 = m[c][1];
			double m3 = m[c][2];

			for (int t = 0; t < 2; ++t) {
				double top1 = (284517 * m1 - 94839 * m3) * sub2;
				double top2 = (838422 * m3 + 769860 * m2 + 731718 * m1) * L * sub2 - 769860 * t * L;
				double bottom = (632260 * m3 - 126452 * m2) * sub2 + 126452 * t;

				result.add(new double[] { top1 / bottom, top2 / bottom });
			}
		}

		return result;
	}

	private static double intersectLineLine(double[] lineA, double[] lineB) {
		return (lineA[1] - lineB[1]) / (lineB[0] - lineA[0]);
	}

	private static double distanceFromPole(double[] point) {
		return Math.sqrt(Math.pow(point[0], 2) + Math.pow(point[1], 2));
	}

	private static Length lengthOfRayUntilIntersect(double theta, double[] line) {
		double length = line[1] / (Math.sin(theta) - line[0] * Math.cos(theta));

		return new Length(length);
	}

	private static class Length {
		final boolean greaterEqualZero;
		final double length;

		private Length(double length) {
			this.greaterEqualZero = length >= 0;
			this.length = length;
		}
	}

	private static double maxSafeChromaForL(double L) {
		List<double[]> bounds = getBounds(L);
		double min = Double.MAX_VALUE;

		for (int i = 0; i < 2; ++i) {
			double m1 = bounds.get(i)[0];
			double b1 = bounds.get(i)[1];
			double[] line = new double[] { m1, b1 };

			double x = intersectLineLine(line, new double[] { -1 / m1, 0 });
			double length = distanceFromPole(new double[] { x, b1 + x * m1 });

			min = Math.min(min, length);
		}

		return min;
	}

	private static double maxChromaForLH(double L, double H) {
		double hrad = H / 360 * Math.PI * 2;

		List<double[]> bounds = getBounds(L);
		double min = Double.MAX_VALUE;

		for (double[] bound : bounds) {
			Length length = lengthOfRayUntilIntersect(hrad, bound);
			if (length.greaterEqualZero) {
				min = Math.min(min, length.length);
			}
		}

		return min;
	}

	private static double dotProduct(double[] a, double[] b) {
		double sum = 0;

		for (int i = 0; i < a.length; ++i) {
			sum += a[i] * b[i];
		}

		return sum;
	}

	private static double round(double value, int places) {
		double n = Math.pow(10, places);

		return Math.round(value * n) / n;
	}

	private static double fromLinear(double c) {
		if (c <= 0.0031308) {
			return 12.92 * c;
		} else {
			return 1.055 * Math.pow(c, 1 / 2.4) - 0.055;
		}
	}

	private static double toLinear(double c) {
		if (c > 0.04045) {
			return Math.pow((c + 0.055) / (1 + 0.055), 2.4);
		} else {
			return c / 12.92;
		}
	}

	private static int[] rgbPrepare(double[] tuple) {

		int[] results = new int[tuple.length];

		for (int i = 0; i < tuple.length; ++i) {
			double chan = tuple[i];
			double rounded = round(chan, 3);

			if (rounded < -0.0001 || rounded > 1.0001) {
				throw new IllegalArgumentException("Illegal rgb value: " + rounded);
			}

			results[i] = (int) Math.round(rounded * 255);
		}

		return results;
	}

	public static double[] xyzToRgb(double[] tuple) {
		return new double[] { fromLinear(dotProduct(m[0], tuple)), fromLinear(dotProduct(m[1], tuple)),
				fromLinear(dotProduct(m[2], tuple)), };
	}

	public static double[] rgbToXyz(double[] tuple) {
		double[] rgbl = new double[] { toLinear(tuple[0]), toLinear(tuple[1]), toLinear(tuple[2]), };

		return new double[] { dotProduct(minv[0], rgbl), dotProduct(minv[1], rgbl), dotProduct(minv[2], rgbl), };
	}

	private static double yToL(double Y) {
		if (Y <= epsilon) {
			return (Y / refY) * kappa;
		} else {
			return 116 * Math.pow(Y / refY, 1.0 / 3.0) - 16;
		}
	}

	private static double lToY(double L) {
		if (L <= 8) {
			return refY * L / kappa;
		} else {
			return refY * Math.pow((L + 16) / 116, 3);
		}
	}

	public static double[] xyzToLuv(double[] tuple) {
		double X = tuple[0];
		double Y = tuple[1];
		double Z = tuple[2];

		double varU = (4 * X) / (X + (15 * Y) + (3 * Z));
		double varV = (9 * Y) / (X + (15 * Y) + (3 * Z));

		double L = yToL(Y);

		if (L == 0) {
			return new double[] { 0, 0, 0 };
		}

		double U = 13 * L * (varU - refU);
		double V = 13 * L * (varV - refV);

		return new double[] { L, U, V };
	}

	public static double[] luvToXyz(double[] tuple) {
		double L = tuple[0];
		double U = tuple[1];
		double V = tuple[2];

		if (L == 0) {
			return new double[] { 0, 0, 0 };
		}

		double varU = U / (13 * L) + refU;
		double varV = V / (13 * L) + refV;

		double Y = lToY(L);
		double X = 0 - (9 * Y * varU) / ((varU - 4) * varV - varU * varV);
		double Z = (9 * Y - (15 * varV * Y) - (varV * X)) / (3 * varV);

		return new double[] { X, Y, Z };
	}

	public static double[] luvToLch(double[] tuple) {
		double L = tuple[0];
		double U = tuple[1];
		double V = tuple[2];

		double C = Math.sqrt(U * U + V * V);
		double H;

		if (C < 0.00000001) {
			H = 0;
		} else {
			double Hrad = Math.atan2(V, U);

			// pi to more digits than they provide it in the stdlib
			H = (Hrad * 180.0) / 3.1415926535897932;

			if (H < 0) {
				H = 360 + H;
			}
		}

		return new double[] { L, C, H };
	}

	public static double[] lchToLuv(double[] tuple) {
		double L = tuple[0];
		double C = tuple[1];
		double H = tuple[2];

		double Hrad = H / 360.0 * 2 * Math.PI;
		double U = Math.cos(Hrad) * C;
		double V = Math.sin(Hrad) * C;

		return new double[] { L, U, V };
	}

	public static double[] hsluvToLch(double[] tuple) {
		double H = tuple[0];
		double S = tuple[1];
		double L = tuple[2];

		if (L > 99.9999999) {
			return new double[] { 100d, 0, H };
		}

		if (L < 0.00000001) {
			return new double[] { 0, 0, H };
		}

		double max = maxChromaForLH(L, H);
		double C = max / 100 * S;

		return new double[] { L, C, H };
	}

	public static double[] lchToHsluv(double[] tuple) {
		double L = tuple[0];
		double C = tuple[1];
		double H = tuple[2];

		if (L > 99.9999999) {
			return new double[] { H, 0, 100 };
		}

		if (L < 0.00000001) {
			return new double[] { H, 0, 0 };
		}

		double max = maxChromaForLH(L, H);
		double S = C / max * 100;

		return new double[] { H, S, L };
	}

	public static double[] hpluvToLch(double[] tuple) {
		double H = tuple[0];
		double S = tuple[1];
		double L = tuple[2];

		if (L > 99.9999999) {
			return new double[] { 100, 0, H };
		}

		if (L < 0.00000001) {
			return new double[] { 0, 0, H };
		}

		double max = maxSafeChromaForL(L);
		double C = max / 100 * S;

		return new double[] { L, C, H };
	}

	public static double[] lchToHpluv(double[] tuple) {
		double L = tuple[0];
		double C = tuple[1];
		double H = tuple[2];

		if (L > 99.9999999) {
			return new double[] { H, 0, 100 };
		}

		if (L < 0.00000001) {
			return new double[] { H, 0, 0 };
		}

		double max = maxSafeChromaForL(L);
		double S = C / max * 100;

		return new double[] { H, S, L };
	}

	public static String rgbToHex(double[] tuple) {
		int[] prepared = rgbPrepare(tuple);

		return String.format("#%02x%02x%02x", prepared[0], prepared[1], prepared[2]);
	}

	public static double[] hexToRgb(String hex) {
		return new double[] { Integer.parseInt(hex.substring(1, 3), 16) / 255.0,
				Integer.parseInt(hex.substring(3, 5), 16) / 255.0, Integer.parseInt(hex.substring(5, 7), 16) / 255.0, };
	}

	public static double[] lchToRgb(double[] tuple) {
		return xyzToRgb(luvToXyz(lchToLuv(tuple)));
	}

	public static double[] rgbToLch(double[] tuple) {
		return luvToLch(xyzToLuv(rgbToXyz(tuple)));
	}

	// RGB <--> HUSL(p)

	public static double[] hsluvToRgb(double[] tuple) {
		return lchToRgb(hsluvToLch(tuple));
	}

	public static double[] rgbToHsluv(double[] tuple) {
		return lchToHsluv(rgbToLch(tuple));
	}

	public static double[] hpluvToRgb(double[] tuple) {
		return lchToRgb(hpluvToLch(tuple));
	}

	public static double[] rgbToHpluv(double[] tuple) {
		return lchToHpluv(rgbToLch(tuple));
	}

	// Hex

	public static String hsluvToHex(double[] tuple) {
		return rgbToHex(hsluvToRgb(tuple));
	}

	public static String hpluvToHex(double[] tuple) {
		return rgbToHex(hpluvToRgb(tuple));
	}

	public static double[] hexToHsluv(String s) {
		return rgbToHsluv(hexToRgb(s));
	}

	public static double[] hexToHpluv(String s) {
		return rgbToHpluv(hexToRgb(s));
	}

}