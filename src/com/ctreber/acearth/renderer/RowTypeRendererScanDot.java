package com.ctreber.acearth.renderer;

import com.ctreber.acearth.scanbit.BitGeneratorMap;
import com.ctreber.acearth.scandot.ScanDot;

/**
 * <p>Renders a row of ScanDots to pixel types.</p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com (Nov 11, 2002)</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class RowTypeRendererScanDot implements RowTypeRenderer
{
  private int fScanDotIndex;
  private ScanDot[] fScanDots;

  public void startNewRun()
  {
    fScanDotIndex = 0;
  }

  public void getPixelTypes(int pRowNo, int[] pPixelTypes)
  {
    // For all ScanDots in specified row...
    while((fScanDotIndex < fScanDots.length) &&
      (fScanDots[fScanDotIndex].getY() == pRowNo))
    {
      ScanDot lDot = fScanDots[fScanDotIndex];

      if(lDot.getType() == ScanDot.DotTypeStar)
      {
        if(pPixelTypes[lDot.getX()] == BitGeneratorMap.PixTypeSpace)
        {
          // Stars get only painted on Space.
          pPixelTypes[lDot.getX()] = BitGeneratorMap.PixTypeStar;
        }
      } else
      {
        // The only other type for a dot (so far) is "grid".
        switch(pPixelTypes[lDot.getX()])
        {
          case BitGeneratorMap.PixTypeLand:
            pPixelTypes[lDot.getX()] = BitGeneratorMap.PixTypeGridLand;
            break;

          case BitGeneratorMap.PixTypeWater:
            pPixelTypes[lDot.getX()] = BitGeneratorMap.PixTypeGridWater;
            break;
        }
      }
      fScanDotIndex++;
    }
  }

  public void setScanDots(ScanDot[] pScanDots)
  {
    fScanDots = pScanDots;
  }
}
