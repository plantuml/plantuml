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
package jcckit.plot;

import jcckit.graphic.GraphPoint;
import jcckit.util.ConfigParameters;

/**
 * An immutable {@link Hint} capsulating two {@link GraphPoint GraphPoints}.
 * 
 * @author Franz-Josef Elmer
 */
public class PositionHint implements Hint {
  /** Configuration parameter key. */
  public static final String POSITION_KEY = "position",
                             ORIGIN_KEY = "origin";
  private final GraphPoint _position;
  private final GraphPoint _origin;

  /**
   * Creates an instance from the specified configuration parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>position = null</tt></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Definition of position.</td></tr>
   * <tr><td><tt>origin = position</tt> or (0,0) if <tt>position</tt> 
   *         undefined</td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Definition of origin.</td></tr>
   * </table>
   */
  public PositionHint(ConfigParameters config) {
    double[] point = config.getDoubleArray(POSITION_KEY, null);
    _position = point == null ? null : new GraphPoint(point);
    _origin = new GraphPoint(config.getDoubleArray(ORIGIN_KEY, point));
  }

  /**
   * Creates an instance based on two points.
   * @param origin The origin.
   * @param position The position.
   */
  public PositionHint(GraphPoint origin, GraphPoint position) {
    _origin = origin;
    _position = position;
  }

  /** Returns the position. */
  public GraphPoint getPosition() {
    return _position;
  }

  /** Returns the origin. */
  public GraphPoint getOrigin() {
    return _origin;
  }
}
