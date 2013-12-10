package com.ctreber.acearth.scandot;

import com.ctreber.acearth.projection.Projection;
import com.ctreber.acearth.util.Coordinate;
import com.ctreber.acearth.util.Point2D;
import com.ctreber.acearth.util.Point3D;

/**
 * <p>Generate latitude and longitude grid as dots.
 *
 * <p>Refactored 08.11.2002
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class DotGeneratorLines extends ScanDotGenerator
{
  private Projection fProjection;
  private int fLineDivider;
  private int fPointDivider;
  private static final double PI = Math.PI;
  private static final double TWOPI = 2 * PI;
  private static final double HALFPI = PI / 2;

  public DotGeneratorLines(Projection pProjection,
    int pLineDevider, int pPointDivider)
  {
    fProjection = pProjection;
    fLineDivider = pLineDevider;
    fPointDivider = pPointDivider;
  }

  /**
   * <p>Paint grid.
   */
  public void generateScanDots()
  {
    double lLonStep = TWOPI / (fLineDivider * 4);
    double lLatStep = PI / (fLineDivider * 2 * fPointDivider);
    for(double lLon = -PI; lLon <= PI; lLon += lLonStep)
    {
      for(double lLat = -HALFPI; lLat <= HALFPI; lLat += lLatStep)
      {
        transformAndAddDot(new Coordinate(lLat, lLon));
      }
    }

    lLatStep = TWOPI / (fLineDivider * 4);
    lLonStep = PI / (fLineDivider * 2 * fPointDivider);
    for(double lLat = -HALFPI; lLat <= HALFPI; lLat += lLatStep)
    {
      for(double lLon = -PI; lLon <= PI; lLon += lLonStep)
      {
        transformAndAddDot(new Coordinate(lLat, lLon));
      }
    }
  }

  private void transformAndAddDot(Coordinate pPos)
  {
    final Point3D lPointRotated = fProjection.rotate(pPos.getPoint3DRads());
    if(fProjection.isVisible(lPointRotated))
    {
      Point2D lPoint = fProjection.finalize(fProjection.project2D(lPointRotated));
      if(fProjection.isWithinImage(lPoint))
      {
        fDots.add(new ScanDot(ScanDot.DotTypeGrid, lPoint));
      }
    }
  }
}
