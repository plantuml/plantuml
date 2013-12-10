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
 *  Basic attributes for shapes.
 *
 *  @author Franz-Josef Elmer
 */
public class ShapeAttributes implements LineAttributes, FillAttributes {
  /** Configuration parameter key. */
  public static final String FILL_COLOR_KEY = "fillColor",
                             LINE_COLOR_KEY = "lineColor",
                             LINE_THICKNESS_KEY = "lineThickness",
                             LINE_PATTERN_KEY = "linePattern";

  private final Color _fillColor;
  private final Color _lineColor;
  private final double _lineThickness;
  private final double[] _linePattern;

  /**
   * Creates a new instance based on the specified configuration
   * parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>fillColor = <i>no filling</i></tt></td><td><tt>Color</tt></td>
   *     <td>no</td><td>The fill color of the shape.</td></tr>
   * <tr><td><tt>lineColor = <i>no line<i></tt></td><td><tt>Color</tt></td>
   *     <td>no</td><td>The color of a line, a polygon, or the border of a shape.</td></tr>
   * <tr><td><tt>lineThickness = 0</tt></td><td><tt>double</tt></td>
   *     <td>no</td>
   *     <td>The thickness of a line. A thickness of zero means that
   *     the renderer will draw the thinest line possible.</td></tr>
   * <tr><td><tt>linePattern = </tt><i>solid line</i></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>A sequence of lengths where the pen is alternatively
   *     down or up. For example, <tt>0.1 0.1</tt> will lead to a dashed
   *     line whereas <tt>0.02 0.02</tt> is the pattern of a dotted
   *     line and <tt>0.02 0.02 0.1 0.02</tt> of a dashed-dotted
   *     line.</td></tr>
   * </table>
   */
  public ShapeAttributes(ConfigParameters config) {
    this(config.getColor(FILL_COLOR_KEY, null),
         config.getColor(LINE_COLOR_KEY, null),
         config.getDouble(LINE_THICKNESS_KEY, 0),
         config.getDoubleArray(LINE_PATTERN_KEY, null));
  }

  /**
   * Creates a new instance.
   * @param fillColor The fill color. May be <tt>null</tt>.
   * @param lineColor The line color. May be <tt>null</tt>.
   * @param lineThickness Thickness of the line.
   *        Negative numbers will be trimmed to zero.
   * @param linePattern Line pattern. May be <tt>null</tt>.
   */
  public ShapeAttributes(Color fillColor, Color lineColor,
                         double lineThickness, double[] linePattern) {
    _fillColor = fillColor;
    _lineColor = lineColor;
    _lineThickness = Math.max(0, lineThickness);
    _linePattern = linePattern;
  }

  public Color getFillColor() {
    return _fillColor;
  }

  public Color getLineColor() {
    return _lineColor;
  }

  public double getLineThickness() {
    return _lineThickness;
  }

  public double[] getLinePattern() {
    return _linePattern;
  }
}

