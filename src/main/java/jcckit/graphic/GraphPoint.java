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
package jcckit.graphic;

import jcckit.util.Point;

/**
 * Immutable class of a two-dimensional point in the device-independent 
 * coordinate system.
 *
 * @author Franz-Josef Elmer
 */
public class GraphPoint extends Point {
  /**
   *  Creates an instance for the specified vector.
   *  If <tt>vector</tt> is <tt>null</tt> or not long enough the
   *  default value 0 will be used instead.
   */
  public GraphPoint(double[] vector) {
    super(vector);
  }

  /** Creates an instance for the specified coordinates. */
  public GraphPoint(double x, double y) {
    super(x, y);
  }
}
