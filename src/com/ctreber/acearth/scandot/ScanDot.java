package com.ctreber.acearth.scandot;

import com.ctreber.acearth.util.Point2D;

/**
 * <p>A single scandot (opposed to a Polygon).
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class ScanDot implements Comparable
{
  // types of dots
  public static final int DotTypeStar = 0;
  public static final int DotTypeGrid = 1;

  private int fX;
  private int fY;
  private int fType;

  public ScanDot(int pType, int pX, int pY)
  {
    fType = pType;
    fX = pX;
    fY = pY;
  }

  public ScanDot(int pType, Point2D pPoint)
  {
    fType = pType;
    fX = (int)pPoint.getX();
    fY = (int)pPoint.getY();
  }

  public int compareTo(Object o)
  {
    if(o instanceof ScanDot)
    {
      ScanDot lOther = (ScanDot)o;

      return fY > lOther.fY ? 1 : (fY < lOther.fY ? -1 : 0);
    }

    throw new IllegalArgumentException("Can't compare to " + o.getClass());
  }

  public int getType()
  {
    return fType;
  }

  public int getX()
  {
    return fX;
  }

  public int getY()
  {
    return fY;
  }

  public String toString()
  {
    return fX + ", " + fY + ": " + fType;
  }
}
