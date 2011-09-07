package com.ctreber.acearth.scanbit;

/**
 * <p>A ScanBitGenerator produces ScanBits.</p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com (Nov 11, 2002)</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
abstract public class ScanBitGenerator
{
  int fImageHeight;
  int fImageWidth;
  protected ScanBit[] fScanBitsArray;

  abstract public void generateScanBits();

  public void setImageHeight(int pImageHeight)
  {
    fImageHeight = pImageHeight;
  }

  public void setImageWidth(int pImageWidth)
  {
    fImageWidth = pImageWidth;
  }

  public ScanBit[] getScanBits()
  {
    return fScanBitsArray;
  }
}
