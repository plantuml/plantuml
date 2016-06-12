package com.ctreber.acearth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.ctreber.acearth.util.Point3D;
import com.ctreber.acearth.util.Polygon;

/**
 * The map data file is a big array of short (16-bit) ints, as follows: - it is
 * a sequence of closed curves - the first value in a curve is the number of
 * points in the curve - the second value in a curve indicates land/water (1 or
 * -1, respectively) - this is followed by an [x,y,z] triple that indicates a
 * point on the unit sphere (each of x, y, and z has been scaled by 30000),
 * where the x axis points "to the right" (towards 0 N 90 E), the y axis points
 * "up" (towards the north pole), and the z axis points "out of the screen"
 * (towards 0 N 0 E). this is the starting point of the curve. - this is
 * followed by (one less than the number of points in the curve) triples
 * [dx,dy,dz]; the [x,y,z] triple for each successive point in the curve is
 * obtained by adding [dx,dy,dz] onto the previous [x,y,z] values. - the curves
 * are [must be!] non-self-intersecting and traced in a counter-clockwise
 * direction
 * 
 * the curves are sampled at a (roughly) a 20 mile resolution.
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public class MapDataReader {
	/** Point value scale (devide value by this number). */
	private static final double MAP_DATA_SCALE = 30000.0;

	private static List fData;
	private static List fPolygons;
	private static int fIndex;

	/**
	 * <p>
	 * Read map data.
	 * 
	 * @param pFileName
	 *            Map data file name.
	 * @return Array of map polygons.
	 * @throws IOException
	 */
	public static Polygon[] readMapData() throws IOException {
		final List lines = new MapData().getLines();
		
		fData = new ArrayList();
		for (Iterator it = lines.iterator(); it.hasNext(); ) {
			String lLine = (String) it.next();
			if (lLine.indexOf("/*") != -1) {
				// Filter out comments.
				continue;
			}

			StringTokenizer lST = new StringTokenizer(lLine, ", ");
			while (lST.hasMoreTokens()) {
				String lToken = lST.nextToken();
				final Integer lValue = new Integer(lToken);
				fData.add(lValue);
			}
		}

		fPolygons = new ArrayList();
		fIndex = 0;
		while (getValue(fIndex) != 0) {
			processCurve();
		}

		return (Polygon[]) fPolygons.toArray(new Polygon[0]);
	}

	private static void processCurve() {
		final int lNPoint = getValue(fIndex++);
		final int lType = getValue(fIndex++);

		final Point3D[] lPoints = new Point3D[lNPoint];
		final Point3D lPoint3D = new Point3D(getValue(fIndex++) / MAP_DATA_SCALE, getValue(fIndex++) / MAP_DATA_SCALE,
				getValue(fIndex++) / MAP_DATA_SCALE);

		lPoints[0] = lPoint3D;
		for (int i = 1; i < lNPoint; i++) {
			lPoints[i] = new Point3D(lPoints[i - 1].getX() + getValue(fIndex++) / MAP_DATA_SCALE, lPoints[i - 1].getY()
					+ getValue(fIndex++) / MAP_DATA_SCALE, lPoints[i - 1].getZ() + getValue(fIndex++) / MAP_DATA_SCALE);
		}

		final Polygon lPolygon = new Polygon(lType, lPoints);
		fPolygons.add(lPolygon);
	}

	/**
	 * <p>
	 * Get value of raw data at specified point.
	 * 
	 * @param pIndex
	 *            Index of value.
	 * @return Value of raw data at specified point.
	 */
	private static int getValue(int pIndex) {
		return ((Integer) fData.get(pIndex)).intValue();
	}

}
