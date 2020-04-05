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

/**
 *  Immutable class of a rectangular clipping area.
 *
 *  @author Franz-Josef Elmer
 */
public class ClippingRectangle implements ClippingShape {
  private final double _minX, _minY, _maxX, _maxY;

  /**
   *  Creates an instance for the specified coordinates of
   *  two opposite corner points.
   */
  public ClippingRectangle(double x1, double y1, double x2, double y2) {
    _minX = Math.min(x1, x2);
    _minY = Math.min(y1, y2);
    _maxX = Math.max(x1, x2);
    _maxY = Math.max(y1, y2);
  }

  /**
   *  Returns <tt>true</tt> if the specified point is inside this
   *  rectangle.
   */
  public boolean isInside(GraphPoint point) {
    double x = point.getX();
    double y = point.getY();
    return _minX <= x && x <= _maxX && _minY <= y && y <= _maxY;
  }

  /** Returns the minimum x value. */
  public double getMinX() {
    return _minX;
  }

  /** Returns the maximum x value. */
  public double getMaxX() {
    return _maxX;
  }

  /** Returns the minimum y value. */
  public double getMinY() {
    return _minY;
  }

  /** Returns the maximum y value. */
  public double getMaxY() {
    return _maxY;
  }

  /** Returns this instance. */
  public ClippingRectangle getBoundingBox() {
    return this;
  }
  
  /** Returns a {@link Rectangle}. */
  public BasicGraphicalElement getGraphicalElement() {
    return new Rectangle(new GraphPoint(0.5 * (_minX + _maxX), 
                                        0.5 * (_minY + _maxY)),
                         _maxX - _minX, _maxY - _minY, null);
  }
}
