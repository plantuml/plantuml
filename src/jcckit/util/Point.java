/*
 * Copyright 2003-2004, Franz-Josef Elmer, All rights reserved
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details
 * (http://www.gnu.org/copyleft/lesser.html).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jcckit.util;

/**
 *  Immutable class of a two-dimensional point with floating point
 *  coordinates.
 *
 *  @author Franz-Josef Elmer
 */
public class Point {
  private final double _x;
  private final double _y;

  /**
   *  Creates an instance for the specified vector. The value of the 
   *  first/second element of <tt>vector</tt> denotes the x/y value.
   *  If <tt>vector</tt> is <tt>null</tt> or not long enough 0 will be used 
   *  as default values.
   */
  public Point(double[] vector) {
    double x = 0;
    double y = 0;
    if (vector != null && vector.length > 0) {
      x =  vector[0];
      if (vector.length > 1) {
        y =  vector[1];
      }
    }
    _x = x;
    _y = y;
  }

  /** Creates an instance for the specified coordinates. */
  public Point(double x, double y) {
    _x = x;
    _y = y;
  }

  /** Returns the x-coordinate of the point. */
  public double getX() {
    return _x;
  }

  /** Returns the y-coordinate of the point. */
  public double getY() {
    return _y;
  }
}
