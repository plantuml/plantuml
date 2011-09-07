package com.ctreber.acearth.scanbit;

/**
 * <p>Instruction to paint points xFrom to xTo on line y.
 *
 * <p>What I don't understand: why do values get summed to determine the
 * pixel type?
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class ScanBit implements Comparable
{
  private int fY;
  private int lXFrom;
  private int fXTo;
  private int fType;

  public ScanBit(int pY, int pLoX, int pHiX, int pType)
  {
    fY = pY;
    lXFrom = pLoX;
    fXTo = pHiX;
    fType = pType;
  }

  public int compareTo(Object o)
  {
    if(o instanceof ScanBit)
    {
      ScanBit lOther = (ScanBit)o;
      return (fY > lOther.fY) ? 1 : (fY < lOther.fY) ? -1 : 0;
    }

    throw new IllegalArgumentException("Can't compare with " + o.getClass());
  }

  public int getY()
  {
    return fY;
  }

  public int getlXFrom()
  {
    return lXFrom;
  }

  public int getXTo()
  {
    return fXTo;
  }

  /**
   * <p>See values for
   * @see com.ctreber.acearth.util.Polygon
   */
  public int getType()
  {
    return fType;
  }
}
