package com.ctreber.aclib.gui;

/**
 * <p></p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class MOBoolean extends MonitoredObject
{
  private boolean fBoolean;

  public MOBoolean()
  {
  }

  public MOBoolean(boolean pBoolean)
  {
    fBoolean = pBoolean;
  }

  public void set(boolean pValue)
  {
    fBoolean = pValue;
    fireValueChanged();
  }

  public boolean get()
  {
    return fBoolean;
  }

  public boolean checkRange()
  {
    return true;
  }
}
