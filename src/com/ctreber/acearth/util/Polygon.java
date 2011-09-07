package com.ctreber.acearth.util;


/**
 * <p>A polygon in a 3 axis space.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class Polygon
{
  public static final int LAND = 1;
  public static final int WATER = -1;

  private int fType;
  private Point3D[] fPoints;

  public Polygon(int pType, Point3D[] pPoints)
  {
    fType = pType;
    fPoints = pPoints;
  }

  public int getType()
  {
    return fType;
  }

  public Point3D[] getPoints()
  {
    return fPoints;
  }

  public Point3D getPoint(int pIndex)
  {
    return fPoints[pIndex];
  }

  public int getSize()
  {
    return fPoints.length;
  }

  public String toString()
  {
    return "Type " + fType + ", " + fPoints.length + " points";
  }
}
