package com.ctreber.acearth.shader;

import java.awt.Color;

import com.ctreber.acearth.projection.Projection;
import com.ctreber.acearth.scanbit.BitGeneratorMap;
import com.ctreber.acearth.util.Coordinate;
import com.ctreber.acearth.util.Point3D;

/**
 * <p>A shader computes Colors for a row of pixel types, depending
 * on lighting parameters.</p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com (Nov 11, 2002)</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
abstract public class Shader
{
  private static final Color COLOR_SPACE = Color.black;
  private static final Color COLOR_STAR = Color.white;
  private static final Color COLOR_WATER = Color.blue;
  private static final Color COLOR_LAND = Color.green;
  // Brown
  //static final Color COLOR_LAND = new Color(255, 136, 25);
  private static final Color COLOR_GRID_LAND = Color.white;
  // Bright blue
  private static final Color COLOR_GRID_WATER = new Color(128, 128, 255);

  /** <p>Needed to calculate lighting vectors. */
  Projection fProjection;

  // Stuff below only needed when shading.
  private Coordinate fSunPos;
  private double fNightSideBrightness;
  private double fDaySideBrightness;
  private double fTerminatorDiscontinuity;

  private double fDaySideValueBase;
  private double fDaySideValueRange;
  Point3D fLightVector;

  abstract public Color[] getShadedColors(int pRowNo, int[] pRowTypes);

  public void init()
  {
    // Precompute shading parameters. I personally find the terminator
    // stuff is obscure and might as well be left out.
    final double tmp = fTerminatorDiscontinuity / 100;
    // 100%: day, 0%: night
    fDaySideValueBase = (int)(tmp * fDaySideBrightness +
      (1 - tmp) * fNightSideBrightness);
    fDaySideValueRange = fDaySideBrightness - fDaySideValueBase;
    fLightVector = fProjection.rotate(fSunPos.getPoint3D());
  }

  Color getShadedColorForType(int pType, double pSunValue)
  {
    double lBrightness;

    if(pSunValue < 0)
    {
      // The sun is below the horizon.
      lBrightness = fNightSideBrightness / 100;
    } else
    {
      // The sun is above the horizon. The brightness will range from
      // the base to the maximum value.
      lBrightness = (fDaySideValueBase + pSunValue * fDaySideValueRange) / 100;
    }
    if(lBrightness > 1.0)
    {
      lBrightness = 1.0;
    }

    switch(pType)
    {
      case BitGeneratorMap.PixTypeSpace:
        return COLOR_SPACE;

      case BitGeneratorMap.PixTypeStar:
        return COLOR_STAR;

      case BitGeneratorMap.PixTypeGridLand:
        return shade(COLOR_GRID_LAND, lBrightness);

      case BitGeneratorMap.PixTypeGridWater:
        return shade(COLOR_GRID_WATER, lBrightness);

      case BitGeneratorMap.PixTypeLand:
        return shade(COLOR_LAND, lBrightness);

      case BitGeneratorMap.PixTypeWater:
        return shade(COLOR_WATER, lBrightness);
    }

    return null;
  }

  private static Color shade(Color pColor, double pBrightness)
  {
    return new Color((int)(pColor.getRed() * pBrightness),
      (int)(pColor.getGreen() * pBrightness),
      (int)(pColor.getBlue() * pBrightness));
  }

  public void setProjection(Projection pProjection)
  {
    fProjection = pProjection;
  }

  public void setSunPos(Coordinate pSunPos)
  {
    fSunPos = pSunPos;
  }

  public void setDaySideBrightness(double pDaySideBrightness)
  {
    fDaySideBrightness = pDaySideBrightness;
  }

  public void setNightSideBrightness(double pNightSideBrightness)
  {
    fNightSideBrightness = pNightSideBrightness;
  }

  public void setTerminatorDiscontinuity(double pTerminatorDiscontinuity)
  {
    fTerminatorDiscontinuity = pTerminatorDiscontinuity;
  }
}
