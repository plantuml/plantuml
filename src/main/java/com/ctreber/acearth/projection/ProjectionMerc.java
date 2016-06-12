package com.ctreber.acearth.projection;

import com.ctreber.acearth.util.Coordinate;
import com.ctreber.acearth.util.Point3D;


/**
 * <p>Mercator projection. Show Earth flatly spread out on rectangle.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class ProjectionMerc extends Projection
{
  /**
   * <p>All of Earth is visible.
   *
   * @param pPoint
   * @return
   */
  public boolean isVisible(Point3D pPoint)
  {
    return true;
  }

  public Coordinate getLocation(int pX, int pY)
  {
    final Coordinate lRaw = new Coordinate(
      2 * (Math.atan(Math.exp(inverseFinalizeY(pY))) - Math.PI / 4),
      inverseFinalizeX(pX));

    return rotateReverse(lRaw.getPoint3DRads()).getCoordinate();
  }

  /**
   * <p>The scale is set so that a value of
   * 2PI gets mapped to the full image width times the magnification.
   * But see ProjectionOrtho.setScale().
   */
  protected void setScale()
  {
    // Makes 2PI come out as full image width
    fScale = fViewMagnification * fImageWidth / (2 * Math.PI);
  }

  /**
   * @return Longitude (-PI to PI), linearly on x axis.
   */
  public double projectX(double pX, double pZ)
  {
    return Math.atan2(pX, pZ);
  }

  public double inverseProjectX(double pX)
  {
    return Math.sin(pX);
  }

  /**
   * @return Latitude (-PI/2 to PI/2), projected from center of Earth on
   * y axis with a twist and a log scale.
   */
  public double projectY(double pY)
  {
    return (pY >= 0.9999999999) ? 1e6
      : (pY <= -0.9999999999) ? -1e6
      : Math.log(Math.tan(Math.asin(pY) / 2 + Math.PI / 4));
  }

  public double inverseProjectY(double y)
  {
    return Math.sin(2 * (Math.atan(Math.exp(y)) - Math.PI / 4));
  }
}
