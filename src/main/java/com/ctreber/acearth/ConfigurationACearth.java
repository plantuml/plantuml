package com.ctreber.acearth;

import com.ctreber.acearth.util.Coordinate;
import com.ctreber.aclib.gui.MOBoolean;
import com.ctreber.aclib.gui.MODouble;
import com.ctreber.aclib.gui.MOEnum;
import com.ctreber.aclib.gui.MOInteger;
import com.ctreber.aclib.gui.MOString;
import com.ctreber.aclib.gui.MonitoredObject;

/**
 * <p>
 * How to avoid writing all the accessors? Code generator that creates derived
 * class from template class? Configuration items in data structure?
 * </p>
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com (06.10.2002)
 * </p>
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public class ConfigurationACearth extends Configuration {
	private static final int DEFAULT_DIMENSION = 512;

	public ConfigurationACearth() {
		final MOEnum lProjection = new MOEnum();
		lProjection.addValidValue("Mercator");
		lProjection.addValidValue("Orthographic");
		lProjection.addValidValue("Cylindrical");
		lProjection.set("Orthographic");
		add("projection", (MonitoredObject) lProjection);

		final MOEnum lPositionType = new MOEnum();
		lPositionType.addValidValue("Fixed");
		lPositionType.addValidValue("Sun-relative");
		lPositionType.addValidValue("Orbit");
		lPositionType.addValidValue("Random");
		lPositionType.addValidValue("Moon");
		lPositionType.set("Sun-relative");
		add("viewPositionType", lPositionType);

		final MOEnum lViewRotationType = new MOEnum();
		lViewRotationType.addValidValue("North");
		lViewRotationType.addValidValue("Galactic");
		lViewRotationType.set("North");
		add("viewRotationType", lViewRotationType);

		final MOString lOutputMode = new MOString("gui");
		add("outputMode", lOutputMode);

		// Only relevant if view type is "Fixed"./
		final MODouble lViewPosLat = new MODouble(0, -90, +90);
		add("viewPosLat", lViewPosLat);
		final MODouble lViewPosLong = new MODouble(0, -180, +180);
		add("viewPosLong", lViewPosLong);
		// Only relevant if view type is "Sun-relative".
		final MODouble lSunPosRelLat = new MODouble(0, -90, +90);
		add("sunPosRelLat", lSunPosRelLat);
		final MODouble lSunPosRelLong = new MODouble(0, -180, +180);
		add("sunPosRelLong", lSunPosRelLong);

		final MOBoolean lSunMovesP = new MOBoolean(true);
		add("sunMovesP", lSunMovesP);
		// Only relevant if sun does not move.
		final MODouble lSunPosLat = new MODouble(0, -90, +90);
		add("sunPosLat", lSunPosLat);
		final MODouble lSunPosLong = new MODouble(0, -180, +180);
		add("sunPosLong", lSunPosLong);

		final MODouble lTimeWarpFactor = new MODouble(1.0, 0, Double.MAX_VALUE);
		add("timeWarpFactor", lTimeWarpFactor);
		final MOInteger lFixedTime = new MOInteger(0, 0, Integer.MAX_VALUE);
		add("fixedTime", lFixedTime);
		final MOInteger lWaitTime = new MOInteger(300, 0, Integer.MAX_VALUE);
		add("waitTime", lWaitTime);

		final MODouble lOrbitPeriod = new MODouble(1, 0.0001, Double.MAX_VALUE);
		add("orbitPeriod", lOrbitPeriod);
		final MODouble lOrbitInclination = new MODouble(45.0, 0, 90);
		add("orbitInclination", lOrbitInclination);

		final MOBoolean lLabelP = new MOBoolean(false);
		add("labelP", lLabelP);

		final MOInteger lImageWidth = new MOInteger(DEFAULT_DIMENSION, 0, Integer.MAX_VALUE);
		add("imageWidth", lImageWidth);
		final MOInteger lImageHeight = new MOInteger(DEFAULT_DIMENSION, 0, Integer.MAX_VALUE);
		add("imageHeight", lImageHeight);

		final MOBoolean lStarsP = new MOBoolean(true);
		add("starsP", lStarsP);
		final MODouble lStarFrequency = new MODouble(0.002, 0, Double.MAX_VALUE);
		add("starFrequency", lStarFrequency);
		final MOInteger lBigStars = new MOInteger(0, 0, 100);
		add("bigStars", lBigStars);

		final MOBoolean lGridP = new MOBoolean(true);
		add("gridP", lGridP);
		final MOInteger lGridDivision = new MOInteger(6, 0, Integer.MAX_VALUE);
		add("gridDivision", lGridDivision);
		final MOInteger lGridPixelDevision = new MOInteger(15, 0, Integer.MAX_VALUE);
		add("gridPixelDivision", lGridPixelDevision);

		final MOInteger lShiftX = new MOInteger(0, 0, Integer.MAX_VALUE);
		add("shiftX", lShiftX);
		final MOInteger lShiftY = new MOInteger(0, 0, Integer.MAX_VALUE);
		add("shiftY", lShiftY);
		final MODouble lViewMagnification = new MODouble(1.0, 0, Double.MAX_VALUE);
		add("viewMagnification", lViewMagnification);

		final MOBoolean lShadeP = new MOBoolean(true);
		add("shadeP", lShadeP);
		final MOInteger lDaySideBrightness = new MOInteger(100, 0, 100);
		add("daySideBrightness", lDaySideBrightness);
		final MOInteger lNightSideBrightness = new MOInteger(5, 0, 100);
		add("nightSideBrightness", lNightSideBrightness);
		final MOInteger lTerminatorDiscontinuity = new MOInteger(1, 0, 100);
		add("terminatorDiscontinuity", lTerminatorDiscontinuity);

		final MODouble lViewRotation = new MODouble(0, 0, Double.MAX_VALUE);
		add("viewRotation", lViewRotation);
	}

	public Coordinate getViewPos() {
		return new Coordinate(getDouble("viewPosLat"), getDouble("viewPosLong"));
	}

	public void setViewPos(Coordinate pViewPos) {
		setDouble("viewPosLat", pViewPos.getLat());
		setDouble("viewPosLong", pViewPos.getLong());
	}

	public Coordinate getSunPos() {
		return new Coordinate(getDouble("sunPosLat"), getDouble("sunPosLong"));
	}
	
	public void setSunPos(Coordinate pSunPos) {
		setDouble("sunPosLat", pSunPos.getLat());
		setDouble("sunPosLong", pSunPos.getLong());
	}

	public Coordinate getSunPosRel() {
		return new Coordinate(getDouble("sunPosRelLat"), getDouble("sunPosRelLong"));
	}

	public void setSunPosRel(Coordinate pSunPosRel) {
		setDouble("sunPosRelLat", pSunPosRel.getLat());
		setDouble("sunPosRelLong", pSunPosRel.getLong());
	}
}
