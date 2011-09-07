package com.ctreber.acearth.util;

/**
 * <p>Holds information about a line crossing "the edge of Earth".
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class EdgeCrossing
{
  public static final int XingTypeEntry = 0;
  public static final int XingTypeExit = 1;

  private int fType;
  private int fIndex;
  private double fX;
  private double fY;
  private double fAngle;

  public EdgeCrossing(int pType, int pIndex, double pX, double pY, double pAngle)
  {
    fType = pType;
    fX = pX;
    fY = pY;
    fAngle = pAngle;
    fIndex = pIndex;
  }

  public String toString()
  {
    return fType + ": " + fX + ", " + fY + ", " + fAngle + " (" + fIndex + ")";
  }

  public int getType()
  {
    return fType;
  }

  public double getX()
  {
    return fX;
  }

  public double getY()
  {
    return fY;
  }

  public double getAngle()
  {
    return fAngle;
  }

  public int getIndex()
  {
    return fIndex;
  }
}
