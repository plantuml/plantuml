package com.ctreber.acearth.scanbit;

import java.util.ArrayList;
import java.util.List;

import com.ctreber.aclib.sort.CTSort;
import com.ctreber.aclib.sort.QuickSort;

/**
 * <p>For each line, the scanbuffer (= a raster divice) records the points hit
 * I.e., line 5 (y=5) contains the values 2, 6, 40, and 46 (these line have
 * been crossed). The values always come as pairs because we're dealing with
 * polygons, which have a left and a right side which consists of a line.
 * The points in between two values painted as filled.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
class ScanBuf
{
  private List[] fScanbuf;
  private int fLineMin;
  private int fLineMax;
  private final int fLines;
  private final int fPoints;
  private boolean fScanBufsAdded;

  /**
   * <p>Create a list for each line.
   *
   * @param pLines Number of lines aka screen height.
   * @param pPoints Number of points per line aka screen width.
   */
  public ScanBuf(int pLines, int pPoints)
  {
    fLines = pLines;
    fPoints = pPoints;

    fLineMin = Integer.MAX_VALUE;
    fLineMax = Integer.MIN_VALUE;
    fScanBufsAdded = false;

    fScanbuf = new ArrayList[fLines];
    for(int i = 0; i < fScanbuf.length; i++)
    {
      fScanbuf[i] = new ArrayList();
    }
  }

  /**
   * <p>Add a line to the generateScanBits buffer.
   */
  public void addLine(double pXFrom, double pYFrom, double pXTo, double pYTo)
  {
    int lYFrom;
    int lYTo;

    // Do some rounding (but not in the way we expect it), limit values
    if(pYFrom < pYTo)
    {
      // Round lYFrom (but .5 is handled oddly)
      // 1.5001 - 2.5 -> 1.0001 - 2.0 -> 2
      lYFrom = (int)Math.ceil(pYFrom - 0.5);
      // Round lYTo, substract 1
      // 1.5 - 2.4999 -> 1.0 - 1.9999 -> 1
      lYTo = (int)Math.floor(pYTo - 0.5);

      /**
       * I don't know if this is intended, but in Java 3 == 3.001 is false
       * (the left arg is converted to double), so the expr is true only when
       * pYTo - 0.5 is exactly lYTo
       */
      if(lYTo == pYTo - 0.5)
      {
        lYTo--;
      }
    } else
    {
      lYFrom = (int)Math.ceil(pYTo - 0.5);
      lYTo = (int)Math.floor(pYFrom - 0.5);

      if(lYTo == pYFrom - 0.5)
      {
        lYTo--;
      }
    }

    // Limit y to size of image
    if(lYFrom < 0)
    {
      lYFrom = 0;
    }
    if(lYTo >= fLines)
    {
      lYTo = fLines - 1;
    }

    if(lYFrom > lYTo)
    {
      // No lines crossed.
      return;
    }

    // Note min/max settings so far
    if(lYFrom < fLineMin)
    {
      fLineMin = lYFrom;
    }
    if(lYTo > fLineMax)
    {
      fLineMax = lYTo;
    }

    // todo Curious: What happens if yFrom and yTo are equal? Shit? Or can't they be?
    double lDx = (pXTo - pXFrom) / (pYTo - pYFrom);
    double lX = pXFrom + lDx * ((lYFrom + 0.5) - pYFrom);

    // Record the x value for every line (y).
    for(int lLineNo = lYFrom; lLineNo <= lYTo; lLineNo++)
    {
      fScanbuf[lLineNo].add(new Double(lX));
      lX += lDx;
    }
    fScanBufsAdded = true;
  }

  public boolean containsPoints()
  {
    return fScanBufsAdded;
  }

  /**
   * <p>For each line, for each x value pair in line, create one ScanBit.
   */
  public List getScanbits(int pCurveType)
  {
    final List fScanBits = new ArrayList();

    // For each generateScanBits line containing points
    for(int lLineNo = fLineMin; lLineNo <= fLineMax; lLineNo++)
    {
      // Sort so that lowest x values come first.
      Double[] lScanLine = (Double[])fScanbuf[lLineNo].toArray(new Double[0]);
      CTSort lSort = new QuickSort();
      lSort.sort(lScanLine);

      // The length will be divisible by 2 because we render closed polyons,
      // so every generateScanBits line is crossed twice (left and right edge of polygon,
      // no intersections allowed!).
      for(int n = 0; n < lScanLine.length; n += 2)
      {
        // Round lLineFrom (but .5 is handled oddly)
        // 1.5001 - 2.5 -> 1.0001 - 2.0 -> 2
        int lXLo = (int)Math.ceil(lScanLine[n].doubleValue() - 0.5);
        // Round lLineTo, substract 1
        // 1.5 - 2.4999 -> 1.0 - 1.9999 -> 1
        int lXHi = (int)Math.floor(lScanLine[n + 1].doubleValue() - 0.5);

        // Limit low and high x to image dimensions
        if(lXLo < 0)
        {
          lXLo = 0;
        }
        if(lXHi >= fPoints)
        {
          lXHi = fPoints - 1;
        }

        if(lXLo <= lXHi)
        {
          /**
           * Shouldn't that always be true since we sorted? "Yes", BUT the
           * rounding might create lo 3.6 -> 4.0 and hi 3.7 -> 3.0
           */
          fScanBits.add(new ScanBit(lLineNo, lXLo, lXHi, pCurveType));
        }
      }
    }

    return fScanBits;
  }

  public int getYMax()
  {
    return fLineMax;
  }

  public int getYMin()
  {
    return fLineMin;
  }
}
