package com.ctreber.acearth.plugins.markers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import com.ctreber.acearth.gui.PixelCanvas;
import com.ctreber.acearth.projection.Projection;
import com.ctreber.acearth.projection.ProjectionOrtho;
import com.ctreber.acearth.util.Coordinate;
import com.ctreber.acearth.util.Point2D;
import com.ctreber.acearth.util.Point3D;
import com.ctreber.acearth.util.StringParser;

/**
 * <p>
 * Marks a location on the globe.
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public class Marker {
	private static final int MARKER_SIZE = 4;

	// types of marker label alignment
	private static final int MarkerAlignDefault = 0;
	private static final int MarkerAlignLeft = 1;
	private static final int MarkerAlignRight = 2;
	private static final int MarkerAlignAbove = 3;
	private static final int MarkerAlignBelow = 4;

	private Marker(Coordinate pCoordinate, String pLabel, int pAlign) {
		fCoordinate = pCoordinate;
		fLabel = pLabel;
		fAlign = pAlign;
	}

	private Coordinate fCoordinate;
	private String fLabel;
	private int fAlign;

	// private static List fMarkers;

	/*
	 * builtin_marker_data[] contains the "built-in" marker data that is
	 * compiled into AC.earth. (My apologies for misspellings, omissions of your
	 * favorite location, or geographic inaccuracies. This is primarily just a
	 * pile of data that I had handy instead of an attempt to provide a sample
	 * that is "globally correct" in some sense.)
	 */
	// public static List loadMarkerFile(String pFileName) throws IOException {
	// fMarkers = new ArrayList();
	//
	// final LineNumberReader lReader = new LineNumberReader(new
	// FileReader(pFileName));
	// String lLine;
	// while ((lLine = lReader.readLine()) != null) {
	// processLine(lLine);
	// }
	//
	// lReader.close();
	//
	// return fMarkers;
	// }
	//
	// private static void processLine(String pLine) {
	// final int lPos = pLine.indexOf('#');
	// if (lPos != -1) {
	// // Remove comment
	// pLine = pLine.substring(0, lPos);
	// }
	//
	// final Marker lMarkerInfo = createFromLine(pLine);
	// if (lMarkerInfo != null) {
	// fMarkers.add(lMarkerInfo);
	// }
	// }
	private static Marker createFromLine(String pLine) {
		final List lWords = StringParser.parse(pLine);

		final double lLat = Double.parseDouble((String) lWords.get(0));
		final double lLong = Double.parseDouble((String) lWords.get(1));
		final String lLabel = (String) lWords.get(2);

		int lAlign = MarkerAlignDefault;
		if (lWords.size() >= 4) {
			String lAlignString = (String) lWords.get(3);
			if (lAlignString.equalsIgnoreCase("left")) {
				lAlign = MarkerAlignLeft;
			}
			if (lAlignString.equalsIgnoreCase("right")) {
				lAlign = MarkerAlignRight;
			}
			if (lAlignString.equalsIgnoreCase("above")) {
				lAlign = MarkerAlignAbove;
			}
			if (lAlignString.equalsIgnoreCase("below")) {
				lAlign = MarkerAlignBelow;
			}
		}

		final Coordinate lPos = new Coordinate(lLat, lLong);
		if (!lPos.check()) {
			// ACearth.logError("latitude must be between -90 and 90, and
			// longitude must be between -180 and 180");
			return null;
		}

		return new Marker(lPos, lLabel, lAlign);
	}

	public String toString() {
		return fLabel + " (" + fCoordinate + "), align: " + fAlign;
	}

	// --Recycle Bin START (10/28/02 2:24 PM):
	// public String getLabel()
	// {
	// return fLabel;
	// }
	// --Recycle Bin STOP (10/28/02 2:24 PM)

	// --Recycle Bin START (10/28/02 2:24 PM):
	// public int getAlign()
	// {
	// return fAlign;
	// }
	// --Recycle Bin STOP (10/28/02 2:24 PM)

	// --Recycle Bin START (10/28/02 2:24 PM):
	// public Coordinate getLocation()
	// {
	// return fCoordinate;
	// }
	// --Recycle Bin STOP (10/28/02 2:24 PM)

	public void render(PixelCanvas pCanvas, Projection pProjection) {
		final Point3D lPos = pProjection.rotate(fCoordinate.getPoint3D());

		if ((pProjection instanceof ProjectionOrtho) && (lPos.getZ() <= 0)) {
			// Back side of the Earth.
			// Insight: We don't need to check if the marker is visible in other
			// projections because they always show the whole earth - and all
			// markers!
			return;
		}

		Point2D lPoint = pProjection.finalize(pProjection.project2D(lPos));
		final int lX = (int) lPoint.getX();
		final int lY = (int) lPoint.getY();

		// Draw a circle
		Graphics2D g2d = pCanvas.getGraphics2D();
		g2d.setColor(Color.red);
		// pCanvas.drawCircle(lX, lY, MARKER_SIZE);
		g2d.drawOval(lX, lY, MARKER_SIZE, MARKER_SIZE);

		if (fLabel != null) {
			switch (fAlign) {
			case Marker.MarkerAlignLeft:
				break;

			case Marker.MarkerAlignRight:
			case Marker.MarkerAlignDefault:
				// pCanvas.drawText(lX + MARKER_SIZE, lY + 4, fLabel);
				// fRenderTarget.setTextFont(fRenderTarget.getTextFont().deriveFont(9.0f));
				g2d.setFont(new Font("", Font.PLAIN, 9));
				g2d.drawString(fLabel, lX + MARKER_SIZE + 1, lY + 2);
				break;

			case Marker.MarkerAlignAbove:
				break;

			case Marker.MarkerAlignBelow:
				break;
			}
		}
	}

	public static Marker loadMarkerFile(String line) {
		return createFromLine(line);
	}
}
