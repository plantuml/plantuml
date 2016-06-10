package com.ctreber.acearth.shader;

import java.awt.Color;

/**
 * <p>Shader for projections which display the whole surface.</p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com (Nov 11, 2002)</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class ShaderDefault extends Shader
{
  public Color[] getShadedColors(int pRowNo, int[] pRowTypes)
  {
    final double y = fProjection.inverseProjectY(fProjection.inverseFinalizeY(pRowNo));

    // conceptually, on each iteration of the i loop, we want:
    //
    //   x = Math.sin(INV_XPROJECT(i)) * sqrt(1 - (y*y));
    //   z = cos(INV_XPROJECT(i)) * sqrt(1 - (y*y));
    //
    // computing this directly is rather expensive, however, so we only
    // compute the first (i=0) pair of values directly; all other pairs
    // (i>0) are obtained through successive rotations of the original
    // pair (by inv_proj_scale radians).
    //

    // compute initial (x, z) values
    double tmp = Math.sqrt(1 - (y * y));
    double x = Math.sin(fProjection.inverseFinalizeX(0)) * tmp;
    double z = Math.cos(fProjection.inverseFinalizeX(0)) * tmp;

    // compute rotation coefficients used
    // to find subsequent (x, z) values
    tmp = 1 / fProjection.getScale();
    final double sin_theta = Math.sin(tmp);
    final double cos_theta = Math.cos(tmp);

    // save a little computation in the inner loop
    final double lYBySunVectorY = y * fLightVector.getY();

    // use i_lim to encourage compilers to register loop limit
    final Color[] lColors = new Color[pRowTypes.length];
    for(int lColNo = 0; lColNo < pRowTypes.length; lColNo++)
    {
      double lSunValue = (x * fLightVector.getX()) + lYBySunVectorY +
        (z * fLightVector.getZ());
      lColors[lColNo] = getShadedColorForType(pRowTypes[lColNo], lSunValue);

      // compute next (x, z) values via 2-d rotation
      tmp = (cos_theta * z) - (sin_theta * x);
      x = (sin_theta * z) + (cos_theta * x);
      z = tmp;
    }

    return lColors;
  }
}
