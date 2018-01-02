package com.ctreber.acearth.util;

/**
 * <p>A point in a 2 axis space.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class Point2D
{
  private double fX;
  private double fY;

  public Point2D(double pX, double pY)
  {
    fX = pX;
    fY = pY;
  }

  public double getX()
  {
    return fX;
  }

  public double getY()
  {
    return fY;
  }

  public String toString()
  {
    return "x: " + fX + ", y: " + fY;
  }
}
