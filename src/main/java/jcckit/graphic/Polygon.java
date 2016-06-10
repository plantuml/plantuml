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

import java.util.Vector;

/**
 *  A polygon or polyline.
 *
 *  @author Franz-Josef Elmer
 */
public class Polygon extends BasicGraphicalElement {
  private final Vector _points = new Vector();
  private final boolean _closed;

  /**
   * Creates an instance of the specified graphic attributes.
   * @param closed <tt>true</tt> if this polygon is closed.
   */
  public Polygon(GraphicAttributes attributes, boolean closed) {
    super(attributes);
    _closed = closed;
  }

  /** Returns <tt>true</tt> if this polygon is closed. */
  public boolean isClosed() {
    return _closed;
  }

  /** Returns the number points. */
  public int getNumberOfPoints() {
    return _points.size();
  }

  /** Returns the point for the specified index. */
  public GraphPoint getPoint(int index) {
    return (GraphPoint) _points.elementAt(index);
  }

  /** Adds a new point to the end of the list of points. */
  public void addPoint(GraphPoint point) {
    _points.addElement(point);
  }

  /** Removes all points. */
  public void removeAllPoints() {
    _points.removeAllElements();
  }

  /** Replaces the point at the specified index by a new one. */
  public void replacePointAt(int index, GraphPoint point) {
    _points.setElementAt(point, index);
  }

  /**
   *  Renders this line with the specified {@link Renderer}.
   *  @param renderer An instance of {@link PolygonRenderer}.
   *  @throws IllegalArgumentException if <tt>renderer</tt> is not
   *          an instance of <tt>PolygonRenderer</tt>.
   */
  public void renderWith(Renderer renderer) {
    if (renderer instanceof PolygonRenderer) {
      ((PolygonRenderer) renderer).render(this);
    } else {
      throw new IllegalArgumentException(renderer
                            + " does not implements PolygonRenderer.");
    }
  }
}
