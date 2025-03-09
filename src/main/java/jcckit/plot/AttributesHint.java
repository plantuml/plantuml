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

import jcckit.graphic.GraphicAttributes;

/**
 * A {@link Hint} which wraps a {@link GraphicAttributes} instance.
 * In addition the method {@link #getNextHint()} creates a new instance 
 * with different attributes derivated from the wrapped attributes.
 * 
 * @author Franz-Josef Elmer
 */
public interface AttributesHint extends Hint {
  /** 
   * Returns the hint for the next {@link Symbol} of a {@link Curve}. 
   * The new hint has a different {@link GraphicAttributes}.
   */
  public AttributesHint getNextHint();

  /** Returns the attributes value. */
  public GraphicAttributes getAttributes();
}
