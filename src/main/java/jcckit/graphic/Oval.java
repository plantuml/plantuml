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
 *  An oval (i.e.&nbsp;an ellipse).
 *
 *  @author Franz-Josef Elmer
 */
public class Oval extends Rectangle {
  /**
   *  Creates a new instance.
   *  @param center The position of the center of this element.
   *  @param width The width.
   *  @param height The height.
   *  @param attributes Drawing attributes. Can be <tt>null</tt>.
   */
  public Oval(GraphPoint center, double width, double height,
                   GraphicAttributes attributes) {
    super(center, width, height, attributes);
  }

  /**
   *  Renders this oval with the specified {@link Renderer}.
   *  @param renderer An instance of {@link OvalRenderer}.
   *  @throws IllegalArgumentException if <tt>renderer</tt> is not
   *          an instance of <tt>OvalRenderer</tt>.
   */
  public void renderWith(Renderer renderer) {
    if (renderer instanceof OvalRenderer) {
      ((OvalRenderer) renderer).render(this);
    } else {
      throw new IllegalArgumentException(renderer
                            + " does not implements OvalRenderer.");
    }
  }
}

