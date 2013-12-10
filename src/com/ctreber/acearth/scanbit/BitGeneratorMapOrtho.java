package com.ctreber.acearth.scanbit;

import java.util.Comparator;

import com.ctreber.acearth.projection.Projection;
import com.ctreber.acearth.util.EdgeCrossing;
import com.ctreber.acearth.util.Point2D;
import com.ctreber.acearth.util.Point3D;

/**
 * <p>Map scanner for orthographic projection.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class BitGeneratorMapOrtho extends BitGeneratorMap
{
  public BitGeneratorMapOrtho(Projection pProjection)
  {
    super(pProjection);
  }

  protected Comparator getEdgeXingComparator()
  {
    return new EdgeCrossingComparator();
  }

  protected ScanBuf scanOutline()
  {
    final ScanBuf lScanBuf = new ScanBuf(fImageHeight, fImageWidth);
    addArcToScanbuf(lScanBuf, 1.0, 0.0, 0.0, 1.0, 0.0, 2 * Math.PI);

    return lScanBuf;
  }

  private void addArcToScanbuf(ScanBuf pScanBuf, double pXFrom, double pYFrom,
    double pAngleFrom, double pXTo, double pYTo, double pAngleTo)
  {
    double step = 1 / fProjection.getScale() * 10;
    if(step > 0.05)
    {
      step = 0.05;
    }
    final int lAngleFrom = (int)Math.ceil(pAngleFrom / step);
    final int lAngleTo = (int)Math.floor(pAngleTo / step);

    double prev_x = fProjection.finalizeX(pXFrom);
    double prev_y = fProjection.finalizeY(pYFrom);
    double curr_x;
    double curr_y;
    if(lAngleFrom <= lAngleTo)
    {
      double c_step = Math.cos(step);
      double s_step = Math.sin(step);

      double angle = lAngleFrom * step;
      double arc_x = Math.cos(angle);
      double arc_y = Math.sin(angle);

      for(int i = lAngleFrom; i <= lAngleTo; i++)
      {
        curr_x = fProjection.finalizeX(arc_x);
        curr_y = fProjection.finalizeY(arc_y);
        pScanBuf.addLine(prev_x, prev_y, curr_x, curr_y);

        /* instead of repeatedly calling cos() and sin() to get the next
         * values for arc_x and arc_y, simply rotate the existing values
         */
        double tmp = (c_step * arc_x) - (s_step * arc_y);
        arc_y = (s_step * arc_x) + (c_step * arc_y);
        arc_x = tmp;

        prev_x = curr_x;
        prev_y = curr_y;
      }
    }

    curr_x = fProjection.finalizeX(pXTo);
    curr_y = fProjection.finalizeY(pYTo);
    pScanBuf.addLine(prev_x, prev_y, curr_x, curr_y);
  }

  protected void scanPolygon(ScanBuf pScanBuf,
    Point3D[] pPoints3D, Point2D[] pPoints2D, int pIndex)
  {
    Point3D extra;

    Point3D lCurr = pPoints3D[pIndex];
    final int lIndexPrev = pIndex - 1 >= 0 ? pIndex - 1 : pPoints2D.length - 1;
    Point3D lPrev = pPoints3D[lIndexPrev];

    if(lPrev.getZ() <= 0)
    {
      if(lCurr.getZ() <= 0)
      {
        return;
      }

      // Previous point not visible, but current one is: horizon crossed.
      extra = findEdgeCrossing(lPrev, lCurr);
      addEdgeXing(new EdgeCrossing(EdgeCrossing.XingTypeEntry, pIndex,
        extra.getX(), extra.getY(), Math.atan2(extra.getY(), extra.getX())));
      lPrev = extra;
    } else
    {
      if(lCurr.getZ() <= 0)
      {
        // Previous point visible, but current is not: horizon crossed.
        extra = findEdgeCrossing(lPrev, lCurr);
        addEdgeXing(new EdgeCrossing(EdgeCrossing.XingTypeExit, pIndex,
          extra.getX(), extra.getY(), Math.atan2(extra.getY(), extra.getX())));
        lCurr = extra;
      }
    }

    pScanBuf.addLine(
      fProjection.finalizeX(lPrev.getX()), fProjection.finalizeY(lPrev.getY()),
      fProjection.finalizeX(lCurr.getX()), fProjection.finalizeY(lCurr.getY()));
  }

  private Point3D findEdgeCrossing(Point3D pPrev, Point3D pCurr)
  {
    double tmp = pCurr.getZ() / (pCurr.getZ() - pPrev.getZ());
    final double r0 = pCurr.getX() - tmp * (pCurr.getX() - pPrev.getX());
    final double r1 = pCurr.getY() - tmp * (pCurr.getY() - pPrev.getY());

    tmp = Math.sqrt((r0 * r0) + (r1 * r1));

    return new Point3D(r0 / tmp, r1 / tmp, 0);
  }

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
      from = xings[xings.length - 1];
      to = xings[0];
      addArcToScanbuf(pScanBuf, from.getX(), from.getY(), from.getAngle(),
        to.getX(), to.getY(), to.getAngle() + 2 * Math.PI);
      lStart = 1;
    }

    for(int i = lStart; i < xings.length - 1; i += 2)
    {
      from = xings[i];
      to = xings[i + 1];
      addArcToScanbuf(pScanBuf, from.getX(), from.getY(), from.getAngle(),
        to.getX(), to.getY(), to.getAngle());
    }
  }

  private static class EdgeCrossingComparator implements Comparator
  {
    public int compare(Object o1, Object o2)
    {
      final EdgeCrossing a = (EdgeCrossing)o1;
      final EdgeCrossing b = (EdgeCrossing)o2;

      return (a.getAngle() < b.getAngle()) ? -1 : (a.getAngle() > b.getAngle()) ? 1 : 0;
    }
  }
}
