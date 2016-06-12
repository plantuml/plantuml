package com.ctreber.acearth.shader;

import java.awt.Color;

/**
 * <p>Flat shader (does not care for Projection).</p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com (Nov 11, 2002)</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class ShaderFlat extends Shader
{
  public Color[] getShadedColors(int pRowNo, int[] pRowTypes)
  {
    final Color[] lColors = new Color[pRowTypes.length];
    for(int i = 0; i < pRowTypes.length; i++)
    {
      lColors[i] = getShadedColorForType(pRowTypes[i], 1.0);
    }

    return lColors;
  }
}
