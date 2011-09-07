package com.ctreber.aclib.gui;

/**
 * <p></p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class MOInteger extends MonitoredObject
{
  private int fInteger;
  private boolean fCheckRange = false;
  private int fMin;
  private int fMax;

  public MOInteger()
  {
  }

  public MOInteger(int pInteger)
  {
    fInteger = pInteger;
  }

  public MOInteger(int pInteger, int pMin, int pMax)
  {
    fMin = pMin;
    fMax = pMax;
    fCheckRange = true;
    set(pInteger);
  }

  public void set(int pInteger)
  {
    if(!checkRange(pInteger))
    {
      throw new IllegalArgumentException("Argument '" + pInteger +
        "' out of range [" + niceFormat(fMin) + "; " + niceFormat(fMax) + "]");
    }
    fInteger = pInteger;
    fireValueChanged();
  }

  private static String niceFormat(int pInteger)
  {
    if(pInteger == Integer.MAX_VALUE)
    {
      return "Infinity";
    }

    if(pInteger == Integer.MIN_VALUE)
    {
      return "-Infinity";
    }

    return Integer.toString(pInteger);
  }

  public int get()
  {
    return fInteger;
  }

  private boolean checkRange(int pInteger)
  {
    return !fCheckRange || (fMin <= pInteger) && (pInteger <= fMax);
  }

  public boolean checkRange()
  {
    return checkRange(fInteger);
  }
}
