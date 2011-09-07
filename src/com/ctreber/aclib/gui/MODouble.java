package com.ctreber.aclib.gui;

/**
 * <p></p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class MODouble extends MonitoredObject
{
  private double fDouble;
  private boolean fCheckRange = false;
  private double fMin;
  private double fMax;

  public MODouble()
  {
  }

  public MODouble(double pDouble)
  {
    fDouble = pDouble;
  }

  public MODouble(double pDouble, double pMin, double pMax)
  {
    fMin = pMin;
    fMax = pMax;
    fCheckRange = true;
    set(pDouble);
  }

  public void set(double pDouble)
  {
    if(!checkRange(pDouble))
    {
      throw new IllegalArgumentException("Argument '" + pDouble +
        "' out of range [" + niceFormat(fMin) + "; " + niceFormat(fMax) + "]");
    }
    fDouble = pDouble;
    fireValueChanged();
  }

  private static String niceFormat(double pDouble)
  {
    if(pDouble == Double.MAX_VALUE)
    {
      return "Infinity";
    }

    if(pDouble == Double.MIN_VALUE)
    {
      return "-Infinity";
    }

    return Double.toString(pDouble);
  }

  public double get()
  {
    return fDouble;
  }

  private boolean checkRange(double pDouble)
  {
    return !fCheckRange || (fMin <= pDouble) && (pDouble <= fMax);
  }

  public boolean checkRange()
  {
    return checkRange(fDouble);
  }
}
