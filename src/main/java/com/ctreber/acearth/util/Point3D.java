package com.ctreber.acearth.util;

/**
 * <p>A point in a 2 axis space.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class Point3D
{
  private double fX;
  private double fY;
  private double fZ;

  public Point3D(double pX, double pY, double pZ)
  {
    fX = pX;
    fY = pY;
    fZ = pZ;
  }

  public double getX()
  {
    return fX;
  }

  public double getY()
  {
    return fY;
  }

  public double getZ()
  {
    return fZ;
  }

  public String toString()
  {
    return "x: " + fX + ", y: " + fY + ", z: " + fZ;
  }

  public Coordinate getCoordinate()
  {
    return new Coordinate(Toolkit.radsToDegs(Math.asin(fY)),
      Toolkit.radsToDegs(Math.atan2(fX, fZ)));
  }
}
