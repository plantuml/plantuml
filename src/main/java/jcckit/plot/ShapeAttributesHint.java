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

import java.awt.Color;

import jcckit.graphic.GraphicAttributes;
import jcckit.graphic.ShapeAttributes;
import jcckit.util.ConfigParameters;

/**
 * An {@link AttributesHint} which wraps {@link ShapeAttributes}.
 * Each call of {@link #getNextHint()} returns a new instance of
 * <tt>ShapeAttributes</tt> where fill color, line color and/or 
 * line thickness has been increased by a constant amount.
 * 
 * @author Franz-Josef Elmer
 */
public class ShapeAttributesHint implements AttributesHint, Cloneable {
  /** Configuration parameter key. */
  public static final String INITIAL_ATTRIBUTES_KEY = "initialAttributes",
                             FILL_COLOR_HSB_INCREMENT_KEY 
                                            = "fillColorHSBIncrement",
                             LINE_COLOR_HSB_INCREMENT_KEY 
                                            = "lineColorHSBIncrement",
                             LINE_THICKNESS_INCREMENT_KEY 
                                            = "lineThicknessIncrement";
  private static float[] extractHSB(Color color) {
    return color == null ? null 
        : Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), 
                         null);
  }
  
  private static Color createColor(float[] colorHSB) {
    return colorHSB == null ? null 
        : Color.getHSBColor(colorHSB[0], colorHSB[1], colorHSB[2]);
  }

  private static float[] incrementColor(float[] colorHSB, 
                                        double[] increments) {
    float[] result = null;
    if (colorHSB != null) {
      result = (float[]) colorHSB.clone();
      for (int i = 0; i < 3; i++) {
        result[i] += increments[i];
      }
    }
    return result;
  }

  private float[] _fillColorHSB;
  private float[] _lineColorHSB;
  private double _lineThickness;
  private double[] _linePattern;
  private double[] _fillColorHSBIncrement;
  private double[] _lineColorHSBIncrement;
  private double _lineThicknessIncrement;

  /**
   * Creates an instance from the specified configuration parameters.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>initialAttributes = </tt><i>default values of
   *         {@link ShapeAttributes}</i></td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Initial values of shape attributes. Note, that default
   *         fill and line colors are undefined (they depend on the
   *         <tt>Renderer</tt>). In this case color increments have no effects.
   *         </td></tr>
   * <tr><td><tt>fillColorHSBIncrement = 0 0 0</tt></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Hue, saturation, and brightness increments of the fill color.
   *         </td></tr>
   * <tr><td><tt>lineColorHSBIncrement = 0 0 0</tt></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Hue, saturation, and brightness increments of the line color.
   *         </td></tr>
   * <tr><td><tt>lineThicknessIncrement = 0</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Line thickness increment.</td></tr>
   * </table>
   */
  public ShapeAttributesHint(ConfigParameters config) {
    ShapeAttributes attributes 
        = new ShapeAttributes(config.getNode(INITIAL_ATTRIBUTES_KEY));
    _fillColorHSB = extractHSB(attributes.getFillColor());
    _lineColorHSB = extractHSB(attributes.getLineColor());
    _lineThickness = attributes.getLineThickness();
    _linePattern = attributes.getLinePattern();
    
    _fillColorHSBIncrement 
          = config.getDoubleArray(FILL_COLOR_HSB_INCREMENT_KEY, new double[3]);
    _lineColorHSBIncrement 
          = config.getDoubleArray(LINE_COLOR_HSB_INCREMENT_KEY, new double[3]);
    _lineThicknessIncrement 
          = config.getDouble(LINE_THICKNESS_INCREMENT_KEY, 0);
  }
  
  /**
   * Creates a new <tt>ShapeAttributesHint</tt> where all attributes has been
   * incremented.
   */
  public AttributesHint getNextHint() {
    ShapeAttributesHint nextHint = null;
    try {
      nextHint = (ShapeAttributesHint) clone();
    } catch (CloneNotSupportedException e) {}
    nextHint._fillColorHSB 
        = incrementColor(_fillColorHSB, _fillColorHSBIncrement);
    nextHint._lineColorHSB 
        = incrementColor(_lineColorHSB, _lineColorHSBIncrement);
    nextHint._lineThickness 
        = Math.max(0, _lineThickness + _lineThicknessIncrement);
    return nextHint;
  }

  /** Returns the wrapped {@link ShapeAttributes} instance. */
  public GraphicAttributes getAttributes() {
    return new ShapeAttributes(createColor(_fillColorHSB),
                               createColor(_lineColorHSB), 
                               _lineThickness, _linePattern);
  }
}
