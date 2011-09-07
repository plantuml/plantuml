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
package jcckit.transformation;

import jcckit.data.DataPoint;
import jcckit.graphic.GraphPoint;

/**
 * Interface for transformations between data coordinates 
 * and device-independent coordinates.
 *
 * @author Franz-Josef Elmer
 */
public interface Transformation {
  /**
   * Transforms a {@link DataPoint} into a {@link GraphPoint}.
   * @param point A point in data coordinates.
   * @return <tt>point</tt> tranformed into device-independent coordinates..
   */
  public GraphPoint transformToGraph(DataPoint point);

  /**
   * Transforms a {@link GraphPoint} into a {@link DataPoint}.
   *  @param point A point in device-independent coordinates..
   *  @return <tt>point</tt> tranformed into data coordinates.
   */
  public DataPoint transformToData(GraphPoint point);
}
