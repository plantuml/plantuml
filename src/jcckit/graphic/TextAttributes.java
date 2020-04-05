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
 *  Interface for text attributes.
 *
 *  @author Franz-Josef Elmer
 */
public interface TextAttributes extends GraphicAttributes {
  /**
   *  Returns the text color.
   *  @return <tt>null</tt> means default color of the renderer.
   */
  public Color getTextColor();

  /**
   *  Returns the font name.
   *  @return <tt>null</tt> means default font name of the renderer.
   */
  public String getFontName();

  /**
   *  Returns the font style.
   *  @return <tt>null</tt> means default font style of the renderer.
   */
  public FontStyle getFontStyle();

  /**
   *  Returns the font size in units of the device-independent coordinates.
   */
  public double getFontSize();

  /**
   *  Returns the orientation angle in degree. Zero means
   *  normal text orientation. Any positive angle means a
   *  counter-clockwise rotation of the text.
   */
  public double getOrientationAngle();

  /**
   *  Returns the anchor for horizontal position of the text.
   *  Note, that the anchor is related to the text <em>before</em>
   *  it is rotated by the orientation angle.
   *  @return one of the three instances of <tt>Anchor</tt>.
   */
  public Anchor getHorizontalAnchor();

  /**
   *  Returns the anchor for vertical position of the text.
   *  Note, that the anchor is related to the text <em>before</em>
   *  it is rotated by the orientation angle.
   *  @return one of the three instances of <tt>Anchor</tt>.
   */
  public Anchor getVerticalAnchor();
}

