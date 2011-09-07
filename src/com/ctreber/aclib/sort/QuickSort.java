package com.ctreber.aclib.sort;

import java.util.Comparator;

/**
 * &copy; 2001 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 */
public class QuickSort extends CTSort
{
  public void sort(Object[] items, Comparator comparator)
  {
    if(items.length <= 1)
    {
      // Nothing to sort t all or only one element.
      return;
    }

    qsort(items, comparator, 0, items.length - 1);
    insertionSort(items, comparator, 0, items.length - 1);
  }

  private void qsort(Object[] items, Comparator comparator, int l, int r)
  {
    final int M = 4;
    int i;
    int j;
    Object v;

    if((r - l) > M)
    {
      i = (r + l) / 2;
      if(comparator.compare(items[l], items[i]) > 0)
      {
        swap(items, l, i);
      }
      if(comparator.compare(items[l], items[r]) > 0)
      {
        swap(items, l, r);
      }
      if(comparator.compare(items[i], items[r]) > 0)
      {
        swap(items, i, r);
      }

      j = r - 1;
      swap(items, i, j);
      i = l;
      v = items[j];
      while(true)
      {
        while(comparator.compare(items[++i], v) < 0)
        {
        }
        while(comparator.compare(items[--j], v) > 0)
        {
        }
        if(j < i)
        {
          break;
        }
        swap(items, i, j);
      }
      swap(items, i, r - 1);
      qsort(items, comparator, l, j);
      qsort(items, comparator, i + 1, r);
    }
  }

  private static void swap(Object[] items, int i, int j)
  {
    final Object tmp;
    tmp = items[i];
    items[i] = items[j];
    items[j] = tmp;
  }

  private static void insertionSort(Object[] items, Comparator comparator, int lo0, int hi0)
  {
    int i;
    int j;
    Object v;

    for(i = lo0 + 1; i <= hi0; i++)
    {
      v = items[i];
      j = i;
      while((j > lo0) && (comparator.compare(items[j - 1], v) > 0))
      {
        items[j] = items[j - 1];
        j--;
      }
      items[j] = v;
    }
  }
}
