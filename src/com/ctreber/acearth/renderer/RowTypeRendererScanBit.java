package com.ctreber.acearth.renderer;

import com.ctreber.acearth.scanbit.BitGeneratorMap;
import com.ctreber.acearth.scanbit.ScanBit;

/**
 * <p>Renders a row of ScanBits to pixel types.</p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com (Nov 11, 2002)</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class RowTypeRendererScanBit implements RowTypeRenderer
{
  private int fScanBitIndex;
  private ScanBit[] fScanBits;
  private final int[] fScanToPixelType = new int[256];

  public void startNewRun()
  {
    fScanBitIndex = 0;
    generateScanToPixelTypeTable();
  }

  public void getPixelTypes(int pRowNo, int[] pPixelTypes)
  {
    // For all ScanBits in specified row...
    while((fScanBitIndex < fScanBits.length) &&
      (fScanBits[fScanBitIndex].getY() == pRowNo))
    {
      for(int i = fScanBits[fScanBitIndex].getlXFrom();
          i <= fScanBits[fScanBitIndex].getXTo(); i++)
      {
        /**
         * This is weird... why summing up the types? Note the row stays the
         * same, but it possibly gets paved over a couple of times (There
         * might be ScanBits painting on the same pixels).
         *
         * The polygons specify -1 as water and 1 as land.
         * The type table says space is 0, Water is 1 to 64, Land is 65+.
         *
         * The outline paints the whole world as water (64). Adding a
         * land pixel (1) creates a value of 65 (land). Adding a water
         * pixel (-1) changes this back to 64 (water).
         */
        pPixelTypes[i] += fScanBits[fScanBitIndex].getType();
      }
      fScanBitIndex++;
    }

    // Translate generateScanBits values into pixels types.
    for(int lCol = 0; lCol < pPixelTypes.length; lCol++)
    {
      pPixelTypes[lCol] = fScanToPixelType[pPixelTypes[lCol] & 0xff];
    }
  }

  private void generateScanToPixelTypeTable()
  {
    for(int i = 0; i < 256; i++)
    {
      if(i == 0)
      {
        // 0 is Space.
        fScanToPixelType[i] = BitGeneratorMap.PixTypeSpace;
      } else if(i > 64)
      {
        // Above 64 it's land.
        fScanToPixelType[i] = BitGeneratorMap.PixTypeLand;
      } else
      {
        // From 1 to 64 incl. it's water
        fScanToPixelType[i] = BitGeneratorMap.PixTypeWater;
      }
    }
  }

  public void setScanBits(ScanBit[] pScanBits)
  {
    fScanBits = pScanBits;
  }
}
