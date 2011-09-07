package com.ctreber.aclib.gui;



/**
 * <p></p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class MOString extends MonitoredObject
{
  private String fString;

  public MOString(String pString)
  {
    fString = pString;
  }

  public void set(String pString)
  {
    fString = pString;
    fireValueChanged();
  }

  public String get()
  {
    return fString;
  }

  public boolean checkRange()
  {
    return true;
  }
}
