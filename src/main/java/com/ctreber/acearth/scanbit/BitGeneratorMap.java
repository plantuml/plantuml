package com.ctreber.acearth.scanbit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.ctreber.acearth.projection.Projection;
import com.ctreber.acearth.util.EdgeCrossing;
import com.ctreber.acearth.util.Point2D;
import com.ctreber.acearth.util.Point3D;
import com.ctreber.acearth.util.Polygon;
import com.ctreber.aclib.sort.CTSort;
import com.ctreber.aclib.sort.QuickSort;

/**
 * <p>
 * A BitGeneratorMap scans a map into ScanBits.
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public abstract class BitGeneratorMap extends ScanBitGenerator {
	// Types of pixels.
	public static final int PixTypeSpace = 0;
	public static final int PixTypeLand = 1;
	public static final int PixTypeWater = 2;
	public static final int PixTypeStar = 3;
	public static final int PixTypeGridLand = 4;
	public static final int PixTypeGridWater = 5;

	// Parameters influencing generateScanBits buffer genertion.
	private Polygon[] fMapData;
	Projection fProjection;
	private List fScanbitsVector = new ArrayList();
	// Created by scanPolygon
	List fEdgeCrossings;

	abstract protected ScanBuf scanOutline();

	abstract protected void handleCrossings(ScanBuf pScanBuf, EdgeCrossing[] pEdgeCrossings);

	abstract protected Comparator getEdgeXingComparator();

	abstract protected void scanPolygon(ScanBuf pScanBuf, Point3D[] pPoints3D, Point2D[] pPoints2D, int pIndex);

	public BitGeneratorMap(Projection pProjection) {
		fProjection = pProjection;
	}

	/**
	 * <p>
	 * Create outline for the map, scan all polygons.
	 */
	public void generateScanBits() {
		// Prepare data.
		fScanbitsVector = new ArrayList();
		fProjection.setImageWidth(fImageWidth);
		fProjection.setImageHeight(fImageHeight);
		fProjection.initTransformTable();

		// Trace outling and polygons.
		final ScanBuf lScanBuf = scanOutline();
		fScanbitsVector.addAll(lScanBuf.getScanbits(64));
		scanPolygons();

		// Dress results.
		final CTSort lSort = new QuickSort();
		fScanBitsArray = (ScanBit[]) fScanbitsVector.toArray(new ScanBit[0]);
		lSort.sort(fScanBitsArray);
	}

	private void scanPolygons() {
		for (int lPolyNo = 0; lPolyNo < fMapData.length; lPolyNo++) {
			Polygon lPolygon = fMapData[lPolyNo];

			Point3D[] lPoints3D = new Point3D[lPolygon.getSize()];
			Point2D[] lPoints2D = new Point2D[lPolygon.getSize()];
			transformPolygonPoints(lPolygon, lPoints3D, lPoints2D);

			// For all points in polygon...
			fEdgeCrossings = new ArrayList();
			ScanBuf lScanBuf = new ScanBuf(fImageHeight, fImageWidth);
			for (int i = 0; i < lPoints2D.length; i++) {
				scanPolygon(lScanBuf, lPoints3D, lPoints2D, i);
			}

			if (fEdgeCrossings.size() > 0) {
				// Edge crossings have been generated, deal with them.
				final EdgeCrossing[] xings = (EdgeCrossing[]) fEdgeCrossings.toArray(new EdgeCrossing[0]);
				final CTSort lSort = new QuickSort();
				lSort.sort(xings, getEdgeXingComparator());
				handleCrossings(lScanBuf, xings);
			}

			if (lScanBuf.containsPoints()) {
				// Scan lines have been generated, transform them into ScanBit.
				fScanbitsVector.addAll(lScanBuf.getScanbits(lPolygon.getType()));
			}
		}
	}

	/**
	 * The transformation rotates 3D and projects 2D points from it
	 */
	private void transformPolygonPoints(Polygon pPolygon, Point3D[] pPoints3D, Point2D[] pPoints2D) {
		for (int i = 0; i < pPolygon.getPoints().length; i++) {
			Point3D lPoint = pPolygon.getPoints()[i];

			Point3D lPointRotated = fProjection.rotate(lPoint);
			pPoints3D[i] = lPointRotated;
			pPoints2D[i] = fProjection.project2D(lPointRotated);
		}
	}

	public void setMapData(Polygon[] pMapData) {
		fMapData = pMapData;
	}

	protected void addEdgeXing(EdgeCrossing pEdgeXing) {
		fEdgeCrossings.add(pEdgeXing);
	}
}
