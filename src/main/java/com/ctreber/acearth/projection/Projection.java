package com.ctreber.acearth.projection;

import com.ctreber.acearth.util.Coordinate;
import com.ctreber.acearth.util.Point2D;
import com.ctreber.acearth.util.Point3D;
import com.ctreber.acearth.util.Toolkit;

/**
 * <p>A projection for a globe on a flat surface (must be subclassed).
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
abstract public class Projection
{
  // Target information
  int fImageHeight;
  int fImageWidth;
  private double fXOffset;
  private double fYOffset;

  //Viewing information
  private int fShiftX;
  private int fShiftY;
  double fScale;

  private Coordinate fViewPos;
  /** <p>In rads */
  private double fViewRotation;
  double fViewMagnification;


  //Transformation matrix parameters */
  private double fCosLat;
  private double fSinLat;
  private double fCosLon;
  private double fSinLon;
  private double fCosRot;
  private double fSinRot;

  /**
   * <p>Initialize transform parameters, set offset to center of image
   * (plus shifts), set scale
   */
  public void initTransformTable()
  {
    // Set transformation parameters
    fCosLat = Math.cos(Toolkit.degsToRads(fViewPos.getLat()));
    fSinLat = Math.sin(Toolkit.degsToRads(fViewPos.getLat()));
    fCosLon = Math.cos(Toolkit.degsToRads(fViewPos.getLong()));
    fSinLon = Math.sin(Toolkit.degsToRads(fViewPos.getLong()));
    fCosRot = Math.cos(Toolkit.degsToRads(fViewRotation));
    fSinRot = Math.sin(Toolkit.degsToRads(fViewRotation));

    fXOffset = (double)fImageWidth / 2 + fShiftX;
    fYOffset = (double)fImageHeight / 2 + fShiftY;

    setScale();
  }

  abstract protected void setScale();

  /**
   * <p>Project 3D point on y axis.
   */
  abstract public double projectY(double pY);

  abstract public double inverseProjectY(double pY);

  /**
   * <p>Project 3D point on x axis.
   */
  abstract protected double projectX(double pX, double pZ);

  abstract public double inverseProjectX(double pX);

  public abstract boolean isVisible(Point3D pPoint);

  public boolean isWithinImage(Point2D pPoint)
  {
    return (pPoint.getX() >= 0) && (pPoint.getX() < fImageWidth) &&
      (pPoint.getY() >= 0) && (pPoint.getY() < fImageHeight);
  }

  /**
   * <p>Translate screen point into coordinate on Earth.
   *
   * @param pX
   * @param pY
   * @return
   */
  abstract public Coordinate getLocation(int pX, int pY);

  /**
   * <p>Imagine view the globe, N is up, S is down, and N0E0 is in the center.
   * x is right/left, y is up/down, and z is front/rear.
   *
   * <p>Map points are located on the surface of a unit sphere (diameter = 1).
   * Latitude is the angle between x and y or z and y. Longitude is the angle
   * between x and z.
   *
   * <p>Why? The way we choose our global coordinate system, longitude circles
   * (latidude variable) always have the same size while the size of
   * latidude circles (longitude variable) depends on the latitude.
   *
   * @param pPoint
   * @return
   */
  public Point2D project2D(Point3D pPoint)
  {
    return new Point2D(projectX(pPoint.getX(), pPoint.getZ()),
      projectY(pPoint.getY()));
  }

  public Point2D finalize(Point2D pPoint)
  {
    return new Point2D(finalizeX(pPoint.getX()), finalizeY(pPoint.getY()));
  }

  /**
   * <p>Since the final mapping is relative to the center of the image
   * -PI and PI get mapped to the left and right border respectively.
   * But see ProjectionOrtho.setScale().
   */
  public double finalizeX(double pX)
  {
    return fXOffset + fScale * pX;
  }

  /**
   * <p>Since the final mapping is relative to the center of the image
   * -PI and PI get mapped to the bottom and top border respectively.
   * But see ProjectionOrtho.setScale().
   */
  public double finalizeY(double pY)
  {
    return fYOffset - fScale * pY;
  }

  /**
   * <p>Transform screen to image coordinates.
   */
  public double inverseFinalizeX(double x)
  {
    return (x - fXOffset) / fScale;
  }

  /**
   * <p>Transform screen to image coordinates.
   */
  public double inverseFinalizeY(double y)
  {
    return (fYOffset - y) / fScale;
  }

  /**
   * <p>Rotate the point according to the current rotation of Earth.
   */
  public Point3D rotate(Point3D pPoint)
  {
    double lX = pPoint.getX();
    double lY = pPoint.getY();
    double lZ = pPoint.getZ();

    // Do NOT inline vars - it does not work (just inline _t_ for a try).
    double _c_ = fCosLon;
    double _s_ = fSinLon;
    double _t_ = _c_ * lX - _s_ * lZ;
    lZ = _s_ * lX + _c_ * lZ;
    lX = _t_;

    _c_ = fCosLat;
    _s_ = fSinLat;
    _t_ = (_c_ * lY) - (_s_ * lZ);
    lZ = (_s_ * lY) + (_c_ * lZ);
    lY = _t_;

    _c_ = fCosRot;
    _s_ = fSinRot;
    _t_ = (_c_ * lX) - (_s_ * lY);
    lY = (_s_ * lX) + (_c_ * lY);
    lX = _t_;

    return new Point3D(lX, lY, lZ);
  }

  public Point3D rotateReverse(Point3D pPoint)
  {
    // Set transformation parameters
    final double fCosLat = Math.cos(Toolkit.degsToRads(-fViewPos.getLat()));
    final double fSinLat = Math.sin(Toolkit.degsToRads(-fViewPos.getLat()));
    final double fCosLon = Math.cos(Toolkit.degsToRads(-fViewPos.getLong()));
    final double fSinLon = Math.sin(Toolkit.degsToRads(-fViewPos.getLong()));
    final double fCosRot = Math.cos(Toolkit.degsToRads(-fViewRotation));
    final double fSinRot = Math.sin(Toolkit.degsToRads(-fViewRotation));

    double lX = pPoint.getX();
    double lY = pPoint.getY();
    double lZ = pPoint.getZ();

    // Do NOT inline vars - it does not work (just inline lTmp for a try).
    double lCosFac;
    double lSinFac;
    double lTmp;

    // Note that the order of the three rotation had to be reversed as well.
    lCosFac = fCosRot;
    lSinFac = fSinRot;
    lTmp = (lCosFac * lX) - (lSinFac * lY);
    lY = (lSinFac * lX) + (lCosFac * lY);
    lX = lTmp;

    lCosFac = fCosLat;
    lSinFac = fSinLat;
    lTmp = (lCosFac * lY) - (lSinFac * lZ);
    lZ = (lSinFac * lY) + (lCosFac * lZ);
    lY = lTmp;

    lCosFac = fCosLon;
    lSinFac = fSinLon;
    lTmp = (lCosFac * lX) - (lSinFac * lZ);
    lZ = (lSinFac * lX) + (lCosFac * lZ);
    lX = lTmp;

    return new Point3D(lX, lY, lZ);
  }

  public double getScale()
  {
    return fScale;
  }

  public Coordinate getViewPos()
  {
    return fViewPos;
  }

  public void setViewMagnification(double pViewMagnification)
  {
    fViewMagnification = pViewMagnification;
    setScale();
  }

  public void setViewPos(Coordinate pViewPos)
  {
    fViewPos = pViewPos;
  }

  public void setShiftX(int pX)
  {
    fShiftX = pX;
  }

  public void setShiftY(int pY)
  {
    fShiftY = pY;
  }

  public void setViewRotation(double pViewRotation)
  {
    fViewRotation = pViewRotation;
  }

  public void setImageWidth(int pImageWidth)
  {
    fImageWidth = pImageWidth;
  }

  public void setImageHeight(int pImageHeight)
  {
    fImageHeight = pImageHeight;
  }
}
