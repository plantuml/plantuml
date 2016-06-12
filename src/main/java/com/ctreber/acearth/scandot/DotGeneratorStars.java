package com.ctreber.acearth.scandot;

import java.util.Random;

/**
 * <p>Generate random stars as dots.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class DotGeneratorStars extends ScanDotGenerator
{
  private final int fImageWidth;
  private final int fImageHeight;
  private int fBigStars;
  private double fStarFrequency;
  private final Random lRandom;

  public DotGeneratorStars(int pWidth, int pHeight,
    double pStarFrequency, int pBigStars, Random rnd)
  {
	lRandom = rnd;
    fImageWidth = pWidth;
    fImageHeight = pHeight;
    fStarFrequency = pStarFrequency;
    fBigStars = pBigStars;
  }

  public void generateScanDots()
  {
    // Make sure stars don't jump around between updates.
	    // final Random lRandom = new Random(ACearth.getStartTime());

    final int lStarsMax = (int)(fImageWidth * fImageHeight * fStarFrequency);
    for(int i = 0; i < lStarsMax; i++)
    {
      // "-1" to leave space for big stars.
      int x = (int)(lRandom.nextDouble() * (fImageWidth - 1));
      int y = (int)(lRandom.nextDouble() * fImageHeight);

      fDots.add(new ScanDot(ScanDot.DotTypeStar, x, y));

      // A big star is just two pixels wide.
      if((fBigStars != 0) && (Math.random() * 100 < fBigStars))
      {
        fDots.add(new ScanDot(ScanDot.DotTypeStar, x + 1, y));
      }
    }
  }
}
