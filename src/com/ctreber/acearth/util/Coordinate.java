package com.ctreber.acearth.util;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>
 * Latitude and longitude coordinate. Can be used as declination and right
 * ascension as well.
 * 
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public class Coordinate {
	/*
	 * MeanObliquity gives the mean obliquity of the earth's axis at epoch
	 * 1990.0 (computed as 23.440592 degrees according to the method given in
	 * duffett-smith, section 27)
	 */
	private static final double MEAN_OBLIQUITY = 23.440592 * Toolkit.TWOPI / 360;

	// Or DE
	private double fLat;
	// Or RA
	private double fLong;

	public Coordinate() {
	}

	/**
	 * <p>
	 * Construct a location specfied by two angles. Your choice if in degrees or
	 * rads, but keep track!
	 * 
	 * @param pLong
	 *            Longitude or RA
	 * @param pLat
	 *            Latitude or DE
	 */
	public Coordinate(double pLat, double pLong) {
		fLat = pLat;
		fLong = pLong;
	}

	public void renderAsXML(Writer writer) throws IOException {
		writer.write("<Coordinate>\n");
		writer.write("  <latitude>" + fLat + "</latitude>\n");
		writer.write("  <longitude>" + fLong + "</longitude>\n");
		writer.write("</Coordinate>\n");
	}

	public Point3D getPoint3D() {
		final double lLatRad = Toolkit.degsToRads(fLat);
		final double lLongRad = Toolkit.degsToRads(fLong);

		final double lX = Math.cos(lLatRad) * Math.sin(lLongRad);
		final double lY = Math.sin(lLatRad);
		final double lZ = Math.cos(lLatRad) * Math.cos(lLongRad);

		return new Point3D(lX, lY, lZ);
	}

	/**
	 * <p>
	 * Assumes coordinate is not in degrees but rads.
	 * 
	 * @return
	 */
	public Point3D getPoint3DRads() {
		final double lX = Math.cos(fLat) * Math.sin(fLong);
		final double lY = Math.sin(fLat);
		final double lZ = Math.cos(fLat) * Math.cos(fLong);

		return new Point3D(lX, lY, lZ);
	}

	/**
	 * <p>
	 * Convert from ecliptic to equatorial coordinates (after duffett-smith,
	 * section 27)
	 */
	public Coordinate eclipticToEquatorial() {
		final double sin_e = Math.sin(MEAN_OBLIQUITY);
		final double cos_e = Math.cos(MEAN_OBLIQUITY);

		final double lRA = Math.atan2(Math.sin(fLong) * cos_e - Math.tan(fLat) * sin_e, Math.cos(fLong));
		final double lDE = Math.asin(Math.sin(fLat) * cos_e + Math.cos(fLat) * sin_e * Math.sin(fLong));

		return new Coordinate(lDE, lRA);
	}

	/**
	 * <p>
	 * Add position to this position, make sure coordinates are valid.
	 */
	public void add(Coordinate lOther) {
		fLat += lOther.fLat;
		fLong += lOther.fLong;
		wrap();
	}

	/**
	 * <p>
	 * Warp coordinates exceeding valid values. Happens when latitudes and
	 * longitudes are added or substracted.
	 */
	public void wrap() {
		if (fLat > 90) {
			fLat = 180 - fLat;
			fLong += 180;
		} else if (fLat < -90) {
			fLat = -180 - fLat;
			fLong += 180;
		}

		if (fLong > 180) {
			do {
				fLong -= 360;
			} while (fLong > 180);
		} else if (fLong < -180) {
			do {
				fLong += 360;
			} while (fLong < -180);
		}
	}

	public double getLat() {
		return fLat;
	}

	public double getDE() {
		return fLat;
	}

	public double getLong() {
		return fLong;
	}

	public double getRA() {
		return fLong;
	}

	public boolean check() {
		return (-90 <= fLat) && (fLat <= 90) && (-180 <= fLong) && (fLong <= 180);
	}

	public String toString() {
		return "lat: " + fLat + ", long: " + fLong;
	}
}
