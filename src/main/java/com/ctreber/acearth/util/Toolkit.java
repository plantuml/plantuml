package com.ctreber.acearth.util;

import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * <p>Some tools.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class Toolkit
{
  public static final double TWOPI = Math.PI * 2;
  public static final double PI = Math.PI;
  public static final double HALFPI = Math.PI / 2;
  private static final HashSet fsNoCap;

  static
  {
    fsNoCap = new HashSet();
    fsNoCap.add("a");
    fsNoCap.add("as");
    fsNoCap.add("to");
    fsNoCap.add("of");
    fsNoCap.add("the");
    fsNoCap.add("off");
    fsNoCap.add("and");
    fsNoCap.add("mid");
  }

  public static double degsToRads(double pDegrees)
  {
    return pDegrees * TWOPI / 360;
  }

  public static double radsToDegs(double pRadians)
  {
    return pRadians * 360 / TWOPI;
  }

  /**
   * Force an angular value into the range [-PI, +PI]
   */
  public static double limitRads(double x)
  {
    return fmod(x, -Math.PI, Math.PI);
  }

  /**
   * <p>Verified.
   */
  public static double fmod(double pValue, double pMod)
  {
    while(pValue < 0)
    {
      pValue += pMod;
    }
    while(pValue > pMod)
    {
      pValue -= pMod;
    }

    return pValue;
  }

  /**
   * <p>Examples: min -2, max 2: range 4
   *
   * <ul>
   * <li> value 1: lFact = 0
   * <li> value 3: lFact = 1, value -1
   * <li> value 9: lFact = 2, value 1
   * <li> value -3: lFact = -1, value 1
   * </ul>
   */
  public static double fmod(double pValue, double pMinValue, double pMaxValue)
  {
    final double lRange = pMaxValue - pMinValue;
    int lFact = (int)((pValue - pMinValue) / lRange);
    if(pValue < pMinValue)
    {
      lFact--;
    }

    return pValue - lFact * lRange;
  }

  /**
   * <p>Capitalize String. Uppercase words smaller/equal than 3 chars,
   * lowercase defined exceptions. Capitalize within word after '.' and '-'.
   * Capitalize all others.
   */
  public static String intelligentCapitalize(String pText)
  {
    boolean lDoCap = false;
    final StringTokenizer lST = new StringTokenizer(pText, ".- ", true);
    final StringBuffer lSB = new StringBuffer(50);
    while(lST.hasMoreTokens())
    {
      String lWord = lST.nextToken();

      if(lWord.equals(".") || lWord.equals("-"))
      {
        lDoCap = true;
        lSB.append(lWord);
        continue;
      }
      if(lWord.equals(" "))
      {
        lDoCap = false;
        lSB.append(lWord);
        continue;
      }

      if(lDoCap || (lWord.length() > 3))
      {
        lSB.append(Character.toUpperCase(lWord.charAt(0)));
        lSB.append(lWord.substring(1).toLowerCase());
      } else
      {
        if(fsNoCap.contains(lWord.toLowerCase()))
        {
          lSB.append(lWord.toLowerCase());
        } else
        {
          lSB.append(lWord.toUpperCase());
        }
      }
    }

    return lSB.toString();
  }
}
