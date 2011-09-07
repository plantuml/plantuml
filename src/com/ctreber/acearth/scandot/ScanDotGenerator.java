package com.ctreber.acearth.scandot;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>A ScanDotGenerator produces ScanDots.
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
abstract public class ScanDotGenerator
{
  List fDots = new ArrayList();

  /**
   * <p>Generate whatever dots are generated.
   */
  abstract public void generateScanDots();

  public List getScanDots()
  {
    return fDots;
  }
}
