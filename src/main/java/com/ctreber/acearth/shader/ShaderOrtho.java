package com.ctreber.acearth.shader;

import java.awt.Color;

/**
 * <p>Shader for the orthographic projection.</p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com (Nov 11, 2002)</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class ShaderOrtho extends Shader
{
  private static double[] fXValues;

  public Color[] getShadedColors(int pRowNo, int[] pRowTypes)
  {
    if(pRowNo == 0)
    {
      fXValues = computeXValues(pRowTypes.length);
    }

    final double y = fProjection.inverseProjectY(fProjection.inverseFinalizeY(pRowNo));
    final double tmp = 1 - (y * y);
    final double lYBySunVectorY = y * fLightVector.getY();

    final Color[] lColors = new Color[pRowTypes.length];
    for(int lColNo = 0; lColNo < pRowTypes.length; lColNo++)
    {
      double x = fXValues[lColNo];
      double z = Math.sqrt(tmp - (x * x));

      double lSunValue = (x * fLightVector.getX()) + lYBySunVectorY + (z * fLightVector.getZ());
      lColors[lColNo] = getShadedColorForType(pRowTypes[lColNo], lSunValue);
    }

    return lColors;
  }

  /**
   *
   * @return X value for each column in image.
   */
  private double[] computeXValues(int pWidth)
  {
    final double[] lTable = new double[pWidth];

    for(int lColNo = 0; lColNo < pWidth; lColNo++)
    {
      lTable[lColNo] = fProjection.inverseProjectX(fProjection.inverseFinalizeX(lColNo));
    }

    return lTable;
  }
}
