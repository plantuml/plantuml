package com.ctreber.acearth.scanbit;

import java.util.Comparator;

import com.ctreber.acearth.projection.Projection;
import com.ctreber.acearth.util.EdgeCrossing;
import com.ctreber.acearth.util.Point2D;
import com.ctreber.acearth.util.Point3D;

/**
 * <p>Map scanner for mercator and cylindrical projections.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class BitGeneratorMapDefault extends BitGeneratorMap
{
  public BitGeneratorMapDefault(Projection pProjection)
  {
    super(pProjection);
  }

  protected Comparator getEdgeXingComparator()
  {
    return new EdgeXingComparator();
  }

  /**
   * Seems to: walk along outline of projected area.
   */
  protected ScanBuf scanOutline()
  {
    final ScanBuf lScanBuf = new ScanBuf(fImageHeight, fImageWidth);

    final double lLeft = fProjection.finalizeX(-Math.PI);
    final double lRight = fProjection.finalizeX(Math.PI);
    // Will be adjusted to fit height.
    final double lTop = fProjection.finalizeY(1e6);
    final double lBottom = fProjection.finalizeY(-1e6);

    // Top
    lScanBuf.addLine(lRight, lTop, lLeft, lTop);
    // Left
    lScanBuf.addLine(lLeft, lTop, lLeft, lBottom);
    // Bottom
    lScanBuf.addLine(lLeft, lBottom, lRight, lBottom);
    // Right
    lScanBuf.addLine(lRight, lBottom, lRight, lTop);

    return lScanBuf;
  }

  /**
   * <p>Look at 2 neighboring points in polygon and find edge crossings -
   * edge of the globe?! Edge of image?
   */
  protected void scanPolygon(ScanBuf pScanBuf,
    Point3D[] pPoints3D, Point2D[] pPoints2D, int pIndex)
  {
    final Point2D lCurr = pPoints2D[pIndex];
    final int lIndexPrev = pIndex - 1 >= 0 ? pIndex - 1 : pPoints2D.length - 1;
    final Point2D lPrev = pPoints2D[lIndexPrev];
    double dx = lCurr.getX() - lPrev.getX();

    if(Math.abs(dx) <= Math.PI)
    {
      // Perimeter not crossed.
      pScanBuf.addLine(
        fProjection.finalizeX(lPrev.getX()), fProjection.finalizeY(lPrev.getY()),
        fProjection.finalizeX(lCurr.getX()), fProjection.finalizeY(lCurr.getY()));
      return;
    }

    // Perimeter crossed, we need to wrap the line around the edge.
    int lAngle;
    double mx;
    double my = getYMidPoint(pPoints3D[lIndexPrev], pPoints3D[pIndex]);
    if(dx > 0)
    {
      // Curve runs right
      mx = -Math.PI;
      lAngle = 2;
    } else
    {
      mx = Math.PI;
      lAngle = 0;
    }

    // From previous point to edge...
    pScanBuf.addLine(
      fProjection.finalizeX(lPrev.getX()), fProjection.finalizeY(lPrev.getY()),
      fProjection.finalizeX(mx), fProjection.finalizeY(my));
    addEdgeXing(new EdgeCrossing(EdgeCrossing.XingTypeExit, pIndex, mx, my, lAngle));

    if(dx > 0)
    {
      mx = Math.PI;
      lAngle = 0;
    } else
    {
      mx = -Math.PI;
      lAngle = 2;
    }

    // ...and from edge to current point.
    pScanBuf.addLine(
      fProjection.finalizeX(mx), fProjection.finalizeY(my),
      fProjection.finalizeX(lCurr.getX()), fProjection.finalizeY(lCurr.getY()));
    addEdgeXing(new EdgeCrossing(EdgeCrossing.XingTypeEntry, pIndex, mx, my, lAngle));
  }

  /**
   * <p>My educated guess is that the mid point between the current and
   * the previous point is calculated, and - kind of - y of that point
   * is returned.
   */
  private double getYMidPoint(Point3D pPrev, Point3D pCurr)
  {
    double lY;
    final double lZ;

    if(pCurr.getX() != 0)
    {
      // if xPrev is twice xCurr, ratio is 2
      double ratio = (pPrev.getX() / pCurr.getX());
      lY = pPrev.getY() - ratio * pCurr.getY();
      lZ = pPrev.getZ() - ratio * pCurr.getZ();
    } else
    {
      lY = pCurr.getY();
      lZ = pCurr.getZ();
    }

    final double lDistance = Math.sqrt((lY * lY) + (lZ * lZ));
    lY *= ((lZ > 0) ? -1 : 1) / lDistance;

    return fProjection.projectY(lY);
  }

  /**
   * <p>Side effect: Creates ScanBuf lines.
   */
  protected void handleCrossings(ScanBuf pScanBuf, EdgeCrossing[] xings)
  {
    EdgeCrossing from;
    EdgeCrossing to;
    int lStart;

    if(xings[0].getType() == EdgeCrossing.XingTypeExit)
    {
      lStart = 0;
    } else
    {
      // Type "entry".
      from = xings[xings.length - 1];
      to = xings[0];
      addEdgeToScanbuf(pScanBuf, from, to);
      lStart = 1;
    }

    for(int i = lStart; i < xings.length - 1; i += 2)
    {
      from = xings[i];
      to = xings[i + 1];
      addEdgeToScanbuf(pScanBuf, from, to);
    }
  }

  /**
   * <p>For handleCrossing(). Side effect: Creates ScanBuf lines.
   *
   * @param pScanBuf
   * @param from
   * @param to
   */
  private void addEdgeToScanbuf(ScanBuf pScanBuf, EdgeCrossing from,
    EdgeCrossing to)
  {
    int lAngleFrom = (int)from.getAngle();
    double lXFrom = fProjection.finalizeX(from.getX());
    double lYFrom = fProjection.finalizeY(from.getY());

    // Step around in 90 degree increments until target angle is reached
    while(lAngleFrom != (int)to.getAngle())
    {
      int lAngleNew = 0;
      double lXNew = 0;
      double lYNew = 0;

      switch(lAngleFrom)
      {
        case 0:
          // Top right
          lXNew = fProjection.finalizeX(Math.PI);
          lYNew = fProjection.finalizeY(1e6);
          lAngleNew = 1;
          break;

        case 1:
          // Top left
          lXNew = fProjection.finalizeX(-Math.PI);
          lYNew = fProjection.finalizeY(1e6);
          lAngleNew = 2;
          break;

        case 2:
          // Bottom left
          lXNew = fProjection.finalizeX(-Math.PI);
          lYNew = fProjection.finalizeY(-1e6);
          lAngleNew = 3;
          break;

        case 3:
          // Bottom right
          lXNew = fProjection.finalizeX(Math.PI);
          lYNew = fProjection.finalizeY(-1e6);
          lAngleNew = 0;
          break;
      }

      pScanBuf.addLine(lXFrom, lYFrom, lXNew, lYNew);

      lAngleFrom = lAngleNew;
      lXFrom = lXNew;
      lYFrom = lYNew;
    }

    // ...and from last to final.
    pScanBuf.addLine(lXFrom, lYFrom, fProjection.finalizeX(to.getX()),
      fProjection.finalizeY(to.getY()));
  }

  private static class EdgeXingComparator implements Comparator
  {
    public int compare(Object o1, Object o2)
    {
      final EdgeCrossing a = (EdgeCrossing)o1;
      final EdgeCrossing b = (EdgeCrossing)o2;

      if(a.getAngle() < b.getAngle())
      {
        return -1;
      }

      if(a.getAngle() > b.getAngle())
      {
        return 1;
      }

      // Angles are equal.
      if(a.getAngle() == 0)
      {
        return (a.getY() < b.getY()) ? -1 : (a.getY() > b.getY()) ? 1 : 0;
      }

      if(a.getAngle() == 2)
      {
        return (a.getY() > b.getY()) ? -1 : (a.getY() < b.getY()) ? 1 : 0;
      }

      throw new RuntimeException("No result");
    }
  }
}
