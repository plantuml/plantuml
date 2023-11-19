package zext.plantuml.com.ctreber.aclib.gui;

/**
 * <p></p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class MOBoolean extends MonitoredObject
{
  // ::remove folder when __CORE__
  // ::remove folder when __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__
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
