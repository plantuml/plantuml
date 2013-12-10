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

import jcckit.util.ConfigParameters;

/**
 * The basic attributes of any {@link BasicGraphicalElement}. This is an
 * extension of {@link ShapeAttributes} implementing {@link TextAttributes}.
 *
 * @author Franz-Josef Elmer
 */
public class BasicGraphicAttributes extends ShapeAttributes
                                    implements TextAttributes {
  /** Configuration parameter key. */
  public static final String TEXT_COLOR_KEY = "textColor",
                             FONT_NAME_KEY = "fontName",
                             FONT_STYLE_KEY = "fontStyle",
                             FONT_SIZE_KEY = "fontSize",
                             HORIZONTAL_ANCHOR_KEY = "horizontalAnchor",
                             VERTICAL_ANCHOR_KEY = "verticalAnchor",
                             ORIENTATION_ANGLE_KEY = "orientationAngle";

  private final Color _textColor;
  private final String _fontName;
  private final FontStyle _fontStyle;
  private final double _fontSize;
  private final double _orientationAngle;
  private final Anchor _horizontalAnchor;
  private final Anchor _verticalAnchor;

  /**
   * Creates a new instance based on the specified configuration
   * parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>textColor = </tt><i>default foreground color of the
   *     renderer</i></td><td><tt>Color</tt></td><td>no</td>
   *     <td>The text color.</td></tr>
   * <tr><td><tt>fontName = </tt><i>default font name of the
   *     renderer</i></td><td><tt>String</tt></td><td>no</td>
   *     <td>The name of the text font. The standard Java font name
   *         "Serif", "SansSerif", and "Monospaced" can be used.
   *         Other font names depend on the actual {@link Renderer}
   *         rendering the corresponding {@link BasicGraphicalElement}.
   *         </td></tr>
   * <tr><td><tt>fontStyle = normal</tt></td><td><tt>String</tt>
   *     </td><td>no</td>
   *     <td>The font style. Possible values are:
   *     <ul><li><tt>normal</tt><li><tt>bold</tt><li><tt>italic</tt>
   *     <li><tt>bold italic</tt></ul>
   *     </td></tr>
   * <tr><td><tt>fontSize = </tt><i>default font size of the
   *     renderer</i></td><td><tt>double</tt></td><td>no</td>
   *     <td>The font size in units of the device-independent
   *     coordinates.</td></tr>
   * <tr><td><tt>orientationAngle = 0</tt></td><td><tt>double</tt></td>
   *     <td>no</td>
   *     <td>The orientation angle of the text (in degree).
   *     Zero means normal orientation whereas a positive value means
   *     a rotation in counter-clockweise direction.</td></tr>
   * <tr><td><tt>horizontalAnchor = left</tt></td><td><tt>String</tt>
   *     </td><td>no</td>
   *     <td>Anchor for horizontal text position. Possible values are
   *     <tt>left</tt>, <tt>center</tt>, and <tt>right</tt>.</td></tr>
   * <tr><td><tt>verticalAnchor = center</tt></td><td><tt>String</tt>
   *     </td><td>no</td>
   *     <td>Anchor for vertical text position. Possible values are
   *     <tt>top</tt>, <tt>center</tt>, and <tt>bottom</tt>.</td></tr>
   * </table>
   * Additional configuration parameters are explained in the
   * {@link ShapeAttributes#ShapeAttributes constructor}
   * of the superclass {@link ShapeAttributes}.
   */
  public BasicGraphicAttributes(ConfigParameters config) {
    super(config);
    _textColor = config.getColor(TEXT_COLOR_KEY, null);
    _fontName = config.get(FONT_NAME_KEY, null);
    _fontStyle = FontStyle.getFontStyle(config, FONT_STYLE_KEY,
                                        FontStyle.NORMAL);
    _fontSize = config.getDouble(FONT_SIZE_KEY, 0);
    _orientationAngle = config.getDouble(ORIENTATION_ANGLE_KEY, 0);

    _horizontalAnchor = Anchor.getHorizontalAnchor(config,
                                  HORIZONTAL_ANCHOR_KEY, Anchor.LEFT_BOTTOM);
    _verticalAnchor = Anchor.getVerticalAnchor(config,
                                  VERTICAL_ANCHOR_KEY, Anchor.CENTER);
  }

  /**
   * Creates a new instance.
   * @param fillColor The fill color. May be <tt>null</tt>.
   * @param lineColor The line color. May be <tt>null</tt>.
   * @param lineThickness Thickness of the line.
   *        Negative numbers will be trimmed to zero.
   * @param linePattern Line pattern. May be <tt>null</tt>.
   * @param textColor The text color. May be <tt>null</tt>.
   * @param fontName The font name. May be <tt>null</tt>.
   * @param fontStyle The font style. May be <tt>null</tt>.
   * @param fontSize The font size in units of the device-independent
   *        coordinates. May be <tt>null</tt>.
   * @param orientationAngle Orientation angle of the text.
   * @param horizontalAnchor Horizontal text anchor.
   * @param verticalAnchor Vertical text anchor.
   */
  public BasicGraphicAttributes(Color fillColor, Color lineColor,
                                double lineThickness,
                                double[] linePattern, Color textColor,
                                String fontName, FontStyle fontStyle,
                                double fontSize, double orientationAngle,
                                Anchor horizontalAnchor,
                                Anchor verticalAnchor) {
    super(fillColor, lineColor, lineThickness, linePattern);
    _textColor = textColor;
    _fontName = fontName;
    _fontStyle = fontStyle;
    _fontSize = fontSize;
    _orientationAngle = orientationAngle;
    _horizontalAnchor = horizontalAnchor;
    _verticalAnchor = verticalAnchor;
  }

  /**
   *  Returns the text color.
   *  @return <tt>null</tt> means default color of the renderer.
   */
  public Color getTextColor() {
    return _textColor;
  }

  /**
   *  Returns the font name.
   *  @return <tt>null</tt> means default font name of the renderer.
   */
  public String getFontName() {
    return _fontName;
  }

  /**
   *  Returns the font style.
   *  @return <tt>null</tt> means default font style of the renderer.
   */
  public FontStyle getFontStyle() {
    return _fontStyle;
  }

  /**
   *  Returns the font size in units of the device-independent coordinates.
   */
  public double getFontSize() {
    return _fontSize;
  }

  /**
   *  Returns the orientation angle in degree. Zero means
   *  normal text orientation. Any positive angle means a
   *  counter-clockwise rotation of the text.
   */
  public double getOrientationAngle() {
    return _orientationAngle;
  }

  /**
   *  Returns the anchor for horizontal position of the text.
   *  Note, that the anchor is related to the text <em>before</em>
   *  it is rotated by the orientation angle.
   *  @return one of the three instances of <tt>Anchor</tt>.
   */
  public Anchor getHorizontalAnchor() {
    return _horizontalAnchor;
  }

  /**
   *  Returns the anchor for vertical position of the text.
   *  Note, that the anchor is related to the text <em>before</em>
   *  it is rotated by the orientation angle.
   *  @return one of the three instances of <tt>Anchor</tt>.
   */
  public Anchor getVerticalAnchor() {
    return _verticalAnchor;
  }
}

