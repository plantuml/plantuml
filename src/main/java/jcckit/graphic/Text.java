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
 *  A single line of text.
 *
 *  @author Franz-Josef Elmer
 */
public class Text extends BasicGraphicalElement {
  private final GraphPoint _position;
  private final String _text;


  /**
   *  Creates an instance with the specified parameters.
   *  @param position Position of the text.
   *  @param text Text.
   *  @param attributes Drawing attributes. Can be <tt>null</tt>.
   */
  public Text(GraphPoint position, String text, GraphicAttributes attributes) {
    super(attributes);
    _position = position;
    _text = text;
  }

  /** Returns the position. */
  public GraphPoint getPosition() {
    return _position;
  }

  /** Returns the text string. */
  public String getText() {
    return _text;
  }

  /**
   *  Renders this line with the specified {@link Renderer}.
   *  @param renderer An instance of {@link TextRenderer}.
   *  @throws IllegalArgumentException if <tt>renderer</tt> is not
   *          an instance of <tt>TextRenderer</tt>.
   */
  public void renderWith(Renderer renderer) {
    if (renderer instanceof TextRenderer) {
      ((TextRenderer) renderer).render(this);
    } else {
      throw new IllegalArgumentException(renderer
                            + " does not implements TextRenderer.");
    }
  }
}
