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
 *  Container for {@link GraphicalElement GraphicalElements}.
 *
 *  @author Franz-Josef Elmer
 */
public class GraphicalComposite implements GraphicalElement {
  private final Vector _elements = new Vector();
  private final ClippingShape _clippingShape;

  /**
   *  Creates an instance with the specified clipping shape.
   *  @param clippingShape Clipping shape or <tt>null</tt> if no clipping.
   */
  public GraphicalComposite(ClippingShape clippingShape) {
    _clippingShape = clippingShape;
  }

  /**
   *  Returns the clipping shape.
   *  @return <tt>null</tt> if no clipping should be applied.
   */
  public ClippingShape getClippingShape() {
    return _clippingShape;
  }

  /** 
   * Adds the specified element at the end of the list of elements. 
   * @param element Element to be added. <tt>null</tt> is not allowed.
   * @throws NullPointerException if <tt>element == null</tt>
   */
  public void addElement(GraphicalElement element) {
    if (element == null) {
      throwNullPointerException();
    } else {
      _elements.addElement(element);
    }
  }

  /** Remove all elements. */
  public void removeAllElements() {
    _elements.removeAllElements();
  }

  /**
   * Replaces the specified element at the specified index of
   * the list of elements.
   * @param element New element. <tt>null</tt> is not allowed.
   * @throws NullPointerException if <tt>element == null</tt>
   */
  public void replaceElementAt(int index, GraphicalElement element) {
    if (element == null) {
      throwNullPointerException();
    } else {
      _elements.setElementAt(element, index);
    }
  }
  
  private void throwNullPointerException() {
    throw new NullPointerException(
        "A null as an GraphicalElement is not allowed");
  }

  /**
   *  Renders all {@link GraphicalElement GraphicalElements} in the sequence
   *  they have been added.
   *  @param renderer Renderer which implements all renderer interfaces
   *         necessary to render the child elements.
   *  @throws IllegalArgumentException if <tt>renderer</tt> is not
   *          an instance of <tt>GraphicalCompositeRenderer</tt>.
   */
  public void renderWith(Renderer renderer) {
    if (renderer instanceof GraphicalCompositeRenderer) {
      GraphicalCompositeRenderer r = (GraphicalCompositeRenderer) renderer;
      r.startRendering(this);
      for (int i = 0, n = _elements.size(); i < n; i++) {
        ((GraphicalElement) _elements.elementAt(i)).renderWith(r);
      }
      r.finishRendering(this);
    } else {
      throw new IllegalArgumentException(renderer
                      + " does not implements GraphicalCompositeRenderer.");
    }
  }
}
