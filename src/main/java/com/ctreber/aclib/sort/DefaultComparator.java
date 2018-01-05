package com.ctreber.aclib.sort;

import java.util.Comparator;

/**
 * <p>Implements a default Comparator based on Comparable and a ascending
 * sort order. Requires that the two objects are Comparable.
 *
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class DefaultComparator implements Comparator
{
  public int compare(Object o1, Object o2)
  {
    return ((Comparable)o1).compareTo(o2);
  }
}
