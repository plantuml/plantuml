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
 * Defining a clipping shape applied to all {@link GraphicalElement
 * GraphicalElements} of a {@link GraphicalComposite}.
 *
 * @author Franz-Josef Elmer
 */
public interface ClippingShape {
  /**
   * Returns <tt>true</tt> if the specified point is inside this
   * clipping shape.
   */
  public boolean isInside(GraphPoint point);

  /**
   * Returns the bounding box of this clipping shape.
   * This method will be used by renderers who supports only
   * rectangular clipping shapes.
   */
  public ClippingRectangle getBoundingBox();
  
  /**
   * Returns a basic graphical element (such as {@link Rectangle}
   * or {@link Polygon}) which may be used by renderers to
   * define the clipping shape for the output device.
   */
  public BasicGraphicalElement getGraphicalElement();
}
