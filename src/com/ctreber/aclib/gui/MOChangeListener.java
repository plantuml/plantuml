package com.ctreber.aclib.gui;

/**
 * <p>Implemented by classes interetested in MonitoredObject values changes.</p>
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com</p>
 * @author Christian Treber, ct@ctreber.com
 *
 */
public interface MOChangeListener
{
  public void valueChanged(MonitoredObject pObject);
}
