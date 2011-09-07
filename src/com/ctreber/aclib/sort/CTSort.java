package com.ctreber.aclib.sort;

import java.util.Comparator;

/**
 * <p>Teehee - found that Comparator allready exists.
 *
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
abstract public class CTSort
{
  public void sort(Object[] items)
  {
    sort(items, new DefaultComparator());
  }

  abstract public void sort(Object[] items, Comparator comparator);
}
