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

import java.util.Properties;

import jcckit.graphic.BasicGraphicAttributes;
import jcckit.graphic.GraphPoint;
import jcckit.graphic.GraphicAttributes;
import jcckit.graphic.GraphicalComposite;
import jcckit.graphic.GraphicalElement;
import jcckit.graphic.Polygon;
import jcckit.graphic.Rectangle;
import jcckit.graphic.ShapeAttributes;
import jcckit.graphic.Text;
import jcckit.graphic.TextAttributes;
import jcckit.util.ConfigData;
import jcckit.util.ConfigParameters;
import jcckit.util.ConfigParametersBasedConfigData;
import jcckit.util.Factory;
import jcckit.util.PropertiesBasedConfigData;


/**
 * Helper class for creating the legend of a {@link Plot}.
 * 
 * @author Franz-Josef Elmer
 */
public class Legend {
  /** Configuration parameter key. */
  public static final String UPPER_RIGHT_CORNER_KEY = "upperRightCorner",
                             BOX_WIDTH_KEY = "boxWidth",
                             BOX_HEIGHT_KEY = "boxHeight",
                             BOX_ATTRIBUTES_KEY = "boxAttributes",
                             TITLE_KEY = "title",
                             TITLE_DISTANCE_KEY = "titleDistance",
                             TITLE_ATTRIBUTES_KEY = "titleAttributes",
                             LEFT_DISTANCE_KEY = "leftDistance",
                             BOTTOM_DISTANCE_KEY = "bottomDistance",
                             TOP_DISTANCE_KEY = "topDistance",
                             LINE_LENGTH_KEY = "lineLength",
                             SYMBOL_SIZE_KEY = "symbolSize",
                             CURVE_TITLE_DISTANCE_KEY = "curveTitleDistance",
                             CURVE_TITLE_ATTRIBUTES_KEY
                                              = "curveTitleAttributes";

  private final GraphicalComposite _box;
  private final TextAttributes _curveTitleAttributes;
  private final double _xSymbol;
  private final double _xText;
  private final double _yBase;
  private final double _yLastRow;
  private final double _length;
  private final double _size;

  /**
   * Creates an instance from the specified configuration parameters.
   * All numbers (lengths, fontsizes, linethicknesses, etc.) are in
   * device-independent units.
   * <table border=1 cellpadding=5>
   * <tr><th>Key &amp; Default Value</th><th>Type</th><th>Mandatory</th>
   *     <th>Description</th></tr>
   * <tr><td><tt>bottomDistance = 0.02</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Distance between the last row and the bottom of the legend box.
   *         </td></tr>
   * <tr><td><tt>boxAttributes = </tt>default values of 
   *         {@link ShapeAttributes} with a white fill color.</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Attributes of the legend box.</td></tr>
   * <tr><td><tt>boxHeight = 0.1</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Height of the legend box.</td></tr>
   * <tr><td><tt>boxWidth = 0.2</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Width of the legend box.</td></tr>
   * <tr><td><tt>curveTitleAttributes = </tt>default values of 
   *         {@link BasicGraphicAttributes}</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Text attributes of curve titles printed in the legend.</td></tr>
   * <tr><td><tt>curveTitleDistance = 0.005</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Horizontal distance between the line part of the legend symbol
   *         and the curve title.</td></tr>
   * <tr><td><tt>leftDistance = 0.01</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Horizontal distance between the line part of the legend symbol
   *         and the left border of the legend box.</td></tr>
   * <tr><td><tt>lineLength = 0.035</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Length of the line part of the legend symbol.</td></tr>
   * <tr><td><tt>symbolSize = 0.01</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Size of the symbol part of the legend symbol. Will be the
   *         <tt>size</tt> argument of {@link SymbolFactory#createLegendSymbol
   *         createLegendSymbol} in a {@link SymbolFactory}.</td></tr>
   * <tr><td><tt>titleAttributes = </tt>default values of 
   *         {@link BasicGraphicAttributes} with a text anchor CENTER 
   *         TOP.</td>
   *     <td><tt>ConfigParameters</tt></td><td>no</td>
   *     <td>Text attributes of the title of the legend box.</td></tr>
   * <tr><td><tt>title = Legend</tt></td>
   *     <td><tt>String</tt></td><td>no</td>
   *     <td>Title of the legend box.</td></tr>
   * <tr><td><tt>titleDistance = 0.005</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Distance between the center of the upper line of the legend box
   *         and the anchor of the legend title.</td></tr>
   * <tr><td><tt>topDistance = 0.04</tt></td>
   *     <td><tt>double</tt></td><td>no</td>
   *     <td>Distance between the first row and the top of the legend box.
   *         </td></tr>
   * <tr><td><tt>upperRightCorner = 0.94,&nbsp;0.54</tt></td>
   *     <td><tt>double[]</tt></td><td>no</td>
   *     <td>Position of the upper-right corner of the legend box.</td></tr>
   * </table>
   */
  public Legend(ConfigParameters config) {
    config = mergeWithDefaultConfig(config);
    GraphPoint corner
        = new GraphPoint(config.getDoubleArray(UPPER_RIGHT_CORNER_KEY,
                                               new double[] {0.94, 0.54}));
    double width = config.getDouble(BOX_WIDTH_KEY, 0.2);
    double height = config.getDouble(BOX_HEIGHT_KEY, 0.1);
    _curveTitleAttributes = (TextAttributes) Factory.create(
        config.getNode(CURVE_TITLE_ATTRIBUTES_KEY));
    _xSymbol = corner.getX() - width
               + config.getDouble(LEFT_DISTANCE_KEY, 0.01);
    _yBase = corner.getY() - config.getDouble(TOP_DISTANCE_KEY, 0.04);
    _yLastRow = corner.getY() - height 
                + config.getDouble(BOTTOM_DISTANCE_KEY, 0.02);
    _length = config.getDouble(LINE_LENGTH_KEY, 0.035);
    _size = config.getDouble(SYMBOL_SIZE_KEY, 0.01);
    _xText = _xSymbol + _length
             + config.getDouble(CURVE_TITLE_DISTANCE_KEY, 0.005);

    _box = new GraphicalComposite(null);
    double xCenter = corner.getX() - width / 2;
    _box.addElement(new Rectangle(
        new GraphPoint(xCenter, corner.getY() - height / 2), width, height,
        (GraphicAttributes) Factory.create(
                                config.getNode(BOX_ATTRIBUTES_KEY))));
    _box.addElement(new Text(
        new GraphPoint(xCenter, corner.getY()
                                - config.getDouble(TITLE_DISTANCE_KEY, 0.005)),
        config.get(TITLE_KEY, "Legend"),
        (TextAttributes) Factory.create(
                                config.getNode(TITLE_ATTRIBUTES_KEY))));
  }

  private ConfigParameters mergeWithDefaultConfig(ConfigParameters config) {
    Properties p = new Properties();
    p.put(BOX_ATTRIBUTES_KEY + '/' + Factory.CLASS_NAME_KEY,
          ShapeAttributes.class.getName());
    p.put(BOX_ATTRIBUTES_KEY + '/'
          + ShapeAttributes.FILL_COLOR_KEY, "0xffffff");
    p.put(BOX_ATTRIBUTES_KEY + '/'
          + ShapeAttributes.LINE_COLOR_KEY, "0");
    p.put(TITLE_ATTRIBUTES_KEY + '/' + Factory.CLASS_NAME_KEY,
          BasicGraphicAttributes.class.getName());
    p.put(TITLE_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.HORIZONTAL_ANCHOR_KEY, "center");
    p.put(TITLE_ATTRIBUTES_KEY + '/'
          + BasicGraphicAttributes.VERTICAL_ANCHOR_KEY, "top");
    p.put(CURVE_TITLE_ATTRIBUTES_KEY + '/' + Factory.CLASS_NAME_KEY,
          BasicGraphicAttributes.class.getName());
    ConfigData cd = new PropertiesBasedConfigData(p);
    cd = new ConfigParametersBasedConfigData(config, new ConfigParameters(cd));
    return new ConfigParameters(cd);
  }

  /** 
   * Returns the legend box with title but without legend symbols and curve 
   * titles.
   */
  public GraphicalElement getBox() {
    return _box;
  }

  /**
   * Creates the symbol part of a legend symbol.
   * @param curveIndex Index of the curve. Will be needed to calculate the 
   *        y-coordinate of the symbol.
   * @param numberOfCurves Number of curves. Will be needed to calculate the 
   *        y-coordinate of the symbol.
   * @param factory Factory for the symbol part of the legend symbol.
   *        Can be <tt>null</tt>.
   * @param withLine <tt>true</tt> if the line part of the legend symbol
   *        should be created.
   * @param lineAttributes Attributes of the line part.
   */
  public GraphicalElement createSymbol(int curveIndex, int numberOfCurves,
                                       SymbolFactory factory,
                                       boolean withLine,
                                       GraphicAttributes lineAttributes) {
    GraphicalComposite result = new GraphicalComposite(null);
    double y = calculateBaseLine(curveIndex, numberOfCurves);
    if (withLine) {
      Polygon line = new Polygon(lineAttributes, false);
      line.addPoint(new GraphPoint(_xSymbol, y));
      line.addPoint(new GraphPoint(_xSymbol + _length, y));
      result.addElement(line);
    }
    if (factory != null) {
      result.addElement(factory.createLegendSymbol(
                          new GraphPoint(_xSymbol + _length / 2, y), _size));
    }
    return result;
  }

  private double calculateBaseLine(int curveIndex, int numberOfCurves) {
    if (numberOfCurves > 1) {
      return _yBase + ((_yLastRow - _yBase) / (numberOfCurves - 1)) 
                      * curveIndex;
    } else {
      return 0.5 * (_yBase + _yLastRow);
    }
  }

  /**
   * Creates the title part of a legend symbol.
   * @param curveIndex Index of the curve. Will be needed to calculate the 
   *        y-coordinate of the title.
   * @param numberOfCurves Number of curves. Will be needed to calculate the 
   *        y-coordinate of the symbol.
   * @param title Title text.
   */
  public GraphicalElement createCurveTitle(int curveIndex, int numberOfCurves,
                                           String title) {
    return new Text(new GraphPoint(_xText, calculateBaseLine(curveIndex, 
                                                             numberOfCurves)),
                    title, _curveTitleAttributes);
  }
}
