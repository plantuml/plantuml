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
package jcckit.data;

/**
 * Interface for all kinds of data elements.
 *
 * @author Franz-Josef Elmer
 */
public interface DataElement {
  /**
   * Returns the container containing this element.
   * @return <tt>null</tt> if this element is not an element of a container.
   */
  public DataContainer getContainer();

  /**
   * Sets the container which should contain this element.
   * This method should <b>not</b> used outside {@link DataContainer}..
   * @param container Container which should contains this element. Cann be
   *        <tt>null</tt> if this element does not belong to a container.
   */
  public void setContainer(DataContainer container);
}
