package com.ctreber.acearth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.ctreber.acearth.gui.CanvasACearth;
import com.ctreber.acearth.plugins.Plugin;
import com.ctreber.acearth.plugins.markers.Marker;
import com.ctreber.acearth.plugins.markers.PluginMarkers;
import com.ctreber.acearth.projection.Projection;
import com.ctreber.acearth.projection.ProjectionCyl;
import com.ctreber.acearth.projection.ProjectionMerc;
import com.ctreber.acearth.projection.ProjectionOrtho;
import com.ctreber.acearth.renderer.Renderer;
import com.ctreber.acearth.renderer.RowTypeRendererScanBit;
import com.ctreber.acearth.renderer.RowTypeRendererScanDot;
import com.ctreber.acearth.scanbit.BitGeneratorMap;
import com.ctreber.acearth.scanbit.BitGeneratorMapDefault;
import com.ctreber.acearth.scanbit.BitGeneratorMapOrtho;
import com.ctreber.acearth.scandot.DotGeneratorLines;
import com.ctreber.acearth.scandot.DotGeneratorStars;
import com.ctreber.acearth.scandot.ScanDot;
import com.ctreber.acearth.scandot.ScanDotGenerator;
import com.ctreber.acearth.shader.Shader;
import com.ctreber.acearth.shader.ShaderDefault;
import com.ctreber.acearth.shader.ShaderFlat;
import com.ctreber.acearth.shader.ShaderOrtho;
import com.ctreber.acearth.util.Coordinate;
import com.ctreber.acearth.util.SunPositionCalculator;
import com.ctreber.acearth.util.Toolkit;
import com.ctreber.aclib.sort.CTSort;
import com.ctreber.aclib.sort.QuickSort;

/**
 * <h1>AC.earth - XEarth for Java
 * <h1>
 * 
 * <p>
 * The original XEarth was written by Kirk Johnson in July 1993 - thank you for
 * writing this great little program and making it available for free!
 * 
 * <p>
 * I wanted to extend the program, but not in C. So I created this Java version,
 * and found the process quite <strike>painfull</strike> interesting. The
 * biggest effort went into resolving references between C files and
 * eliminatiing pointers.
 * 
 * <h1>License</h1>
 * 
 * <p>
 * AC.earth Copyright (c) 2002 Christian Treber, ct@ctreber.com
 * 
 * <p>
 * AC.earth is based on XEarth by Kirk Johnson
 * 
 * <p>
 * To comply with the XEarth license I include the following text:
 * 
 * <pre>
 * XEarth Copyright (C) 1989, 1990, 1993-1995, 1999 Kirk Lauritz Johnson
 * Parts of the source code are:
 *   Copyright (C) 1989, 1990, 1991 by Jim Frost
 *   Copyright (C) 1992 by Jamie Zawinski &lt;jwz@lucid.com&gt;
 * Permission to use, copy, modify and freely distribute xearth for
 * non-commercial and not-for-profit purposes is hereby granted
 * without fee, provided that both the above copyright notice and this
 * permission notice appear in all copies and in supporting
 * documentation.
 * [Section refering to GIF omitted because it doesn't apply to this version]
 * The author makes no representations about the suitability of this
 * software for any purpose. It is provided &quot;as is&quot; without express or
 * implied warranty.
 * THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS,
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, INDIRECT
 * OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
 * LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN
 * CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * </pre>
 * 
 * <p>
 * The license for this program (AC.earth) is the same as the quoted license
 * above, with one change: The "copyright notice and permission notice" shall
 * include the entire text of this section.
 * 
 * todo Phase 2: Make grid value stuff more meaningful ("every n degrees") todo
 * Phase 2: Enter fixed time as data and time, not seconds since epoch todo
 * Phase 2: Compact map data into binary file
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * 
 * @author Christian Treber, ct@ctreber.com
 */
public class ACearth {
	public static final String VERSION = "1.1";
	public static final String BUILD = "22.11.2002 004";

	// private static long fsStartTime = 0;

	private ConfigurationACearth fConf = new ConfigurationACearth();
	private long fCurrentTime;

	private CanvasACearth fCanvas;

	private Coordinate fViewPos;
	private double fViewRotation;

	private List fPlugins;

	/**
	 * <p>
	 * Well, the main class.
	 * @param markers 
	 */
	public ACearth(List<Marker> markers) {
		// fsStartTime = System.currentTimeMillis();

		fPlugins = new ArrayList();
		fPlugins.add(new PluginMarkers(markers));

	}

	public void exportPng(OutputStream os) throws IOException {
		fCanvas = new CanvasACearth(this, fConf.getInt("imageWidth"), fConf.getInt("imageHeight"));
		update();
		fCanvas.saveToImage(os);
	}

	public void update() throws IOException {
		Projection lProjection = null;
		Shader lShader = null;
		BitGeneratorMap lScanner = null;
		if (fConf.is("projection", "Cylindrical")) {
			lProjection = new ProjectionCyl();
			lScanner = new BitGeneratorMapDefault(lProjection);
			lShader = new ShaderDefault();
		}

		if (fConf.is("projection", "Mercator")) {
			lProjection = new ProjectionMerc();
			lScanner = new BitGeneratorMapDefault(lProjection);
			lShader = new ShaderDefault();
		}

		if (fConf.is("projection", "Orthographic")) {
			lProjection = new ProjectionOrtho();
			lScanner = new BitGeneratorMapOrtho(lProjection);
			lShader = new ShaderOrtho();
		}

		computePositions();
		lProjection.setImageWidth(fConf.getInt("imageWidth"));
		lProjection.setImageHeight(fConf.getInt("imageHeight"));
		lProjection.setShiftX(fConf.getInt("shiftX"));
		lProjection.setShiftY(fConf.getInt("shiftY"));
		lProjection.setViewMagnification(fConf.getDouble("viewMagnification"));
		lProjection.setViewPos(fViewPos);
		lProjection.setViewRotation(fViewRotation);

		lScanner.setImageWidth(fConf.getInt("imageWidth"));
		lScanner.setImageHeight(fConf.getInt("imageHeight"));
		lScanner.setMapData(MapDataReader.readMapData());
		// Process the map (produces ScanBit-s).
		lScanner.generateScanBits();

		// Process stars and lines (produces ScanDots-s).
		List lScanDots = new ArrayList();
		if (fConf.getBoolean("starsP")) {
			ScanDotGenerator lGenerator = new DotGeneratorStars(fConf.getInt("imageWidth"),
					fConf.getInt("imageHeight"), fConf.getDouble("starFrequency"), fConf.getInt("bigStars"), new Random(fCurrentTime));
			lGenerator.generateScanDots();
			lScanDots.addAll(lGenerator.getScanDots());
		}

		if (fConf.getBoolean("gridP")) {
			ScanDotGenerator lGenerator = new DotGeneratorLines(lProjection, fConf.getInt("gridDivision"), fConf
					.getInt("gridPixelDivision"));
			lGenerator.generateScanDots();
			lScanDots.addAll(lGenerator.getScanDots());
		}

		final CTSort lSort = new QuickSort();
		ScanDot[] lScanDotsArray = (ScanDot[]) lScanDots.toArray(new ScanDot[0]);
		lSort.sort(lScanDotsArray);

		if (!fConf.getBoolean("shadeP")) {
			lShader = new ShaderFlat();
		}
		lShader.setProjection(lProjection);
		lShader.setSunPos(fConf.getSunPos());
		lShader.setDaySideBrightness(fConf.getInt("daySideBrightness"));
		lShader.setTerminatorDiscontinuity(fConf.getInt("terminatorDiscontinuity"));
		lShader.setNightSideBrightness(fConf.getInt("nightSideBrightness"));
		lShader.init();

		Renderer lRenderer = new Renderer(fCanvas);

		RowTypeRendererScanBit lRowRendererScanBit = new RowTypeRendererScanBit();
		lRowRendererScanBit.setScanBits(lScanner.getScanBits());
		lRenderer.addRowTypeRenderer(lRowRendererScanBit);

		RowTypeRendererScanDot lRowRendererScanDot = new RowTypeRendererScanDot();
		lRowRendererScanDot.setScanDots(lScanDotsArray);
		lRenderer.addRowTypeRenderer(lRowRendererScanDot);

		lRenderer.setShader(lShader);
		lRenderer.render();

		// Apply plugins
		Iterator lIt = fPlugins.iterator();
		while (lIt.hasNext()) {
			Plugin lPlugin = (Plugin) lIt.next();
			lPlugin.setProjection(lProjection);
			lPlugin.setRenderTarget(fCanvas);
			lPlugin.setParent(this);
			lPlugin.render();
		}

	}

	/**
	 * <p>
	 * This is repeated when time changes since this influences the position of
	 * Earth.
	 */
	private void computePositions() {
		// Determine time for rendering
		if (fConf.getInt("fixedTime") == 0) {
			// No fixed time.
			// final long lTimePassed = System.currentTimeMillis() - fsStartTime;
			// fCurrentTime = fsStartTime + (long) (fConf.getDouble("timeWarpFactor") * lTimePassed);
			fCurrentTime = System.currentTimeMillis();
		} else {
			// Fixed time.
			fCurrentTime = fConf.getInt("fixedTime") * 1000L;
		}

		if (fConf.getBoolean("sunMovesP")) {
			fConf.setSunPos(SunPositionCalculator.getSunPositionOnEarth(fCurrentTime));
		}

		// Determine viewing position
		if (fConf.is("viewPositionType", "Fixed")) {
			fViewPos = fConf.getViewPos();
		} else if (fConf.is("viewPositionType", "Sun-relative")) {
			fViewPos = getSunRelativePosition();
		} else if (fConf.is("viewPositionType", "Orbit")) {
			fViewPos = getOrbitPosition(fCurrentTime);
		} else if (fConf.is("viewPositionType", "Random")) {
			fViewPos = getRandomPosition();
		} else if (fConf.is("viewPositionType", "Moon")) {
			fViewPos = SunPositionCalculator.getMoonPositionOnEarth(fCurrentTime);
		}

		// for ViewRotGalactic, compute appropriate viewing rotation
		if (fConf.is("viewRotationType", "Galactic")) {
			fViewRotation = (Toolkit.degsToRads(fConf.getSunPos().getLat()
					* Math.sin((fViewPos.getLong() - fConf.getSunPos().getLong()))));
		} else {
			fViewRotation = fConf.getDouble("viewRotation");
		}
	}

	/**
	 * <p>
	 * Add sun position and position relative to sun, straighten out the result.
	 * 
	 * @return Position relativ to sun position as defined by fSunPosRel.
	 */
	private Coordinate getSunRelativePosition() {
		final Coordinate lPos = fConf.getSunPos();
		lPos.add(fConf.getSunPosRel());

		return lPos;
	}

	private Coordinate getOrbitPosition(long pTimeMillis) {
		double x, y, z;
		double a, c, s;
		double t1, t2;

		/* start at 0 N 0 E */
		x = 0;
		y = 0;
		z = 1;

		/*
		 * rotate in about y axis (from z towards x) according to the number of
		 * orbits we've completed
		 */
		a = (double) pTimeMillis / (fConf.getDouble("orbitPeriod") * 3600 * 1000) * 2 * Math.PI;
		c = Math.cos(a);
		s = Math.sin(a);
		t1 = c * z - s * x;
		t2 = s * z + c * x;
		z = t1;
		x = t2;

		/*
		 * rotate about z axis (from x towards y) according to the inclination
		 * of the orbit
		 */
		a = Toolkit.degsToRads(fConf.getDouble("orbitInclination"));
		c = Math.cos(a);
		s = Math.sin(a);
		t1 = c * x - s * y;
		t2 = s * x + c * y;
		x = t1;
		y = t2;

		/*
		 * rotate about y axis (from x towards z) according to the number of
		 * rotations the earth has made
		 */
		a = ((double) pTimeMillis / 86400000) * (2 * Math.PI);
		c = Math.cos(a);
		s = Math.sin(a);
		t1 = c * x - s * z;
		t2 = s * x + c * z;
		x = t1;
		z = t2;

		return new Coordinate(Toolkit.radsToDegs(Math.asin(y)), Toolkit.radsToDegs(Math.atan2(x, z)));
	}

	/**
	 * <p>
	 * Pick a position (lat, lon) at random
	 * 
	 * @return A random position.
	 */
	private static Coordinate getRandomPosition() {

		/* select a vector at random */
		final double[] pos = new double[3];
		double mag = 0;
		do {
			for (int i = 0; i < 3; i++) {
				pos[i] = ((Math.random() * 20000) * 1e-4) - 1;
				mag += pos[i] * pos[i];
			}
		} while ((mag > 1.0) || (mag < 0.01));

		/* normalize the vector */
		mag = Math.sqrt(mag);
		for (int i = 0; i < 3; i++) {
			pos[i] /= mag;
		}

		/* convert to (lat, lon) */
		final double s_lat = pos[1];
		final double c_lat = Math.sqrt(1 - s_lat * s_lat);
		final double s_lon = pos[0] / c_lat;
		final double c_lon = pos[2] / c_lat;

		return new Coordinate(Math.atan2(s_lat, c_lat) * (180 / Math.PI), Math.atan2(s_lon, c_lon) * (180 / Math.PI));
	}

//	public static long getStartTime() {
//		return fsStartTime;
//	}

	public ConfigurationACearth getConf() {
		return fConf;
	}

}