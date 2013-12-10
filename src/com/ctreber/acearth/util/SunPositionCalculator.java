package com.ctreber.acearth.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * <p>Calculates the position of the point on Earth which is directly
 * below the sun or the moon.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class SunPositionCalculator
{
  /*
    * the epoch upon which these astronomical calculations are based is
    * 1990 january 0.0, 631065600 seconds since the beginning of the
    * "unix epoch" (00:00:00 GMT, Jan. 1, 1970)
    *
    * given a number of seconds since the start of the unix epoch,
    * daysSinceEpoch() computes the number of days since the start of the
    * astronomical epoch (1990 january 0.0)
    */

  private static final long EPOCH_START = 631065600000l;

  /*
  * assuming the apparent orbit of the sun about the earth is circular,
  * the rate at which the orbit progresses is given by RadsPerDay --
  * TWOPI radians per orbit divided by 365.242191 days per year:
  */

  private static final double RADS_PER_DAY = Toolkit.TWOPI / 365.242191;

  /*
  * details of sun's apparent orbit at epoch 1990.0 (after
  * duffett-smith, table 6, section 46)
  *
  * Epsilon_g    (ecliptic longitude at epoch 1990.0) 279.403303 degrees
  * OmegaBar_g   (ecliptic longitude of perigee)      282.768422 degrees
  * Eccentricity (eccentricity of orbit)                0.016713
  */

  private static final double EPSILON_G = Toolkit.degsToRads(279.403303);
  private static final double OMEGA_BAR_G = Toolkit.degsToRads(282.768422);
  private static final double ECCENTRICITY = 0.016713;

  /*
  * Lunar parameters, epoch January 0, 1990.0
  */
  private static final double MOON_MEAN_LONGITUDE = Toolkit.degsToRads(318.351648);
  private static final double MOON_MEAN_LONGITUDE_PERIGEE = Toolkit.degsToRads(36.340410);
  private static final double MOON_MEAN_LONGITUDE_NODE = Toolkit.degsToRads(318.510107);
  private static final double MOON_INCLINATION = Toolkit.degsToRads(5.145396);

  private static final double SIDERAL_MONTH = 27.3217;

  /**
   * <p>Calculate the position of the mean sun: where the sun would
   * be if the earth's orbit were circular instead of ellipictal.
   *
   * <p>Verified.
   *
   * @param pDays days since ephemeris epoch
   */
  private static double getMeanSunLongitude(double pDays)
  {
    double N, M;

    N = RADS_PER_DAY * pDays;
    N = Toolkit.fmod(N, 0, Toolkit.TWOPI);
    if(N < 0)
    {
      N += Toolkit.TWOPI;
    }

    M = N + EPSILON_G - OMEGA_BAR_G;
    if(M < 0)
    {
      M += Toolkit.TWOPI;
    }

    return M;
  }

  /**
   * <p>Compute ecliptic longitude of sun (in radians)
   * (after duffett-smith, section 47)
   *
   * <p>Verified.
   *
   * @param pMillis Milliseconds since unix epoch
   */
  private static double getSunEclipticLongitude(long pMillis)
  {
    final double lDays = daysSinceEpoch(pMillis);
    final double M_sun = getMeanSunLongitude(lDays);

    final double E = doKepler(M_sun);
    final double v = 2 * Math.atan(Math.sqrt((1 + ECCENTRICITY) / (1 - ECCENTRICITY)) * Math.tan(E / 2));

    return (v + OMEGA_BAR_G);
  }

  static double daysSinceEpoch(long pMillis)
  {
    return (double)(pMillis - EPOCH_START) / 24 / 3600 / 1000;
  }

  /**
   * solve Kepler's equation via Newton's method
   * (after duffett-smith, section 47)
   *
   * <p>Verified.
   */
  private static double doKepler(double M)
  {
    double E;
    double lDelta;

    E = M;
    while(true)
    {
      lDelta = E - ECCENTRICITY * Math.sin(E) - M;
      if(Math.abs(lDelta) <= 1e-10)
      {
        break;
      }
      E -= lDelta / (1 - ECCENTRICITY * Math.cos(E));
    }

    return E;
  }


  /**
   * <p>computing julian dates (assuming gregorian calendar, thus this is
   * only valid for dates of 1582 oct 15 or later)
   * (after duffett-smith, section 4)
   *
   * <p>Verified.
   *
   * @param pYear year (e.g. 19xx)
   * @param pMonth month (jan=1, feb=2, ...)
   * @param pDay day of month
   */
  private static double getJulianDate(int pYear, int pMonth, int pDay)
  {
    if((pMonth == 1) || (pMonth == 2))
    {
      pYear -= 1;
      pMonth += 12;
    }

    final int A = pYear / 100;
    final int B = 2 - A + (A / 4);
    final int C = (int)(365.25 * pYear);
    final int D = (int)(30.6001 * (pMonth + 1));

    return B + C + D + pDay + 1720994.5;
  }


  /**
   * <p>compute greenwich mean sidereal time (getGST) corresponding to a given
   * number of milliseconds since the unix epoch
   * (after duffett-smith, section 12)
   *
   * <p>Verified.
   */
  private static double getGST(long pMillis)
  {
    final Calendar lCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    lCal.setTime(new Date(pMillis));

    final double lJulianDate = getJulianDate(lCal.get(Calendar.YEAR), lCal.get(Calendar.MONTH) + 1,
      lCal.get(Calendar.DAY_OF_MONTH));
    final double T = (lJulianDate - 2451545) / 36525;
    double T0 = ((T + 2.5862e-5) * T + 2400.051336) * T + 6.697374558;

    T0 = Toolkit.fmod(T0, 0, 24.0);
    if(T0 < 0)
    {
      T0 += 24;
    }

    final double UT = lCal.get(Calendar.HOUR_OF_DAY) +
      (lCal.get(Calendar.MINUTE) + lCal.get(Calendar.SECOND) / 60.0) / 60.0;

    T0 += UT * 1.002737909;
    T0 = Toolkit.fmod(T0, 0, 24.0);
    if(T0 < 0)
    {
      T0 += 24;
    }

    return T0;
  }

  /**
   * <p>Given a particular time (expressed in milliseconds since the unix
   * epoch), compute position on the earth (lat, lon) such that sun is
   * directly overhead.
   *
   * <p>Verified.
   *
   * @param pMillis seconds since unix epoch
   *
   */
  public static Coordinate getSunPositionOnEarth(long pMillis)
  {
    final Coordinate lSunPosEc = new Coordinate(0.0, getSunEclipticLongitude(pMillis));
    final Coordinate lSunPosEq = lSunPosEc.eclipticToEquatorial();

    final double lRA = Toolkit.limitRads(lSunPosEq.getRA() - (Toolkit.TWOPI / 24) * getGST(pMillis));

    return new Coordinate(Toolkit.radsToDegs(lSunPosEq.getDE()), Toolkit.radsToDegs(lRA));
  }

  /**
   * <p>Given a particular time (expressed in milliseconds since the unix
   * epoch), compute position on the earth (lat, lon) such that the
   * moon is directly overhead.
   *
   * Based on duffett-smith **2nd ed** section 61; combines some steps
   * into single expressions to reduce the number of extra variables.
   *
   * <p>Verified.
   */
  public static Coordinate getMoonPositionOnEarth(long pMillis)
  {
    final double lDays = daysSinceEpoch(pMillis);
    double lSunLongEc = getSunEclipticLongitude(pMillis);
    final double Ms = getMeanSunLongitude(lDays);

    double L = Toolkit.limitRads(Toolkit.fmod(lDays / SIDERAL_MONTH, 0, 1.0) * Toolkit.TWOPI + MOON_MEAN_LONGITUDE);
    double Mm = Toolkit.limitRads(L - Toolkit.degsToRads(0.1114041 * lDays) - MOON_MEAN_LONGITUDE_PERIGEE);
    double N = Toolkit.limitRads(MOON_MEAN_LONGITUDE_NODE - Toolkit.degsToRads(0.0529539 * lDays));
    final double Ev = Toolkit.degsToRads(1.2739) * Math.sin(2.0 * (L - lSunLongEc) - Mm);
    final double Ae = Toolkit.degsToRads(0.1858) * Math.sin(Ms);
    Mm += Ev - Ae - Toolkit.degsToRads(0.37) * Math.sin(Ms);
    final double Ec = Toolkit.degsToRads(6.2886) * Math.sin(Mm);
    L += Ev + Ec - Ae + Toolkit.degsToRads(0.214) * Math.sin(2.0 * Mm);
    L += Toolkit.degsToRads(0.6583) * Math.sin(2.0 * (L - lSunLongEc));
    N -= Toolkit.degsToRads(0.16) * Math.sin(Ms);

    L -= N;
    lSunLongEc = Toolkit.limitRads((Math.abs(Math.cos(L)) < 1e-12) ?
      (N + Math.sin(L) * Math.cos(MOON_INCLINATION) * Math.PI / 2) :
      (N + Math.atan2(Math.sin(L) * Math.cos(MOON_INCLINATION), Math.cos(L))));
    final double lSunLatEc = Math.asin(Math.sin(L) * Math.sin(MOON_INCLINATION));

    final Coordinate lSunPosEq = new Coordinate(lSunLatEc, lSunLongEc).eclipticToEquatorial();
    final double lRA = Toolkit.limitRads(lSunPosEq.getRA() - (Toolkit.TWOPI / 24) * getGST(pMillis));

    return new Coordinate(Toolkit.radsToDegs(lSunPosEq.getDE()), Toolkit.radsToDegs(lRA));
  }
}
