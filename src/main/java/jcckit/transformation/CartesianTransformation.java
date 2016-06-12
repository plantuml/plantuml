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
import jcckit.util.Util;

/**
 *  Two-dimensional Cartesian transformation. The two independent
 *  transformations for the x-axis and the y-axis can be logarithmic
 *  from data coordinates to device-independent coordinates in order to
 *  realize diagrams with logarithmic scales.
 *
 *  @author Franz-Josef Elmer
 */
public class CartesianTransformation implements Transformation {
  private final boolean _xLogScale;
  private final double _xOffset;
  private final double _xScale;
  private final boolean _yLogScale;
  private final double _yOffset;
  private final double _yScale;

  /**
   *  Creates an instance from the specified reference points.
   *  Note, that the reference points must differ in x and y coordinates
   *  otherwise a transformation would not be possible.
   *  @param xLogScale <tt>true</tt> if logarithmic x axis.
   *  @param yLogScale <tt>true</tt> if logarithmic y axis.
   *  @param dataPoint1 First reference point in data coordinates.
   *  @param graphPoint1 First reference point in device-independent 
   *         coordinates.
   *  @param dataPoint2 Second reference point in data coordinates.
   *  @param graphPoint2 Second reference point in device-independent
   *         coordinates.
   *  @throws IllegalArgumentException if transformation in at least
   *          one of both directions is not possible.
   */
  public CartesianTransformation(boolean xLogScale, boolean yLogScale,
                           DataPoint dataPoint1, GraphPoint graphPoint1,
                           DataPoint dataPoint2, GraphPoint graphPoint2) {
    _xLogScale = xLogScale;
    double d1 = Util.log(dataPoint1.getX(), xLogScale);
    double dd = Util.log(dataPoint2.getX(), xLogScale) - d1;
    check(dd, "data", "x", d1);
    _xScale = (graphPoint2.getX() - graphPoint1.getX()) / dd;
    check(_xScale, "graphical", "x", graphPoint1.getX());
    _xOffset = graphPoint1.getX() - d1 * _xScale;

    _yLogScale = yLogScale;
    d1 = Util.log(dataPoint1.getY(), yLogScale);
    dd = Util.log(dataPoint2.getY(), yLogScale) - d1;
    check(dd, "data", "y", d1);
    _yScale = (graphPoint2.getY() - graphPoint1.getY()) / dd;
    check(_yScale, "graphical", "y", graphPoint1.getY());
    _yOffset = graphPoint1.getY() - d1 * _yScale;
  }

  private void check(double valueToCheck, String type, String axis,
                     double value) {
    if (valueToCheck == 0) {
      throw new IllegalArgumentException("The " + type
          + " reference points in " + axis + " must be different; both are "
          + value);
    }
  }

  public GraphPoint transformToGraph(DataPoint point) {
    return new GraphPoint(
        _xOffset + Util.log(point.getX(), _xLogScale) * _xScale,
        _yOffset + Util.log(point.getY(), _yLogScale) * _yScale);
  }

  public DataPoint transformToData(GraphPoint point) {
    return new DataPoint(
        Util.exp((point.getX() - _xOffset) / _xScale, _xLogScale),
        Util.exp((point.getY() - _yOffset) / _yScale, _yLogScale));
  }
}
