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
 *  Interface all graphical elements have to implement.
 *  Together with the marker interface {@link Renderer} it
 *  realizes the Anticyclic Visitor Pattern, a variant of the
 *  GoF Visitor Pattern. This allows not only to extend JCCKit with
 *  new renderers but also with new types of <tt>GraphicalElements</tt>
 *  without touching existing code.
 *
 *  @author Franz-Josef Elmer
 */
public interface GraphicalElement {
  /**
   *  Renders this element according to the type of renderer.
   *  Concrete <tt>GraphicalElements</tt> who are not instances of
   *  {@link GraphicalComposite} dynamically cast <tt>renderer</tt>.
   *  If it does not implement the type of renderer specific for
   *  the concrete <tt>GraphicalElement</tt> it should throw an
   *  <tt>IllegalArgumentException</tt>.
   */
  public abstract void renderWith(Renderer renderer);
}
