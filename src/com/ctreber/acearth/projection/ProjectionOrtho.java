package com.ctreber.acearth.projection;

import com.ctreber.acearth.util.Coordinate;
import com.ctreber.acearth.util.Point3D;


/**
 * <p>Orthographic projection (show Earth as a ball).
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class ProjectionOrtho extends Projection
{
  /**
   * <p>All of Earth is visible.
   *
   * @param pPoint
   * @return
   */
  public boolean isVisible(Point3D pPoint)
  {
    return pPoint.getZ() >= 0;
  }

  public Coordinate getLocation(int pX, int pY)
  {
    final double lX = inverseFinalizeX(pX);
    final double lY = inverseFinalizeY(pY);
    final double lZ = Math.sqrt(1 - lX * lX - lY * lY);
    final Point3D lP = new Point3D(lX, lY, lZ);

    return rotateReverse(lP).getCoordinate();
  }

  /**
   * @return Longitude, not in rad but from -1 to 1.
   */
  public double projectX(double pX, double pZ)
  {
    return pX;
  }

  public double inverseProjectX(double pX)
  {
    return pX;
  }

  /**
   * @return Latitude, not in rad but from -1 to 1.
   */
  public double projectY(double pY)
  {
    return pY;
  }

  public double inverseProjectY(double pY)
  {
    return pY;
  }

  /**
   * <p>The scale is not from -PI to PI but from -1 to 1 in this case
   * (the range of x, y, z of the points).
   */
  protected void setScale()
  {
    fScale = Math.min(fImageHeight, fImageWidth) * fViewMagnification * 0.99 / 2;
  }
}
