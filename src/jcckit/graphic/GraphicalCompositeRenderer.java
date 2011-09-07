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
 *  Interface of all {@link Renderer Renderers} who render a
 *  {@link GraphicalComposite}. Note, that a
 *  <tt>GraphicalCompositeRenderer</tt> does <em>not</em>
 *  render the element of a <tt>GraphicalComposite</tt>
 *
 *  @author Franz-Josef Elmer
 */
public interface GraphicalCompositeRenderer extends Renderer {
  /**
   *  Starts rendering of the specified composite before its
   *  elements are rendererd. Implementations of this method
   *  usually obtain the {@link ClippingShape} from
   *  <tt>composite</tt>.
   */
  public void startRendering(GraphicalComposite composite);

  /** Finishes rendering of the specified composite. */
  public void finishRendering(GraphicalComposite composite);
}
