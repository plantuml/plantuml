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

import java.awt.Color;

/**
 *  Interface for line attributes.
 *
 *  @author Franz-Josef Elmer
 */
public interface LineAttributes extends GraphicAttributes {
  /**
   *  Returns the line color.
   *  @return <tt>null</tt> means default color of the renderer.
   */
  public Color getLineColor();

  /**
   * Returns the line tickness. 0 means that the line thickness is
   * chosen as thin as possible.
   * Implementations have to guarantee that the returned value is
   * never negative.
   */
  public double getLineThickness();

  /**
   *  Returns the line pattern. This is a sequence of length where the
   *  pen is down or up. The first element is the length where the
   *  pen is down. The next element is the length where the pen is up.
   *  The pattern is cyclically repeated.
   *  @return <tt>null</tt> means solid line.
   */
  public double[] getLinePattern();
}

